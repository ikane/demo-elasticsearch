package com.example.demoelasticsearch;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "elastic.index")
public class ElasticIndexProperties {
	private String name;
	private String type;
	private Integer numberOfShards = 5;
	private Integer numberOfReplicas = 1;

	private List<CustomIndexSettings> indices;
}
