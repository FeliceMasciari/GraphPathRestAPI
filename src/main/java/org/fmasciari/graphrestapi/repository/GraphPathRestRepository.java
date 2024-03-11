package org.fmasciari.graphrestapi.repository;


import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.fmasciari.graphrestapi.utils.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GraphPathRestRepository {

    @Autowired
    private AppConfig config;

    public RepositoryConnection getRepositoryConnection() {

        Repository repository = new HTTPRepository(config.getRepositoryServerHost()+":"+config.getRepositoryServerPort()+"/", config.getRepositoryName());

        repository.initialize();
        RepositoryConnection repositoryConnection =
                repository.getConnection();

        return repositoryConnection;
    }
}
