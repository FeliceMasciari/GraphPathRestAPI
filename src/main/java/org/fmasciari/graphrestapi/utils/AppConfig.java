package org.fmasciari.graphrestapi.utils;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "configuration")
public class AppConfig {

    private  String repositoryServerHost;//=127.0.0.1
    private  String repositoryServerPort;//=7200
    private  String repositoryName;//=PersonData


    private String specificQueryPrefix;
    private String specificQueryResourcePrefix;


    public String getRepositoryServerHost() {
        return repositoryServerHost;
    }

    public void setRepositoryServerHost(String repositoryServerHost) {
        this.repositoryServerHost = repositoryServerHost;
    }

    public String getRepositoryServerPort() {
        return repositoryServerPort;
    }

    public void setRepositoryServerPort(String repositoryServerPort) {
        this.repositoryServerPort = repositoryServerPort;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getSpecificQueryPrefix() {
        return specificQueryPrefix;
    }

    public void setSpecificQueryPrefix(String specificQueryPrefix) {
        this.specificQueryPrefix = specificQueryPrefix;
    }

    public String getSpecificQueryResourcePrefix() {
        return specificQueryResourcePrefix;
    }

    public void setSpecificQueryResourcePrefix(String specificQueryResourcePrefix) {
        this.specificQueryResourcePrefix = specificQueryResourcePrefix;
    }
}
