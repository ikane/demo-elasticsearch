package com.example.demoelasticsearch;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.io.IOException;
import java.util.Map;

@Configuration
@EnableElasticsearchRepositories/*(basePackages = "com.example.demorabbitmqelastic.repository")*/
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    @Value("${elasticsearch.host:localhost}")
    private String host;

    @Value("${elasticsearch.port:0}")
    private int port;

    //@Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        //return RestClients.create(ClientConfiguration.localhost()).rest();

        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                //.connectedTo(new InetSocketAddress(host, port))
                .connectedTo(getHttpHostAddress())
                .build();

        return RestClients.create(clientConfiguration).rest();
    }

    public String getHttpHostAddress() {
        String result = host;
        if(port != 0) {
            result += ":" + port;
        }
        return result;
    }

    /*
    @Bean
    public ElasticsearchRestTemplate elasticsearchTemplate(ElasticsearchProperties configuration) {
        var nodes =  Stream.of(configuration.getClusterNodes().split(",")).map(HttpHost::create).toArray(HttpHost[]::new);
        var client = new RestHighLevelClient(RestClient.builder(nodes));
        var converter = new CustomElasticSearchConverter(new SimpleElasticsearchMappingContext(), createConversionService());
        return new ElasticsearchRestTemplate(client, converter, new DefaultResultMapper(converter));
    }
     */

    @Bean
    @Override
    public EntityMapper entityMapper() {
        return new ElasticCustomEntityMapper();
    }

    private class ElasticCustomEntityMapper implements EntityMapper {

        private ObjectMapper mapper;

        public ElasticCustomEntityMapper() {
            this.mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.registerModule(new JavaTimeModule());
        }

        @Override
        public String mapToString(Object object) throws IOException {
            return mapper.writeValueAsString(object);
        }

        @Override
        public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
            return mapper.readValue(source, clazz);
        }

        /**
         * Map the given {@literal source} to {@link Map}.
         *
         * @param source must not be {@literal null}.
         *
         * @return never {@literal null}
         *
         * @since 3.2
         */
        @Override
        public Map<String, Object> mapObject(Object source) {
            return mapper.convertValue(source, Map.class);
        }

        /**
         * Map the given {@link Map} into an instance of the {@literal targetType}.
         *
         * @param source     must not be {@literal null}.
         * @param targetType must not be {@literal null}.
         *
         * @return can be {@literal null}.
         *
         * @since 3.2
         */
        @Override
        public <T> T readObject(Map<String, Object> source, Class<T> targetType) {
            return mapper.convertValue(source, targetType);
        }
    }
}
