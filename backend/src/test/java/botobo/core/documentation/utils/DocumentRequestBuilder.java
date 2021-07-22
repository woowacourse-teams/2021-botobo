package botobo.core.documentation.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static botobo.core.documentation.utils.DocumentationUtils.getDocumentRequest;
import static botobo.core.documentation.utils.DocumentationUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

public class DocumentRequestBuilder {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static MockMvc mockMvc;

    public MockMvcFunction build() {
        return new MockMvcFunction();
    }

    public static class MockMvcFunction {
        public <T> Options post(String path, T body) {
            return new Options(new PostPerform<>(path, body));
        }

        public <T> Options put(String path, T body) {
            return new Options(new PutPerform<>(path, body));
        }

        public Options get(String path) {
            return new Options(new GetPerform(path));
        }

        public Options delete(String path) {
            return new Options(new DeletePerform(path));
        }

        public MockMvcFunction mockMvc(MockMvc mockMvc) {
            DocumentRequestBuilder.mockMvc = mockMvc;
            return this;
        }

        public Options get(String path, Object... params) {
            return new Options(new GetPerformWithParam(path, params));
        }
    }

    public static class Options {
        private final HttpMethodRequest request;
        private ResultActions resultActions;
        private boolean loginFlag;
        private boolean headerFlag;
        private String headerLocation;
        private String accessToken;

        public Options(HttpMethodRequest request) {
            this.request = request;
            loginFlag = false;
        }

        public Options auth(String token) {
            this.accessToken = token;
            this.loginFlag = true;
            return this;
        }

        public Options headerLocation(String headerLocation) {
            this.headerFlag = true;
            this.headerLocation = headerLocation;
            return this;
        }

        public Options build() throws Exception {
            MockHttpServletRequestBuilder requestBuilder = request.doAction();
            if (loginFlag) {
                requestBuilder = requestBuilder.header("Authorization", "Bearer " + accessToken);
            }
            resultActions = mockMvc.perform(requestBuilder);
            if (headerFlag) {
                resultActions = resultActions.andExpect(header().string("Location", headerLocation));
            }
            return this;
        }

        public void addStatusAndIdentifier(ResultMatcher resultStatus, String documentIdentifier) throws Exception {
            resultActions.andExpect(resultStatus)
                    .andDo(document(documentIdentifier,
                            getDocumentRequest(),
                            getDocumentResponse()));
        }

    }

    interface HttpMethodRequest {
        MockHttpServletRequestBuilder doAction() throws JsonProcessingException;
    }

    private static class PostPerform<T> implements HttpMethodRequest {
        private final String path;
        private final T body;

        public PostPerform(String path, T body) {
            this.path = path;
            this.body = body;
        }

        public MockHttpServletRequestBuilder doAction() throws JsonProcessingException {
            return post(path)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(body));
        }
    }

    private static class PutPerform<T> implements HttpMethodRequest {
        private final String path;
        private final T body;

        public PutPerform(String path, T body) {
            this.path = path;
            this.body = body;
        }

        public MockHttpServletRequestBuilder doAction() throws JsonProcessingException {
            return put(path)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(body));
        }
    }

    private static class GetPerform implements HttpMethodRequest {
        private final String path;

        public GetPerform(String path) {
            this.path = path;
        }

        @Override
        public MockHttpServletRequestBuilder doAction() {
            return get(path);
        }
    }

    private static class GetPerformWithParam implements HttpMethodRequest {
        private final String path;
        private final Object[] params;

        public GetPerformWithParam(String path, Object[] params) {
            this.path = path;
            this.params = params;
        }

        @Override
        public MockHttpServletRequestBuilder doAction() {
            return get(path, params);
        }
    }


    private static class DeletePerform implements HttpMethodRequest {
        private final String path;

        public DeletePerform(String path) {
            this.path = path;
        }

        @Override
        public MockHttpServletRequestBuilder doAction() {
            return delete(path);
        }
    }
}
