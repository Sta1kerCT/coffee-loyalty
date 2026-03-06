package ru.coffee.loyalty.api;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClientApiTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @Order(1)
    void createClient() throws Exception {
        mvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"API Test\",\"phone\":\"+7900\",\"email\":\"a@b.ru\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.fullName").value("API Test"))
                .andExpect(jsonPath("$.balancePoints").value(0));
    }

    @Test
    @Order(2)
    void listClients() throws Exception {
        mvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(3)
    void getClient() throws Exception {
        mvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").exists());
    }

    @Test
    @Order(4)
    void updateClient() throws Exception {
        mvc.perform(put("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"API Updated\",\"phone\":\"+7900\",\"email\":\"a@b.ru\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("API Updated"));
    }

    @Test
    @Order(5)
    void deleteClient() throws Exception {
        mvc.perform(delete("/api/clients/1"))
                .andExpect(status().isNoContent());
        mvc.perform(get("/api/clients/1"))
                .andExpect(status().isNotFound());
    }
}
