package com.example.demoelasticsearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

@Document(indexName = "customers", type = "personal")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Serializable {
    @Id
    private String id;
    private String name;
    private String email;
    private String gender;
    private Boolean isActive;
    private String phone;
    private String street;
    private String city;
    private String state;
    private String eyeColor;
    private String company;
    private List<String> tags;
}
