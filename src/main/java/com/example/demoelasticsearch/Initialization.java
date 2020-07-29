package com.example.demoelasticsearch;


import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Initialization implements CommandLineRunner {

    static final Logger LOGGER = LoggerFactory.getLogger(Initialization.class);

    private final RestHighLevelClient client;
    private final ElasticIndexProperties elasticIndexProperties;

    @Value("${elastic.index.number_of_shards:5}")
    private int indexNumberOfShards;

    @Value("${elastic.index.number_of_replicas:1}")
    private int indexNumberOfReplicas;


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

        /*
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
        */

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
