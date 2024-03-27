package org.fmasciari.graphrestapi.utils;

public class QueryUtils {
    private static String queryPrefixes =   "PREFIX path: <http://www.ontotext.com/path#>\n" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX dbr: <http://dbpedia.org/resource/>\n" +
            "$othersPrefixes$";

    public static String queryShortestPath = queryPrefixes + "\n\n"+
            "SELECT ?start ?end ?index ?path\n" +
            "WHERE {\n" +
            "    VALUES (?src ?dst) {\n" +
            "        ( $prefixRES$$sourceNodeParam$ $prefixRES$$destNodeParam$ )\n" +
            "    }\n" +
            "    SERVICE <http://www.ontotext.com/path#search> {\n" +
            "        <urn:path> path:findPath path:shortestPath ;\n" +
            "                   path:sourceNode ?src ;\n" +
            "                   path:destinationNode ?dst ;\n" +
            "                   path:pathIndex ?path ;\n" +
            "                   path:startNode ?start;\n" +
            "                   path:endNode ?end;\n" +
            "                   path:resultBindingIndex ?index .\n" +
            "    }\n" +
            "}";

    public static String queryAllPaths = queryPrefixes + "\n\n"+
            "SELECT ?edge ?index ?path\n" +
            "WHERE {\n" +
            "    VALUES (?dst) {\n" +
            "        ( $prefixRES$$destNodeParam$ )\n" +
            "    }\n" +
            "    SERVICE <http://www.ontotext.com/path#search> {\n" +
            "        <urn:path> path:findPath path:allPaths ;\n" +
            "                   path:sourceNode ?src ;\n" +
            "                   path:destinationNode ?dst ;\n" +
            "                   path:pathIndex ?path ;\n" +
            "                   path:minPathLength 5 ;\n" +
            "                   path:resultBinding ?edge ;\n" +
            "                   path:resultBindingIndex ?index .\n" +
            "   }\n" +
            "}";

    public static String queryAllPathsBetween2Res = queryPrefixes + "\n\n"+
            "SELECT ?edge ?index ?path\n" +
            "WHERE {\n" +
            "    VALUES (?src ?dst) {\n" +
            "        ( $prefixRES$$sourceNodeParam$ $prefixRES$$destNodeParam$ )\n" +
            "    }\n" +
            "    SERVICE <http://www.ontotext.com/path#search> {\n" +
            "        <urn:path> path:findPath path:allPaths ;\n" +
            "                   path:sourceNode ?src ;\n" +
            "                   path:destinationNode ?dst ;\n" +
            "                   path:pathIndex ?path ;\n" +
            "                   path:minPathLength 3 ;\n" +
            "                   path:resultBinding ?edge ;\n" +
            "                   path:resultBindingIndex ?index .\n" +
            "   }\n" +
            "}";

    public static String queryShortestDistance = queryPrefixes + "\n\n"+
            "SELECT ?dist\n" +
            "WHERE {\n" +
            "    VALUES (?src ?dst) {\n" +
            "        ( $prefixRES$$sourceNodeParam$ $prefixRES$$destNodeParam$ )\n" +
            "    }\n" +
            "    SERVICE <http://www.ontotext.com/path#search> {\n" +
            "        <urn:path> path:findPath path:distance ;\n" +
            "                   path:sourceNode ?src ;\n" +
            "                   path:destinationNode ?dst ;\n" +
            "                   path:distanceBinding ?dist;\n" +
            "    }\n" +
            "}";

    public static String queryCyclicPath = queryPrefixes + "\n\n"+
            "SELECT ?edge ?index ?path\n" +
            "WHERE {\n" +
            "    VALUES (?src) {\n" +
            "        ($prefixRES$$sourceNodeParam$)\n" +
            "    }\n" +
            "    SERVICE <http://www.ontotext.com/path#search> {\n" +
            "        <urn:path> path:findPath path:cycle ;\n" +
            "                   path:sourceNode ?src ;\n" +
            "                   path:resultBinding ?edge ;\n" +
            "                   path:pathIndex ?path ;\n" +
            "                   path:resultBindingIndex ?index .\n" +
            "    }\n" +
            "}";

    public static String queryBidirectionalSearch = queryPrefixes + "\n\n"+
            "SELECT ?edge ?index ?path\n" +
            "WHERE {\n" +
            "    VALUES (?src ?dst) {\n" +
            "        ( $prefixRES$$sourceNodeParam$ $prefixRES$$destNodeParam$ )\n" +
            "    }\n" +
            "    SERVICE path:search {\n" +
            "        <urn:path> path:findPath path:allPaths ;\n" +
            "                   path:sourceNode ?src ;\n" +
            "                   path:destinationNode ?dst ;\n" +
            "                   path:resultBinding ?edge ;\n" +
            "                   path:pathIndex ?path ;\n" +
            "                   path:maxPathLength 2 ;\n" +
            "                   path:bidirectional true ;\n" +
            "                   path:resultBindingIndex ?index ;\n" +
            "    }\n" +
            "}";

}
