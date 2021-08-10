package botobo.core.utils;

import lombok.Getter;

public class DummyRequestBuilder {

    public static DummyRequest build() {
        return new DummyRequest();
    }

    @Getter
    private static class DummyRequest {
        private String _dummy;
    }
}
