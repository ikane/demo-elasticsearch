package com.example.demoelasticsearch;

import lombok.Data;

@Data
public class CustomIndexSettings {
    private String name;
    private String type;
    private Integer numberOfShards = 5;
    private Integer numberOfReplicas = 1;
}
