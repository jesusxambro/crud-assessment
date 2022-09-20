package com.crud.hillcountry.demo;


import com.crud.hillcountry.demo.dao.UserRepository;
import com.crud.hillcountry.demo.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository repository;

    User jesus;
    User marcus;
    List<User> pairProgramming;

    @BeforeEach
     void initDB(){
         jesus = new User();
        jesus.setEmail("ja@army.mil");
        jesus.setPassword("password");

         marcus = new User();
        marcus.setPassword("password1");
        marcus.setEmail("marcus.a.scott@gmail.com");

        //List filling database
        pairProgramming = new ArrayList<>(Arrays.asList(jesus,marcus));

    }

    @Test
    @Transactional
    @Rollback
    public void getListOfUsers()throws Exception{
//        this.repository.saveAll(pairProgramming);
        this.repository.save(jesus);
        this.repository.save(marcus);
        MockHttpServletRequestBuilder request = get("/users");
        String jsonJesusEmail = "ja@army.mil";
        String jsonMarcusEmail = "marcus.a.scott@gmail.com";
        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(jsonJesusEmail))
                .andExpect(jsonPath("$[1].email").value(jsonMarcusEmail));


    }
    @Test
    @Transactional
    @Rollback
    public void postUser()throws Exception{
        String json = "{\n" +
                "    \"email\": \"john@example.com\",\n" +
                "    \"password\": \"something-secret\"\n" +
                "  }";
        MockHttpServletRequestBuilder request = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);


        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"));


    }

    @Test
    @Transactional
    @Rollback
    public void getById() throws Exception{
        this.repository.save(jesus);
        MockHttpServletRequestBuilder request = get("/users/1");
        String jsonJesusEmail = "ja@army.mil";

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(jsonJesusEmail));
    }
    @Test
    @Transactional
    @Rollback
    public void patchById () throws Exception {
        this.repository.save(marcus);
        String newEmail = "marcus.a.scott2@gmail.com";
        MockHttpServletRequestBuilder request = patch(String.format("/users/%d", marcus.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"email\": \"marcus.a.scott2@gmail.com\",\n" +
                        "  \"password\": \"1234\"\n" +
                        "}");
        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(newEmail))
                .andExpect(jsonPath("$.id").value(marcus.getId()));
    }
}
