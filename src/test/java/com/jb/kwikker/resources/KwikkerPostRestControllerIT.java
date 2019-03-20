package com.jb.kwikker.resources;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class KwikkerPostRestControllerIT {
    private static final String TEST_MESSAGE_SHORT = "test message (short)";
    public static final String TEST_MESSAGE_SHORT_2 = "test message (short) 2";
    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldUploadKwikkerPost() throws Exception {
        mvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"author\": \"john\", \"message\": \"test message (short)\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    public void shouldThrowBadRequestWhenTooLongMessage() throws Exception {
        mvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"author\": \"john\", \"message\": \"" +
                        "123456789012345678901234567890123456789012345678901234567890" +
                        "123456789012345678901234567890123456789012345678901234567890" +
                        "1234567890123456789012345678901234567890\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetWall() throws Exception {
        postMessage("john", TEST_MESSAGE_SHORT);
        postMessage("john", TEST_MESSAGE_SHORT_2);
        postMessage("peter", "other message");

        mvc.perform(get("/wall/john"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    private void postMessage(String author, String message) throws Exception {
        mvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"author\": \"" + author + "\", \"message\": \"" + message + "\"}"));
    }
}