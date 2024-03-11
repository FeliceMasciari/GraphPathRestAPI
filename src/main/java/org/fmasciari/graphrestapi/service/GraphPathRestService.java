package org.fmasciari.graphrestapi.service;


import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GraphPathRestService {

    String getName();

    List<Object> getTuple();

    String getAll();

    String shortestPath(String src, String dst);

    String allPaths(String node);

    String shortestDistance();

    String cyclicPath(String node);

    String bidirectionalSearch(String src, String dst);
}

