package com.example.demoelasticsearch;


import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Initialization implements CommandLineRunner {

    private final ElasticsearchOperations elasticsearchOperations;
    private final RestHighLevelClient client;

    @Value("${elastic.index.number_of_shards:5}")
    private int indexNumberOfShards;

    @Value("${elastic.index.number_of_replicas:1}")
    private int indexNumberOfReplicas;


    public Initialization(ElasticsearchOperations elasticsearchOperations, RestHighLevelClient client) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.client = client;
    }

    @Override
    public void run(String... args) throws Exception {

        if(!elasticsearchOperations.indexExists("item_index")) {

            CreateIndexRequest request = new CreateIndexRequest("item_index");
            request.settings(Settings.builder()
                    .put("index.number_of_shards", indexNumberOfShards)
                    .put("index.number_of_replicas", indexNumberOfReplicas)
            );

            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);

            log.info("Index 'item_index' created: {}", createIndexResponse.isAcknowledged());
            /*
            Settings settings = Settings.builder()
                    .put("index.number_of_shards", 3)
                    .put("index.number_of_replicas", 2)
                    .build();

            boolean indexCreated = elasticsearchOperations.createIndex("item_index", settings);

            log.info("Index 'item_index' created: {}", indexCreated);
            */



        }
    }
}
