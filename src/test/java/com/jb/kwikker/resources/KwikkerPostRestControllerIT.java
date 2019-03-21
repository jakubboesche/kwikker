package com.jb.kwikker.resources;


import com.jb.kwikker.api.KwikkerPostDto;
import com.jb.kwikker.repositories.KwikkerFollowRepository;
import com.jb.kwikker.repositories.KwikkerPostRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KwikkerPostRestControllerIT {
    private static final String JOHN_MESSAGE = "test message (short)";
    private static final String JOHN_MESSAGE_2 = "test message (short) 2";
    private static final String PETER_MESSAGE_3 = "other message 3";
    private static final String EVA_MESSAGE_4 = "other message 4";
    private static final String JOHN = "john";
    private static final String PETER = "peter";
    private static final String EVA = "eva";
    private static final String TOM = "tom";
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private KwikkerPostRepository kwikkerPostRepository;
    @Autowired
    private KwikkerFollowRepository kwikkerFollowRepository;

    @Before
    public void init() {
        kwikkerPostRepository.init();
        kwikkerFollowRepository.init();

        restTemplate.getRestTemplate().setInterceptors(
                singletonList((request, body, execution) -> {
                    request.getHeaders().set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
                    request.getHeaders().set("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
                    return execution.execute(request, body);
                })
        );
    }

    @Test
    public void shouldUploadKwikkerPost() {
        assertThat(
                restTemplate.postForLocation("/",
                        "{\"author\": \"john\", \"message\": \"test message (short)\"}")
                        .getPath()).isNotBlank();
    }

    @Test
    public void shouldThrowBadRequestWhenTooLongMessage() {
        assertThat(
                restTemplate.postForEntity("/",
                        "{\"author\": \"john\", \"message\": \"" +
                                "123456789012345678901234567890123456789012345678901234567890" +
                                "123456789012345678901234567890123456789012345678901234567890" +
                                "1234567890123456789012345678901234567890\"}", Void.class).getStatusCode()
        ).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldGetWall() throws Exception {
        postMessage(JOHN, JOHN_MESSAGE);
        Thread.sleep(20);
        postMessage(JOHN, JOHN_MESSAGE_2);
        postMessage(PETER, "other message");

        KwikkerPostDto[] posts = restTemplate.getForObject("/wall/john", KwikkerPostDto[].class);
        assertThat(posts)
                .extracting(KwikkerPostDto::getAuthor, KwikkerPostDto::getMessage)
                .containsExactly(tuple(JOHN, JOHN_MESSAGE_2),
                        tuple(JOHN, JOHN_MESSAGE));
    }

    @Test
    public void shouldGetTimeline() throws Exception {
        postMessage(JOHN, JOHN_MESSAGE);
        Thread.sleep(20);
        postMessage(JOHN, JOHN_MESSAGE_2);
        Thread.sleep(20);
        postMessage(PETER, PETER_MESSAGE_3);
        Thread.sleep(20);
        postMessage(EVA, EVA_MESSAGE_4);
        follow(TOM, JOHN);
        follow(TOM, EVA);

        KwikkerPostDto[] posts = restTemplate.getForObject("/timeline/tom", KwikkerPostDto[].class);
        assertThat(posts)
                .extracting(KwikkerPostDto::getAuthor, KwikkerPostDto::getMessage)
                .containsExactly(
                        tuple(EVA, EVA_MESSAGE_4),
                        tuple(JOHN, JOHN_MESSAGE_2),
                        tuple(JOHN, JOHN_MESSAGE)
                );
    }

    private void postMessage(String author, String message) {
        restTemplate.postForLocation("/",
                "{\"author\": \"" + author + "\", \"message\": \"" + message + "\"}");
    }

    private void follow(String user, String author) {
        restTemplate.put("/follow/" + user + "/" + author, Void.class);
    }
}