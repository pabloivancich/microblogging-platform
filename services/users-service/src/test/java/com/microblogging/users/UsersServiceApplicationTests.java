package com.microblogging.users;

import com.microblogging.users.config.PostgreSQLTestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Import({PostgreSQLTestcontainersConfiguration.class})
@SpringBootTest
class UsersServiceApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
        assertNotNull(applicationContext);
	}

}
