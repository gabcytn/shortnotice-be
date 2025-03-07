package com.gabcytn.spring_messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabcytn.spring_messaging.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WebLayerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void mustAuthenticateFirst () throws Exception{
        this.mockMvc.perform(get("/conversation/list")).andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void authStatusMustReturnFalseOnUnauthenticatedRequest () throws Exception
    {
        mockMvc.perform(get("/auth/status"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    public void login() throws Exception
    {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("pass1");

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());
    }
}
