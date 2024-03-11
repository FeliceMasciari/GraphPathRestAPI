package org.fmasciari.graphrestapi.utils;

public class QueryUtils {
    private static String queryPrefixes =   "PREFIX path: <http://www.ontotext.com/path#>\n" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX dbr: <http://dbpedia.org/resource/>\n" +
            "PREFIX : <http://lexica/mylexicon#>";
    private static String prefixSRC ="";
    private static String prefixDST ="";

    public static String queryShortestPathO = queryPrefixes + "\n\n"+
            "SELECT ?start ?end ?index ?path\n" +
            "WHERE {\n" +
            "    VALUES (?src ?dst) {\n" +
            "        ( "+prefixSRC+":sourceNodeParam "+prefixDST+":destNodeParam )\n" +
            "    }\n" +
            "    SERVICE <http://www.ontotext.com/path#search> {\n" +
            "        <urn:path> path:findPath path:shortestPath ;\n" +
            "                   path:sourceNode ?src ;\n" +
            "                   path:destinationNode ?dst ;\n" +
            "                   path:pathIndex ?path ;\n" +
            "                   path:startNode ?start;\n" +
            "                   path:endNode ?end;\n" +
            "                   path:resultBindingIndex ?index .\n" +
            "        SERVICE <urn:path> {\n" +
            "            ?start !dbp:keyPerson ?end\n" +
            "        }\n" +
            "    }\n" +
            "}";

    public static String queryAllPaths = queryPrefixes + "\n\n"+
            "SELECT ?edge ?index ?path\n" +
            "WHERE {\n" +
            "    VALUES (?dst) {\n" +
            "        ( :destNodeParam )\n" +
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

    public static String queryShortestDistance = queryPrefixes + "\n\n"+
            "SELECT ?dist\n" +
            "WHERE {\n" +
            "    VALUES (?src ?dst) {\n" +
            "        ( dbr:Marvel_Studios \"1973-06-02\"^^xsd:date )\n" +
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
            "        (dbr:destNodeParam)\n" +
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
            "        ( dbr:sourceNodeParam dbr:destNodeParam )\n" +
            "    }\n" +
            "    SERVICE path:search {\n" +
            "        <urn:path> path:findPath path:allPaths ;\n" +
            "                   path:sourceNode ?src ;\n" +
            "                   path:destinationNode ?dst ;\n" +
            "                   path:resultBinding ?edge ;\n" +
            "                   path:pathIndex ?path ;\n" +
            "                   path:maxPathLength 4 ;\n" +
            "                   path:bidirectional true ;\n" +
            "                   path:resultBindingIndex ?index ;\n" +
            "    }\n" +
            "}";


    public static String queryShortestPath = queryPrefixes + "\n\n"+
            "SELECT ?pathIndex ?edgeIndex ?edge\n" +
            "WHERE {\n" +
            "    VALUES (?src ?dst) {\n" +
            "        ( dbr:sourceNodeParam dbr:destNodeParam )\n" +
            "    }\n" +
            "    SERVICE path:search {\n" +
            "        [] path:findPath path:shortestPath ;\n" +
            "           path:sourceNode ?src ;\n" +
            "           path:destinationNode ?dst ;\n" +
            "           path:pathIndex ?pathIndex ;\n" +
            "           path:resultBindingIndex ?edgeIndex ;\n" +
            "           path:resultBinding ?edge ;\n" +
            "           .\n" +
            "    }\n" +
            "}";

    public static String queryShortestPathNS =
            "PREFIX path: <http://www.ontotext.com/path#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX dbr: <http://dbpedia.org/resource/>\n" +
                    "\n" +
                    "SELECT ?pathIndex ?edgeIndex ?edge\n" +
                    "WHERE {\n" +
                    "    VALUES (?src ?dst) {\n" +
                    "        ( dbr:sourceNodeParam dbr:destNodeParam )\n" +
                    "    }\n" +
                    "    SERVICE path:search {\n" +
                    "        [] path:findPath path:shortestPath ;\n" +
                    "           path:sourceNode ?src ;\n" +
                    "           path:destinationNode ?dst ;\n" +
                    "           path:pathIndex ?pathIndex ;\n" +
                    "           path:resultBindingIndex ?edgeIndex ;\n" +
                    "           path:resultBinding ?edge ;\n" +
                    "           .\n" +
                    "    }\n" +
                    "}";
}
