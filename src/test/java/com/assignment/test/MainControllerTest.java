package com.assignment.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.assignment.testTask.TestTaskApplication;
import com.assignment.testTask.user.User;
import com.assignment.testTask.user.UserHandler;
import com.assignment.testTask.user.UserValidator;

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.MOCK,
  classes = TestTaskApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MainControllerTest {
  
    @Autowired
    private MockMvc mvc;

    @Autowired
    UserHandler userHandler;

    @Autowired
    UserValidator validator;

    @BeforeEach
    public void setupUser() throws ParseException{
        userHandler.clear();

        LocalDate birth_date = LocalDate.of(1994, 04,14);
        userHandler.add(new User
        (
            birth_date, 
            "default@example.com", 
            "John", 
            "Doe", 
            "Bulvard 59c, Munich", 
            "0960530513"
        ));
    }

    @Test
    void postUserTest() throws Exception{
        final String userStringified = "{\"email\":\"maralea@gmail.com\",\"f_name\":\"Alea\",\"l_name\":\"Monar\",\"birth_date\":\"1989-06-17\"}";
        mvc.perform
        (
            MockMvcRequestBuilders.post("/api/postUser")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userStringified)
        )
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value("maralea@gmail.com"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.f_name").value("Alea"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.l_name").value("Monar"));
    }

    @Test
    void getUserTest() throws Exception{
        final String expectedGetResponse = "{\"data\":{\"id\":0,\"birth_date\":\"1994-04-14\",\"email\":\"default@example.com\",\"f_name\":\"John\",\"l_name\":\"Doe\",\"address\":\"Bulvard 59c, Munich\",\"phone\":\"0960530513\"}}";
        mvc.perform
        (
            MockMvcRequestBuilders.get("/api/getUsers/0")
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(expectedGetResponse));
    }

    @Test
    void getAllUsersTest() throws Exception{
        
        LocalDate birth_date = LocalDate.of(2004,9,12);

        User user = new User
        (
            birth_date, 
            "IvanSchmidt@example.com", 
            "Ivan", 
            "Schmidt", 
            "Hofstrasse 4, Hilden", 
            "4915510686794"
        );
        userHandler.add(user);

        mvc.perform
        (
            MockMvcRequestBuilders.get("/api/getUsers")
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].email").value("default@example.com"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].f_name").value("John"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].l_name").value("Doe"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].phone").value("0960530513"))

        .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].email").value("IvanSchmidt@example.com"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].f_name").value("Ivan"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].l_name").value("Schmidt"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].phone").value("4915510686794"));
   }

   @Test
    void getAllUsersByDateTest() throws Exception{
        
        LocalDate birth_date = LocalDate.of(2003,3,1);
        
        User user = new User
        (
            birth_date, 
            "IvanSchmidt@example.com", 
            "Ivan", 
            "Schmidt", 
            "Hofstrasse 4, Hilden", 
            "4915510686794"
        );
        userHandler.getAllUsers();

        userHandler.add(user);

        mvc.perform
        (
            MockMvcRequestBuilders.get("/api/getUsers?from=2002-01-01&to=2024-01-01")
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].email").value("default@example.com"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].f_name").value("John"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].l_name").value("Doe"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].phone").value("0960530513"));
   }

   @Test
    void deleteUserTest() throws Exception{
        
        LocalDate birth_date = LocalDate.of(2004, 9,12);
        User user = new User
        (
            birth_date, 
            "IgorFalaev@example.com", 
            "Igor", 
            "Falaev", 
            "Wolfgang Str. 35c, New York", 
            "1-212-456-7890"
        );
        int id = userHandler.add(user).getId();

        String url = String.format("/api/deleteUser/%d", id);

        mvc.perform
        (
            MockMvcRequestBuilders.delete(url)
        )
        .andExpect(MockMvcResultMatchers.status().isOk());

        assertThat(userHandler.getUser(user)).isNull();
   }

   @Test
    void putUserTest() throws Exception{
        
        LocalDate birth_date = LocalDate.of(2005, 1,26);
        User user = new User
        (
            birth_date, 
            "manuel-rodrigues@example.com", 
            "Manuel", 
            "Rodrigues", 
            "Prolongación Pilar Zapata s/n., La Línea de la Concepción, Leo 44796", 
            "746 189 715"
        );
        int id = userHandler.add(user).getId();

        String url = String.format("/api/putUser/%d", id);
        final String userStringified = "{\"email\":\"manuel-rodriguezz@hotmail.com\",\"f_name\":\"Manuel\",\"l_name\":\"Rodrigues\"}";
        final String userStringifiedAllFields = "{\"email\":\"the-manuel-rodrigue@hotmail.com\",\"f_name\":\"Manel\",\"l_name\":\"Rodriguez\",\"birth_date\":\"2003-04-24\",\"address\":\"Prolongación Pilar Zapata, La Línea de la Concepción\",\"phone\":\"746 110 743\"}";

        mvc.perform
        (
            MockMvcRequestBuilders.put(url) 
            .contentType(MediaType.APPLICATION_JSON)
            .content(userStringified)
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value("manuel-rodriguezz@hotmail.com"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.f_name").value("Manuel"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.l_name").value("Rodrigues"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.address").value("Prolongación Pilar Zapata s/n., La Línea de la Concepción, Leo 44796"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.phone").value("746 189 715"));

        mvc.perform
        (
            MockMvcRequestBuilders.put(url) 
            .contentType(MediaType.APPLICATION_JSON)
            .content(userStringifiedAllFields)
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value("the-manuel-rodrigue@hotmail.com"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.f_name").value("Manel"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.l_name").value("Rodriguez"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.address").value("Prolongación Pilar Zapata, La Línea de la Concepción"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.phone").value("746 110 743"));
   }
}
