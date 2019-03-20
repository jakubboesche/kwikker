package com.jb.kwikker.resources;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class KwikkerFollowRestControllerIT {
    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldFollow() throws Exception {
        mvc.perform(put("/follow/john/peter"))
                .andExpect(status().isOk())
                .andExpect(content().string("[\"peter\"]"));
    }

    @Test
    public void shouldUnfollowWhenNotFollowedBefore() throws Exception {
        mvc.perform(delete("/follow/john/peter"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void shouldUnfollowWhenFollowedBefore() throws Exception {
        mvc.perform(put("/follow/john/peter"));
        mvc.perform(put("/follow/john/michael"));

        mvc.perform(delete("/follow/john/peter"))
                .andExpect(status().isOk())
                .andExpect(content().string("[\"michael\"]"));
    }
}