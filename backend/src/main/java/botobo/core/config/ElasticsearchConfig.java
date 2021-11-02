package botobo.core.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {
    // TODO 서브모듈 업데이트 귀찮아서 그냥 박아놓음.
//    @Value("${elasticsearch.host}")
//    private String host;
//    @Value("${elasticsearch.port}")
//    private String port;

    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("127.0.0.1:9200")
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
}
