package com.microblogging.posts.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.testcontainers.cassandra.CassandraContainer;

import java.net.InetSocketAddress;

@TestConfiguration
public class CassandraTestcontainersConfiguration {

    @Bean
    @ServiceConnection
    public CassandraContainer cassandraContainer() {
        return new CassandraContainer("cassandra:latest");
    }

    @Bean
    public CqlSession cqlSession(CassandraContainer cassandraContainer) {
        CqlSession session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress(cassandraContainer.getHost(), cassandraContainer.getMappedPort(9042)))
                .withLocalDatacenter("datacenter1")
                .build();

        session.execute("CREATE KEYSPACE IF NOT EXISTS posts_keyspace " +
                "WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}");

        session.execute("CREATE TABLE IF NOT EXISTS posts_keyspace.posts (" +
                "user_id bigint," +
                "post_id uuid," +
                "content text," +
                "created_at timestamp," +
                "PRIMARY KEY (user_id, post_id)" +
                ")");

        return session;
    }

    @Bean
    public CqlTemplate cqlTemplate(CqlSession session) {
        return new CqlTemplate(session);
    }

}
