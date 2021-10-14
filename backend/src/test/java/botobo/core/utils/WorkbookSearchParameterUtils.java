package botobo.core.utils;

import botobo.core.ui.search.WorkbookSearchParameter;

public class WorkbookSearchParameterUtils {

    private String searchKeyword;
    private String searchCriteria;
    private String start;
    private String size;

    private WorkbookSearchParameterUtils() {

    }

    public static WorkbookSearchParameterUtils builder() {
        return new WorkbookSearchParameterUtils();
    }

    public WorkbookSearchParameterUtils searchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
        return this;
    }

    public WorkbookSearchParameterUtils searchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
        return this;
    }

    public WorkbookSearchParameterUtils start(String start) {
        this.start = start;
        return this;
    }

    public WorkbookSearchParameterUtils size(String size) {
        this.size = size;
        return this;
    }

    public WorkbookSearchParameter build() {
        return WorkbookSearchParameter.ofRequest(
                searchCriteria,
                searchKeyword,
                start,
                size
        );
    }
}
