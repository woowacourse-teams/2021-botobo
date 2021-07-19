package botobo.core;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class TestUtils {

    public static Long extractId(ExtractableResponse<Response> response) {
        final String location = response.header("Location");
        return Long.parseLong(location.split("/")[3]);
    }

    public static String longStringGenerator(int max) {
        return "a".repeat(max);
    }
}
