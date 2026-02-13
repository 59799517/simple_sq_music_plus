package com.sqmusicplus.v3.plug.apple.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname TipResult
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/10/13 13:57
 * @Created by sq
 */

@NoArgsConstructor
@Data
public class TipResult {

    @JsonProperty("hints")
    private HintsDTO hints;
    @JsonProperty("success")
    private Boolean success;

    @NoArgsConstructor
    @Data
    public static class HintsDTO {
        @JsonProperty("meta")
        private MetaDTO meta;
        @JsonProperty("results")
        private ResultsDTO results;

        @NoArgsConstructor
        @Data
        public static class MetaDTO {
            @JsonProperty("metrics")
            private MetricsDTO metrics;

            @NoArgsConstructor
            @Data
            public static class MetricsDTO {
                @JsonProperty("dataSetId")
                private String dataSetId;
            }
        }

        @NoArgsConstructor
        @Data
        public static class ResultsDTO {
            @JsonProperty("terms")
            private List<String> terms;
        }
    }
}
