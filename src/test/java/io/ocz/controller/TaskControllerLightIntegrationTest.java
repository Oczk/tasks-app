package io.ocz.controller;

import io.ocz.model.Task;
import io.ocz.model.contract.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(TaskController.class)
class TaskControllerLightIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository repository;

    @Test
    void httpGet_getTask() throws Exception {
        //given
        when(repository.findById(anyInt()))
                .thenReturn(Optional.of(new Task("foo", LocalDateTime.now())));
        //when + then
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/123"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().string(containsString("\"description\":\"foo\"")));
    }
}
