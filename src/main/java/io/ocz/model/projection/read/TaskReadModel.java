package io.ocz.model.projection.read;

import io.ocz.model.Task;

public class TaskReadModel {
    private boolean done;
    private String description;

    public TaskReadModel(Task source) {
        description = source.getDescription();
        done = source.isDone();
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
