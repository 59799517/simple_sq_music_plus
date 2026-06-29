public class TestPattern {
    public static void main(String[] args) {
        Object result = "test";
        if (result instanceof String s) {
            System.out.println(s);
        }
        int code = result instanceof Number n ? n.intValue() : 0;
    }
}
