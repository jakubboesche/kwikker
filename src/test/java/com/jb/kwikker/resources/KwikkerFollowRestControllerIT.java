package com.jb.kwikker.resources;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KwikkerFollowRestControllerIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void init() {
        restTemplate.getRestTemplate().setInterceptors(
                singletonList((request, body, execution) -> {
                    request.getHeaders().set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
                    request.getHeaders().set("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
                    return execution.execute(request, body);
                })
        );
    }

    @Test
    public void shouldFollow() {
        restTemplate.put("/follow/john/peter", Void.class);

        assertThat(restTemplate.getForObject("/follow/john", String[].class))
                .contains("peter");
    }

    @Test
    public void shouldUnfollowWhenNotFollowedBefore() {
        restTemplate.delete("/follow/john/peter");

        assertThat(restTemplate.getForObject("/follow/john", String[].class))
                .isEmpty();
    }

    @Test
    public void shouldUnfollowWhenFollowedBefore() {
        restTemplate.put("/follow/john/peter", Void.class);
        restTemplate.put("/follow/john/michael", Void.class);

        restTemplate.delete("/follow/john/peter");

        assertThat(restTemplate.getForObject("/follow/john", String[].class))
                .contains("michael");
    }
}