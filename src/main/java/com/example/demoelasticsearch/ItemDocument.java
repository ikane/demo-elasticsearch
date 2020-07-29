package com.example.demoelasticsearch;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "#{@elasticIndexProperties.name}", type = "#{@elasticIndexProperties.type}", createIndex = true)
@Data
@Builder(toBuilder = true)
public class ItemDocument {

    @Id
    private String id;
    private String name;
    private double price;
    private boolean newItem;
    private String description;
    private String composition;
}
