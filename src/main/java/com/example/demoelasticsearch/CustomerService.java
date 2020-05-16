package com.example.demoelasticsearch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public Customer createCustomer(Customer customer) {
        return this.customerRepository.save(customer);
    }

    public List<Customer> findCustomers() {
        List<Customer> result = new ArrayList<>();
        this.customerRepository.findAll().forEach(customer -> result.add(customer));
        return result;
    }

    public List<Customer> findCustomersWithTags(List<String> tags) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        tags.forEach(tag -> queryBuilder.filter(QueryBuilders.matchQuery("tags", tag)));

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("customers")
                .withQuery(queryBuilder)
                .build();

        List<Customer> customers = this.elasticsearchOperations.queryForList(searchQuery, Customer.class);

        return customers;
    }


    public Object getCustomerAggregateByGenderActive() {
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("by_gender").field("gender.keyword");

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("isActive", true));

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("customers")
                .withQuery(boolQueryBuilder)
                .addAggregation(aggregation)
                .build();

        Aggregations aggregations = this.elasticsearchOperations.query(searchQuery, response -> response.getAggregations());
        Map<String, Aggregation> aggregationMap = aggregations.asMap();
        ParsedStringTerms stringTerms = (ParsedStringTerms) aggregationMap.get("by_gender");
        List<? extends Terms.Bucket> buckets = stringTerms.getBuckets();

        Map<String, Long> stats = new HashMap<>();
        buckets.forEach(bucket -> stats.put(bucket.getKeyAsString().toUpperCase(), bucket.getDocCount()));

        return stats;
    }

    public Object testComplexQueries() {
        /*
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("top_genders")
                                                                 .field("gender")
                                                                 .order(Terms.Order.count(false));
        */
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder() //
                                                                       .withQuery(matchAllQuery()) //
                                                                       .withSearchType(SearchType.DEFAULT) //
                                                                       .addAggregation(terms("genders").field(
                                                                               "gender.keyword")) //
                                                                       .build();

        ResultsExtractor<?> resultsExtractor = response -> response.getAggregations();
        Object query = this.elasticsearchOperations.query(searchQuery, resultsExtractor);

        /*
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("gender", "M"));
        CountRequest countRequest = new CountRequest();
        countRequest.source(searchSourceBuilder);

        SearchRequest searchRequest = new SearchRequest("users");
        searchRequest.source(searchSourceBuilder);
        */

        MatchQueryBuilder builder1 = new MatchQueryBuilder("gender", "M");
        SearchQuery searchQuery2 = new NativeSearchQueryBuilder()
                .withQuery(builder1)
                //.withFilter(boolFilter().must(termFilter("gender", "M")))
                .withIndices("users")
                .build();

        long count = this.elasticsearchOperations.count(searchQuery2);

        //*************************
        BoolQueryBuilder builder2 = QueryBuilders.boolQuery();
        builder2.must(new MatchQueryBuilder("gender", "F"));
        builder2.must(new MatchQueryBuilder("married", true));
        builder2.mustNot(new ExistsQueryBuilder("address"));
        //builder.must(QueryBuilders.termQuery("gender", "F"));


        NativeSearchQuery searchQuery3 = new NativeSearchQueryBuilder() //
                                                                       .withQuery(builder2) //
                                                                       .withSearchType(SearchType.DEFAULT) //
                                                                       .build();
        Page<Customer> search = this.customerRepository.search(searchQuery3);
        long totalElements = search.getTotalElements();

        log.info("result {}", query);

        return query;
    }

}
