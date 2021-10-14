package botobo.core.utils;

import botobo.core.ui.search.WorkbookSearchParameter;

public class WorkbookSearchParameterUtils {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String searchKeyword;
        private String searchCriteria;
        private String start;
        private String size;

        public Builder searchKeyword(String searchKeyword) {
            this.searchKeyword = searchKeyword;
            return this;
        }

        public Builder searchCriteria(String searchCriteria) {
            this.searchCriteria = searchCriteria;
            return this;
        }

        public Builder start(String start) {
            this.start = start;
            return this;
        }

        public Builder size(String size) {
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
}
