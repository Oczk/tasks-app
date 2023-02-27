package io.ocz.model.projection.write;

import io.ocz.model.TaskGroup;

import java.util.Set;
import java.util.stream.Collectors;

public class GroupWriteModel {
    private String description;
    private Set<TaskWriteModel> tasks;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<TaskWriteModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<TaskWriteModel> tasks) {
        this.tasks = tasks;
    }

    public TaskGroup toTaskGroup() {
        TaskGroup result = new TaskGroup();
        result.setDescription(description);
        result.setTasks(
                tasks.stream()
                        .map(source -> source.toTask(result))
                        .collect(Collectors.toSet())
        );

        return result;
    }
}
