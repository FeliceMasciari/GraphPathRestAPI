package org.fmasciari.graphrestapi.service;

import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.query.resultio.TupleQueryResultWriter;
import org.eclipse.rdf4j.query.resultio.sparqljson.SPARQLResultsJSONWriter;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.fmasciari.graphrestapi.repository.GraphPathRestRepository;
import org.fmasciari.graphrestapi.utils.AppConfig;
import org.fmasciari.graphrestapi.utils.QueryUtils;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class GraphPathRestServiceImpl implements GraphPathRestService {

   /* private static Logger logger =
            LoggerFactory.getLogger(UNtServiceImpl.class);*/
    // Why This Failure marker
    private static final Marker WTF_MARKER =
            MarkerFactory.getMarker("WTF");

    // GraphDB
    private static final String GRAPHDB_SERVER =
            "http://localhost:7200/";
    private static final String REPOSITORY_ID = "PersonData";

    private static String strInsert;
    private static String strInsert2;
    private static String strQuery;
    private static String strQueryAll;

    @Autowired
    private GraphPathRestRepository graphDbRepo;

    @Autowired
    private AppConfig config;

    static {
        strInsert2 =
                "INSERT DATA {"
                        + "<http://dbpedia.org/resource/Grace_Hopper> <http://dbpedia.org/ontology/birthDate> \"1906-12-09\"^^<http://www.w3.org/2001/XMLSchema#date> ."
                        + "<http://dbpedia.org/resource/Grace_Hopper> <http://dbpedia.org/ontology/birthPlace> <http://dbpedia.org/resource/New_York_City> ."
                        + "<http://dbpedia.org/resource/Grace_Hopper> <http://dbpedia.org/ontology/deathDate> \"1992-01-01\"^^<http://www.w3.org/2001/XMLSchema#date> ."
                        + "<http://dbpedia.org/resource/Grace_Hopper> <http://dbpedia.org/ontology/deathPlace> <http://dbpedia.org/resource/Arlington_County,_Virginia> ."
                        + "<http://dbpedia.org/resource/Grace_Hopper> <http://purl.org/dc/terms/description> \"American computer scientist and United States Navy officer.\" ."
                        + "<http://dbpedia.org/resource/Grace_Hopper> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/Person> ."
                        + "<http://dbpedia.org/resource/Grace_Hopper> <http://xmlns.com/foaf/0.1/gender> \"female\" ."
                        + "<http://dbpedia.org/resource/Grace_Hopper> <http://xmlns.com/foaf/0.1/givenName> \"Grace\" ."
                        + "<http://dbpedia.org/resource/Grace_Hopper> <http://xmlns.com/foaf/0.1/name> \"Grace Hopper\" ."
                        + "<http://dbpedia.org/resource/Grace_Hopper> <http://xmlns.com/foaf/0.1/surname> \"Hopper\" ."
                        + "}";

        strInsert =
                "INSERT DATA {"
                        + "<http://dbpedia.org/resource/Grace_Hopper> <http://dbpedia.org/ontology/birthDate> \"2020-12-09\"^^<http://www.w3.org/2001/XMLSchema#date> ."
                        + "<http://dbpedia.org/resource/Grace_Hopper> <http://dbpedia.org/ontology/birthPlace> <http://dbpedia.org/resource/Rome> ."
                        + "<http://dbpedia.org/resource/Grace_Hopper> <http://dbpedia.org/ontology/deathDate> \"1998-01-01\"^^<http://www.w3.org/2001/XMLSchema#date> ."
                        + "<http://dbpedia.org/resource/Grace_Hopper> <http://dbpedia.org/ontology/deathPlace> <http://dbpedia.org/resource/Alabama_County,_WDC> ."
                        + "<http://dbpedia.org/resource/Grace_Hopper> <http://purl.org/dc/terms/description> \"American Test.\" ."
                        + "<http://dbpedia.org/resource/Grace_Hopper> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/Person> ."
                        + "<http://dbpedia.org/resource/Grace_Hopper> <http://xmlns.com/foaf/0.1/gender> \"male\" ."
                        + "<http://dbpedia.org/resource/Grace_Hopper> <http://xmlns.com/foaf/0.1/givenName> \"Mario\" ."
                        + "<http://dbpedia.org/resource/Grace_Hopper> <http://xmlns.com/foaf/0.1/name> \"Mario Rossi\" ."
                        + "<http://dbpedia.org/resource/Grace_Hopper> <http://xmlns.com/foaf/0.1/surname> \"Rossi\" ."
                        + "}";

        strQuery =
                "SELECT ?name FROM DEFAULT WHERE {" +
                        "?s <http://xmlns.com/foaf/0.1/name> ?name .}";

        strQueryAll =
                "SELECT ?subject ?predicate ?object\n" +
                        "WHERE {?subject ?predicate ?object} \n" +
                        "LIMIT 100";

        /*
        PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX dbpedia-owl: <http://dbpedia.org/ontology/>

PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
SELECT *
WHERE { ?lang1 rdf:type dbpedia-owl:Person . }
         */
    }

    private static RepositoryConnection getRepositoryConnection(AppConfig config) {
        //Repository repository = new HTTPRepository(GRAPHDB_SERVER, REPOSITORY_ID);
        Repository repository = new HTTPRepository(config.getRepositoryName()+":"+config.getRepositoryServerPort(), config.getRepositoryName());

        repository.initialize();
        RepositoryConnection repositoryConnection =
                repository.getConnection();

        //CharSink test = new CharSink(
        return repositoryConnection;
    }

    private static void insert(
            RepositoryConnection repositoryConnection) {

        repositoryConnection.begin();
        Update updateOperation = repositoryConnection
                .prepareUpdate(QueryLanguage.SPARQL, strInsert);
        updateOperation.execute();

        try {
            repositoryConnection.commit();
        } catch (Exception e) {
            if (repositoryConnection.isActive())
                repositoryConnection.rollback();
        }
    }

    private static void query(
            RepositoryConnection repositoryConnection) {

        TupleQuery tupleQuery = repositoryConnection
                .prepareTupleQuery(QueryLanguage.SPARQL, strQuery);
        TupleQueryResult result = null;
        try {
            result = tupleQuery.evaluate();
            while (result.hasNext()) {
                BindingSet bindingSet = result.next();

                SimpleLiteral name =
                        (SimpleLiteral)bindingSet.getValue("deathPlace");
                //logger.trace("deathPlace = " + name.stringValue());
                System.out.println("deathPlace = " + name.stringValue());
            }
        }
        catch (QueryEvaluationException qee) {
            //logger.error(WTF_MARKER,                    qee.getStackTrace().toString(), qee);
            qee.printStackTrace();
        } finally {
            result.close();
        }
    }

    private static String queryS(
            RepositoryConnection repositoryConnection) {

        TupleQuery tupleQuery = repositoryConnection
                .prepareTupleQuery(QueryLanguage.SPARQL, strQuery);
        TupleQueryResult result = null;
        try {
            result = tupleQuery.evaluate();
            while (result.hasNext()) {
                BindingSet bindingSet = result.next();

                SimpleLiteral name =
                        (SimpleLiteral)bindingSet.getValue("name");
               // logger.trace("deathPlace = " + name.stringValue());
                System.out.println("name = " + name.stringValue());

                return name.stringValue();
            }
        }
        catch (QueryEvaluationException qee) {
            //logger.error(WTF_MARKER,                    qee.getStackTrace().toString(), qee);
            qee.printStackTrace();
        } finally {
            result.close();
        }

        return "nada";
    }

    private static String querySJson(
            RepositoryConnection repositoryConnection) {

        TupleQuery tupleQuery = repositoryConnection
                .prepareTupleQuery(QueryLanguage.SPARQL, strQuery);
        TupleQueryResult result = null;
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        //File file = new File(filePath);
        //createNewFile(file);
        //FileOutputStream os = new FileOutputStream(file);
        TupleQueryResultWriter tupleHandler = new SPARQLResultsJSONWriter(writer);
        try {
            tupleQuery.evaluate(tupleHandler);
            return new String(writer.toByteArray(), "UTF-8");
        }
        catch (Exception qee) {
            //logger.error(WTF_MARKER,                    qee.getStackTrace().toString(), qee);
            qee.printStackTrace();
        } finally {
            //result.close();
        }

        return "nada";
    }


    private static String querySJson(RepositoryConnection repositoryConnection, String query) {

        TupleQuery tupleQuery = repositoryConnection
                .prepareTupleQuery(QueryLanguage.SPARQL, query);
        TupleQueryResult result = null;
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        //File file = new File(filePath);
        //createNewFile(file);
        //FileOutputStream os = new FileOutputStream(file);
        TupleQueryResultWriter tupleHandler = new SPARQLResultsJSONWriter(writer);
        try {
            tupleQuery.evaluate(tupleHandler);
            return new String(writer.toByteArray(), "UTF-8");
        }
        catch (Exception qee) {
            //logger.error(WTF_MARKER,                    qee.getStackTrace().toString(), qee);
            qee.printStackTrace();
        } finally {
            //result.close();
        }

        return "nada";
    }

    private static String querySJsonAll(
            RepositoryConnection repositoryConnection) {

        TupleQuery tupleQuery = repositoryConnection
                .prepareTupleQuery(QueryLanguage.SPARQL, strQueryAll);
        TupleQueryResult result = null;
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        //File file = new File(filePath);
        //createNewFile(file);
        //FileOutputStream os = new FileOutputStream(file);
        TupleQueryResultWriter tupleHandler = new SPARQLResultsJSONWriter(writer);
        try {
            tupleQuery.evaluate(tupleHandler);
            return new String(writer.toByteArray(), "UTF-8");
        }
        catch (Exception qee) {
            //logger.error(WTF_MARKER,                    qee.getStackTrace().toString(), qee);
            qee.printStackTrace();
        } finally {
            //result.close();
        }

        return "nada";
    }

    @Override
    public String getName() {
        RepositoryConnection repositoryConnection = null;
        try {
            repositoryConnection = graphDbRepo.getRepositoryConnection();// getRepositoryConnection(config);

            //insert(repositoryConnection);
            return querySJson(repositoryConnection);

        } catch (Throwable t) {
            //logger.error(WTF_MARKER, t.getMessage(), t);
            t.printStackTrace();
        } finally {
            repositoryConnection.close();
        }

        return "nope";
    }

    @Override
    public List<Object> getTuple() {
        return null;
    }

    @Override
    public String shortestPath(String src, String dst) {

        RepositoryConnection repositoryConnection = null;
        try {
            repositoryConnection = graphDbRepo.getRepositoryConnection();// getRepositoryConnection(config);

            String queryFinal = QueryUtils.queryShortestPathO.replace("sourceNodeParam",src).replace("destNodeParam",dst);//$sourceNodeParam dbr:$destNodeParam
            //insert(repositoryConnection);
            return  querySJson(repositoryConnection, queryFinal);//querySZZ(repositoryConnection,src,dst);// querySJson(repositoryConnection, queryFinal);

        } catch (Throwable t) {
            //logger.error(WTF_MARKER, t.getMessage(), t);
            t.printStackTrace();
        } finally {
            repositoryConnection.close();
        }

        return "nope";

    }

    @Override
    public String allPaths(String node) {


        RepositoryConnection repositoryConnection = null;
        try {
            repositoryConnection =graphDbRepo.getRepositoryConnection();// getRepositoryConnection(config);

            String queryFinal = QueryUtils.queryAllPaths.replace("destNodeParam",node);
            //insert(repositoryConnection);
            return  querySJson(repositoryConnection, queryFinal);

        } catch (Throwable t) {
            //logger.error(WTF_MARKER, t.getMessage(), t);
            t.printStackTrace();
        } finally {
            repositoryConnection.close();
        }

        return "nope";
    }

    @Override
    public String shortestDistance() {
        return null;
    }

    @Override
    public String cyclicPath(String node) {


        RepositoryConnection repositoryConnection = null;
        try {
            repositoryConnection = graphDbRepo.getRepositoryConnection();// getRepositoryConnection(config);

            String queryFinal = QueryUtils.queryCyclicPath.replace("destNodeParam",node);
            //insert(repositoryConnection);
            return  querySJson(repositoryConnection, queryFinal);

        } catch (Throwable t) {
            //logger.error(WTF_MARKER, t.getMessage(), t);
            t.printStackTrace();
        } finally {
            repositoryConnection.close();
        }

        return "nope";

    }

    @Override
    public String bidirectionalSearch(String src, String dst) {


        RepositoryConnection repositoryConnection = null;
        try {
            repositoryConnection = graphDbRepo.getRepositoryConnection();// getRepositoryConnection(config);

            String queryFinal = QueryUtils.queryBidirectionalSearch.replace("sourceNodeParam", src).replace("destNodeParam", dst);
            //insert(repositoryConnection);
            return querySJson(repositoryConnection, queryFinal);

        } catch (Throwable t) {
            //logger.error(WTF_MARKER, t.getMessage(), t);
            t.printStackTrace();
        } finally {
            repositoryConnection.close();
        }

        return "nope";
    }

    @Override
    public String getAll() {
        RepositoryConnection repositoryConnection = null;
        try {
            repositoryConnection = graphDbRepo.getRepositoryConnection();// getRepositoryConnection(config);

            //insert(repositoryConnection);
            return querySJsonAll(repositoryConnection);

        } catch (Throwable t) {
            //logger.error(WTF_MARKER, t.getMessage(), t);
            t.printStackTrace();
        } finally {
            repositoryConnection.close();
        }

        return "nope";
    }
/*
    public static void main(String[] args) {
        RepositoryConnection repositoryConnection = null;
        try {
            repositoryConnection = getRepositoryConnection();

            insert(repositoryConnection);
            query(repositoryConnection);

        } catch (Throwable t) {
            logger.error(WTF_MARKER, t.getMessage(), t);
            t.printStackTrace();
        } finally {
            repositoryConnection.close();
        }
    }

 */

    private static String querySZZ(
            RepositoryConnection repositoryConnection, String src, String dst) {

        TupleQuery tupleQuery = repositoryConnection
                .prepareTupleQuery(QueryLanguage.SPARQL,  QueryUtils.queryShortestPathNS.replace("sourceNodeParam",src).replace("destNodeParam",dst));
        TupleQueryResult result = null;
        try {
            result = tupleQuery.evaluate();
            /*while (result.hasNext()) {
                BindingSet bindingSet = result.next();

                SimpleLiteral name =
                        (SimpleLiteral)bindingSet.getValue("name");
                // logger.trace("deathPlace = " + name.stringValue());
                System.out.println("name = " + name.stringValue());

                return name.stringValue();
            }*/

            return result.toString();
        }
        catch (QueryEvaluationException qee) {
            //logger.error(WTF_MARKER,                    qee.getStackTrace().toString(), qee);
            qee.printStackTrace();
        } finally {
            result.close();
        }

        return "nada";
    }
}
