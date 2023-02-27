package io.ocz.controller;

import io.ocz.model.Task;
import io.ocz.model.contract.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaskRepository repository;

    @Test
    void httpGet_returnsAllTasks() {
        //given
        repository.save(new Task("foo", LocalDateTime.now()));
        repository.save(new Task("bar", LocalDateTime.now()));

        //when
        Task[] result = restTemplate.getForObject(getServerUrl(), Task[].class);

        //then
        assertThat(result).hasSize(2);
    }

    @Test
    void httpPost_createNewTask() {
        //given
        Task task = new Task("createdWithTest", LocalDateTime.now());
        HttpEntity<Task> request = new HttpEntity<>(task);

        //when
        ResponseEntity<Task> result = restTemplate.postForEntity(getServerUrl(), request, Task.class);

        //then
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertNotEquals(result.getBody().getId(), 0);
        assertEquals(task.getDescription(), result.getBody().getDescription());
    }

    private String getServerUrl() {
        return "http://localhost:" + port + "/tasks";
    }
}