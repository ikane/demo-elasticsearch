package com.example.demoelasticsearch;


import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.example.demoelasticsearch.ElasticIndexProperties.INDEX_NUMBER_OF_REPLICAS;
import static com.example.demoelasticsearch.ElasticIndexProperties.INDEX_NUMBER_OF_SHARDS;

@Slf4j
@Component
public class Initialization implements CommandLineRunner {

    static final Logger LOGGER = LoggerFactory.getLogger(Initialization.class);

    private final RestHighLevelClient client;
    private final ElasticIndexProperties elasticIndexProperties;

    public Initialization(
            RestHighLevelClient client,
            ElasticIndexProperties elasticIndexProperties
    ) {
        this.client = client;
        this.elasticIndexProperties = elasticIndexProperties;
    }

    @Override
    public void run(String... args) throws Exception {

        LOGGER.info("Initializing index '{}'...", elasticIndexProperties.getName());


        String indexName = elasticIndexProperties.getName();
        try {
            GetIndexRequest request = new GetIndexRequest(indexName);
            if (!client.indices().exists(request, RequestOptions.DEFAULT)) {
                CreateIndexRequest indexRequest = new CreateIndexRequest(indexName);
                indexRequest.settings(Settings.builder()
                                              .put(INDEX_NUMBER_OF_SHARDS, elasticIndexProperties.getNumberOfShards())
                                              .put(INDEX_NUMBER_OF_REPLICAS, elasticIndexProperties.getNumberOfReplicas())
                );

                CreateIndexResponse createIndexResponse = client.indices().create(indexRequest, RequestOptions.DEFAULT);

                log.info("Index 'item_index' created: {}", createIndexResponse.isAcknowledged());
            }
        } catch (IOException e) {
            log.error(String.format("Can not create index %s", indexName), e);
        }


        /*
        if(!elasticsearchOperations.indexExists("item_index")) {

            CreateIndexRequest request = new CreateIndexRequest("item_index");
            request.settings(Settings.builder()
                    .put("index.number_of_shards", indexNumberOfShards)
                    .put("index.number_of_replicas", indexNumberOfReplicas)
            );

            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);

            log.info("Index 'item_index' created: {}", createIndexResponse.isAcknowledged());
        }
        */
    }
}
