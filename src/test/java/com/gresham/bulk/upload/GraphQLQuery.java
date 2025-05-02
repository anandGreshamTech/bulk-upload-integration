package com.gresham.bulk.upload;

import lombok.Data;

@Data
public class GraphQLQuery {

    private String query;
    private Object variables;
}