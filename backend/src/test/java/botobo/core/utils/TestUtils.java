package botobo.core.utils;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class TestUtils {

    public static Long extractId(ExtractableResponse<Response> response) {
        final String location = response.header("Location");
        return Long.parseLong(location.split("/")[4]);
    }

    public static String stringGenerator(int max) {
        return "a".repeat(max);
    }

    public static long measureExecutionStartTime() {
        return System.currentTimeMillis();
    }

    public static void measureExecutionEndTimeFrom(long start) {
        long end = System.currentTimeMillis();
        System.out.println("@@@ findAllByWorkbookName 완료 실행 시간 : " + (end - start));
    }
}
