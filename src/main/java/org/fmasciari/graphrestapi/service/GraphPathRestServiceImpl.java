package org.fmasciari.graphrestapi.service;

import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.query.resultio.TupleQueryResultWriter;
import org.eclipse.rdf4j.query.resultio.sparqljson.SPARQLResultsJSONWriter;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.fmasciari.graphrestapi.repository.GraphPathRestRepository;
import org.fmasciari.graphrestapi.utils.AppConfig;
import org.fmasciari.graphrestapi.utils.QueryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class GraphPathRestServiceImpl implements GraphPathRestService {

   private static Logger logger =  LoggerFactory.getLogger(GraphPathRestServiceImpl.class);

    private static String strInsert;
    private static String strInsert2;
    private static String strQuery;
    private static String strQueryAll;

    @Autowired
    private GraphPathRestRepository graphDbRepo;

    @Autowired
    private AppConfig config;



    private String querySJson(RepositoryConnection repositoryConnection, String query) {

        TupleQuery tupleQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, query);

        ByteArrayOutputStream writer = new ByteArrayOutputStream();

        TupleQueryResultWriter tupleHandler = new SPARQLResultsJSONWriter(writer);

        try {
            tupleQuery.evaluate(tupleHandler);
            return new String(writer.toByteArray(), "UTF-8");
        }
        catch (Exception qee) {
            logger.error(qee.getStackTrace().toString(), qee);
            qee.printStackTrace();
        } finally {
            //result.close();
        }

        return "NOT FOUND";
    }

    private String querySJson(String query) {

        RepositoryConnection repositoryConnection = null;

        try {

            repositoryConnection = graphDbRepo.getRepositoryConnection();

            query  = query.replace("$othersPrefixes$",config.getSpecificQueryPrefix()+"\n").replace("$prefixRES$",config.getSpecificQueryResourcePrefix());

            TupleQuery tupleQuery = repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL,query);

            ByteArrayOutputStream writer = new ByteArrayOutputStream();

            TupleQueryResultWriter tupleHandler = new SPARQLResultsJSONWriter(writer);

            tupleQuery.evaluate(tupleHandler);

            return new String(writer.toByteArray(), "UTF-8");

        } catch (Exception qee) {
            logger.error(qee.getStackTrace().toString(), qee);
            qee.printStackTrace();
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            t.printStackTrace();
        } finally {
            if(repositoryConnection!= null)
            repositoryConnection.close();
        }

        return "NOT FOUND";
    }




    @Override
    public String shortestPath(String src, String dst) {

        String queryFinal = QueryUtils.queryShortestPath.replace("$sourceNodeParam$",src).replace("$destNodeParam$",dst);

        return  querySJson(queryFinal);

    }

    @Override
    public String allPaths(String node) {

        String queryFinal = QueryUtils.queryAllPaths.replace("$destNodeParam$",node);

        return  querySJson(queryFinal);

    }

    @Override
    public String allPathsBetween2Res(String src, String dst) {

        String queryFinal = QueryUtils.queryAllPathsBetween2Res.replace("$sourceNodeParam$",src).replace("$destNodeParam$",dst);

        return  querySJson(queryFinal);
    }

    @Override
    public String shortestDistance(String src, String dst) {
        String queryFinal = QueryUtils.queryShortestDistance.replace("$sourceNodeParam$",src).replace("$destNodeParam$",dst);

        return  querySJson(queryFinal);
    }

    @Override
    public String cyclicPath(String node) {

        String queryFinal = QueryUtils.queryCyclicPath.replace("$sourceNodeParam$",node);

        return  querySJson(queryFinal);

    }

    @Override
    public String bidirectionalSearch(String src, String dst) {

        String queryFinal = QueryUtils.queryBidirectionalSearch.replace("$sourceNodeParam$",src).replace("$destNodeParam$",dst);

        return querySJson(queryFinal);

    }


}
