package com.sqmusicplus.v3.config;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/** Uses a disposable MySQL schema named sqmusic_migration_test. */
@EnabledIfEnvironmentVariable(named = "SQMUSIC_TEST_JDBC_URL", matches = ".+")
class RetryMigrationTest {
    private Connection connection;
    private String migration;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection(System.getenv("SQMUSIC_TEST_JDBC_URL"),
                System.getenv().getOrDefault("SQMUSIC_TEST_DB_USER", "root"),
                System.getenv().getOrDefault("SQMUSIC_TEST_DB_PASSWORD", ""));
        assertEquals("sqmusic_migration_test", connection.getCatalog());
        try (InputStream input = getClass().getResourceAsStream("/db/3.1.23update.sql")) {
            assertNotNull(input);
            migration = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
        execute("DROP TABLE IF EXISTS download_info");
        execute("DROP TABLE IF EXISTS sq_config");
        execute("CREATE TABLE download_info (parent_download_id varchar(255))");
        execute("CREATE TABLE sq_config (config_name varchar(255), config_value text, "
                + "config_key varchar(255), config_type varchar(50), config_options text, "
                + "config_show int, config_remark text, config_null_check int, config_disabled int)");
        execute("INSERT INTO sq_config (config_key, config_value, config_disabled) VALUES "
                + "('system.show.traffic.monitoring', 'true', 1), "
                + "('plug.netease.baseurl', 'old-default', 0)");
    }

    @AfterEach
    void tearDown() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    void freshMigrationCanBeRepeated() throws Exception {
        migrate(migration);
        migrate(migration);
        assertMigrationComplete();
    }

    @Test
    void recoversAfterMissingTerminatorFailure() throws Exception {
        String incomplete = migration.stripTrailing();
        assertThrows(RuntimeException.class,
                () -> migrate(incomplete.substring(0, incomplete.length() - 1)));
        assertEquals("1", query("SELECT config_disabled FROM sq_config "
                + "WHERE config_key='system.show.traffic.monitoring'"));
        migrate(migration);
        assertMigrationComplete();
    }

    @Test
    void recoversWhenOnlyFirstColumnExists() throws Exception {
        execute("ALTER TABLE download_info ADD COLUMN download_retry_num int");
        migrate(migration);
        assertMigrationComplete();
    }

    @Test
    void preservesExistingRetrySettings() throws Exception {
        execute("INSERT INTO sq_config (config_key, config_value) "
                + "VALUES ('system.download.timeout.retry.interval', '1440')");
        migrate(migration);
        assertMigrationComplete();
        assertEquals("1440", query("SELECT config_value FROM sq_config "
                + "WHERE config_key='system.download.timeout.retry.interval'"));
    }

    private void migrate(String sql) {
        ScriptRunner runner = new ScriptRunner(connection);
        runner.setStopOnError(true);
        runner.setAutoCommit(true);
        runner.setLogWriter(null);
        runner.setErrorLogWriter(null);
        runner.runScript(new StringReader(sql));
    }

    private void assertMigrationComplete() throws Exception {
        assertEquals("2", query("SELECT COUNT(*) FROM information_schema.columns "
                + "WHERE table_schema=DATABASE() AND table_name='download_info' "
                + "AND column_name IN ('download_retry_num', 'download_retry_time')"));
        assertEquals("2", query("SELECT COUNT(*) FROM sq_config WHERE config_key IN "
                + "('system.download.timeout.retry.interval', 'system.download.timeout.retry.num')"));
        assertEquals("0", query("SELECT config_disabled FROM sq_config "
                + "WHERE config_key='system.show.traffic.monitoring'"));
    }

    private void execute(String sql) throws Exception {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    private String query(String sql) throws Exception {
        try (Statement statement = connection.createStatement(); ResultSet result = statement.executeQuery(sql)) {
            assertTrue(result.next());
            return result.getString(1);
        }
    }
}
