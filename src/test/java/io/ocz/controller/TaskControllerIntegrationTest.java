package io.ocz.controller;

import io.ocz.model.Task;
import io.ocz.model.contract.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository repository;

    @Test
    void httpGet_getTask() throws Exception {
        //given
        int id = repository.save(new Task("foo", LocalDateTime.now())).getId();

        //when + then
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/" + id))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}
