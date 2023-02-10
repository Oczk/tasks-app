package io.ocz.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@MappedSuperclass
public class BaseTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    @NotBlank(message = "Description must not be empty.")
    protected String description;
    protected boolean done;
    @Embedded
    private Audit audit = new Audit(); //Composition over inheritance

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
