package botobo.core.user.domain;

public enum Role {
    ANONYMOUS,
    USER,
    ADMIN;

    public boolean isAnonymous() {
        return this == ANONYMOUS;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
