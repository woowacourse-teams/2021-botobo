package botobo.core.user.domain;

public enum Role {
    ANONYMOUS,
    USER;

    public boolean isAnonymous() {
        return this == ANONYMOUS;
    }
}
