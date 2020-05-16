package com.example.demoelasticsearch;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

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

}
