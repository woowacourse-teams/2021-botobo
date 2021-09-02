package botobo.core.ui.search;

import java.util.Objects;

public class SearchRelated {

    private static final String EMTPY = "";

    private final String value;

    public SearchRelated(String value) {
        this.value = resolve(value);
    }

    private String resolve(String tag) {
        if (Objects.isNull(tag)) {
            return EMTPY;
        }
        tag = tag.trim();
        return tag.toLowerCase();
    }

    public String get() {
        return value;
    }
}
