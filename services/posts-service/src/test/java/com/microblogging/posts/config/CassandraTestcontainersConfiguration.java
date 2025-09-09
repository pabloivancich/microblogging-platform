package com.microblogging.posts.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.cassandra.CassandraContainer;

@TestConfiguration(proxyBeanMethods = false)
public class CassandraTestcontainersConfiguration {

    @Bean
    @ServiceConnection
    public CassandraContainer cassandraContainer() {
        return new CassandraContainer("cassandra:latest");
    }

}
