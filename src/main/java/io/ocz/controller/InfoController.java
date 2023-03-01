package io.ocz.controller;

import io.ocz.TaskConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class InfoController {
    private DataSourceProperties dataSource;
    private TaskConfigurationProperties taskProperties;

    public InfoController(DataSourceProperties dataSource, TaskConfigurationProperties taskProperties) {
        this.dataSource = dataSource;
        this.taskProperties = taskProperties;
    }

    @GetMapping("/url")
    String url() {
        return dataSource.getUrl();
    }

    @GetMapping("/prop")
    boolean isAllowMultipleTasksFromTemplate() {
        return taskProperties.getTemplate().isAllowMultipleTasks();
    }
}
