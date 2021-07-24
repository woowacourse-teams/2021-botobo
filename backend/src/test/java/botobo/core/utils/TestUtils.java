package botobo.core.utils;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class TestUtils {

    public static Long extractId(ExtractableResponse<Response> response) {
        final String location = response.header("Location");
        return Long.parseLong(location.split("/")[4]);
    }

    public static String longStringGenerator(int max) {
        return "a".repeat(max);
    }
}
