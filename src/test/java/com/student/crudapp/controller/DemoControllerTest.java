package com.student.crudapp.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoControllerTest {
    static MockMvc mockMvc;

    @BeforeAll
    static void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new DemoController()).build();
    }

    @Test
    void testGetBranch() {
        Map<Integer, String> testCases = new HashMap<>();
        testCases.put(0, "master");
        testCases.put(1, "dev");
        testCases.put(3, "release");
        testCases.forEach((key, value) -> {
            try {
                mockMvc
                        .perform(MockMvcRequestBuilders.get("/test/{branchId}", key)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8.name()))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().string(value))
                        .andReturn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
