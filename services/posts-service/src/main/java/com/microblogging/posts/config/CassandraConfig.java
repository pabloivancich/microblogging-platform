package com.microblogging.posts.config;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.cql.CqlTemplate;

import java.net.InetSocketAddress;

/**
 * Configuration for creating Cassandra beans using CqlSessionBuilder.
 * This class ensures that the session is built with properties from application.yml
 * and not with hardcoded values.
 */
@Configuration
public class CassandraConfig {

    @Value("${cassandra.local-datacenter}")
    private String cassandraLocalDatacenter;
    @Value("${cassandra.contact-points}")
    private String cassandraContactPoints;
    @Value("${cassandra.port}")
    private String cassandraPort;
    @Value("${cassandra.keyspace-name}")
    private String cassandraKeyspaceName;

    /**
     * Creates a CqlTemplate bean to execute CQL queries.
     */
    @Bean
    public CqlSession cqlSession() {
        try (CqlSession session = CqlSession.builder()
                .withKeyspace("system") // connect to system keyspace first
                .withLocalDatacenter(cassandraLocalDatacenter)
                .addContactPoint(new InetSocketAddress(cassandraContactPoints, Integer.parseInt(cassandraPort)))
                .build()) {

            // Create keyspace if missing
            session.execute("CREATE KEYSPACE IF NOT EXISTS " + cassandraKeyspaceName +
                    " WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}");
        }

        // Now connect to your app keyspace
        return CqlSession.builder()
                .addContactPoint(new InetSocketAddress(cassandraContactPoints, Integer.parseInt(cassandraPort)))
                .withLocalDatacenter(cassandraLocalDatacenter)
                .withKeyspace(CqlIdentifier.fromCql(cassandraKeyspaceName))
                .build();
    }

    @Bean
    public CqlTemplate cqlTemplate(CqlSession session) {
        return new CqlTemplate(session);
    }

}