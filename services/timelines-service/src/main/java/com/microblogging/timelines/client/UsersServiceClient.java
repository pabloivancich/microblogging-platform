package com.microblogging.timelines.client;

import com.microblogging.timelines.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class UsersServiceClient {

    private final WebClient webClient;

    @Autowired
    public UsersServiceClient(
            @Value("${services.users.base-url}") final String usersServiceBaseUrl,
            final WebClient.Builder webClientBuilder
    ) {
        this.webClient = webClientBuilder.baseUrl(usersServiceBaseUrl).build();
    }

    /**
     * Retrieves the list of followers for a given user.
     *
     * @param userId the user ID
     * @return list of User objects
     */
    public List<User> getFollowers(Long userId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/users/{id}/followers").build(userId))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(User.class) // Expecting a JSON array of Users
                .collectList()
                .block();
    }

}
