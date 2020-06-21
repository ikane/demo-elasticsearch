package com.example.demoelasticsearch;


import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class Initialization implements CommandLineRunner {

    private final RestHighLevelClient client;

    @Value("${elastic.index.number_of_shards:5}")
    private int indexNumberOfShards;

    @Value("${elastic.index.number_of_replicas:1}")
    private int indexNumberOfReplicas;


    public Initialization(RestHighLevelClient client) {
        this.client = client;
    }

    @Override
    public void run(String... args) throws Exception {

        String indexName = "item_index";
        try {
            GetIndexRequest request = new GetIndexRequest(indexName);
            if (!client.indices().exists(request, RequestOptions.DEFAULT)) {
                CreateIndexRequest indexRequest = new CreateIndexRequest(indexName);
                indexRequest.settings(Settings.builder()
                        .put("index.number_of_shards", indexNumberOfShards)
                        .put("index.number_of_replicas", indexNumberOfReplicas)
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
