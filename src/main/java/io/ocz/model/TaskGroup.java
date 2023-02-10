package io.ocz.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "task_groups")
public class TaskGroup extends BaseTask {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private Set<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public TaskGroup() {
    } //for Hibernate

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
}
