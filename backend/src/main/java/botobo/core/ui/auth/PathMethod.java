package botobo.core.ui.auth;

public enum PathMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE, ANY;

    public boolean match(String httpMethod) {
        return this.name().equals(httpMethod);
    }
}
