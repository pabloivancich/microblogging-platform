package com.microblogging.posts.cassandra;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CassandraInitializer implements CommandLineRunner {

    @Autowired
    private final CqlTemplate cqlTemplate;

    @Autowired
    public CassandraInitializer(CqlTemplate cqlTemplate) {
        this.cqlTemplate = cqlTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        var resource = new ClassPathResource("schema.cql");
        try (var reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String content = reader.lines().collect(Collectors.joining("\n"));
            for (String statement : content.split(";")) {
                String trimmed = statement.trim();
                if (!trimmed.isEmpty()) {
                    cqlTemplate.execute(trimmed + ";");
                }
            }
            log.info("Cassandra schema initialized successfully.");
        }
    }

}
