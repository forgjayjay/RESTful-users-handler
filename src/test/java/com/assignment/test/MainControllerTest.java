package com.assignment.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.assignment.testTask.TestTaskApplication;
import com.assignment.testTask.user.UserHandler;

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.MOCK,
  classes = TestTaskApplication.class)
@AutoConfigureMockMvc
public class MainControllerTest {
  
    @Autowired
    private MockMvc mvc;

    @Autowired
    UserHandler userHandler;

    @Test
    void hello() throws Exception{
    
        final String userStringified = "{\"email\":\"mymail@mail.com\",\"f_name\":\"hi\",\"l_name\":\"me\",\"birth_date\":\"1995-04-24\",\"address\":\"aeagea\",\"phone\":\"2145136165\"}";
        final String expectedPostResponse = "{\"data\":{\"id\":0,\"birth_date\":\"1995-04-24\",\"email\":\"mymail@mail.com\",\"f_name\":\"hi\",\"l_name\":\"me\",\"address\":\"aeagea\",\"phone\":2145136165}}";
        mvc.perform(
            MockMvcRequestBuilders.post("http://localhost:8080/api/postUser")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userStringified))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.content().json(expectedPostResponse));

        // mvc.perform(
        //     MockMvcRequestBuilders.get("api/getUsers")
        //     .contentType(MediaType.APPLICATION_JSON))
        //     .andExpect(MockMvcResultMatchers.status().isOk())
        //     .andExpect(MockMvcResultMatchers.content()
        //     .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        //     .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value(""));
    
    }

}
