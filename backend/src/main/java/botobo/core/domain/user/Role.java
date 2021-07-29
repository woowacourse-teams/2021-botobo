package botobo.core.domain.user;

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

    public boolean isUser() {
        return this == USER;
    }
}
