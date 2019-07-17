package ru.fmtk.khlystov.booksaccounting.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
@ConfigurationProperties("spring.data.mongodb")
public class MongoProps {
    private int port;
    private String database;
    private String host;
    private String username;

    public MongoProps() {
        this(27017, "library-accounting", "localhost", null);
    }

    public MongoProps(int port, String database, String host, String username) {
        this.port = port;
        this.database = database;
        this.host = host;
        this.username = username;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getHost() {
        return host;
    }

    public String getUsername() {
        return username;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        URI uri = null;
        try {
            uri = new URI("mongodb", username, host, port, "/" + database, null, null);
        } catch (URISyntaxException e) {
            return "mongodb://localhost:27017/library-accounting";
        }
        return uri.toString();
    }
}
