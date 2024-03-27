package org.fmasciari.graphrestapi.service;


import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GraphPathRestService {

    String shortestPath(String src, String dst);

    String allPaths(String node);

    String allPathsBetween2Res(String src, String dst);

    String shortestDistance(String src, String dst);

    String cyclicPath(String node);

    String bidirectionalSearch(String src, String dst);
}

