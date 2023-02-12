package io.ocz.model.projection.read;

import io.ocz.model.Project;

import java.util.Set;
import java.util.stream.Collectors;

public class ProjectReadModel {

    private String description;
    private Set<GroupReadModel> groups;
    private Set<ProjectStepReadModel> steps;

    public ProjectReadModel(Project source) {
        this.description = source.getDescription();
        groups = source.getGroups().stream().map(GroupReadModel::new).collect(Collectors.toSet());
        steps = source.getSteps().stream().map(ProjectStepReadModel::new).collect(Collectors.toSet());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<GroupReadModel> getGroups() {
        return groups;
    }

    public void setGroups(Set<GroupReadModel> groups) {
        this.groups = groups;
    }

    public Set<ProjectStepReadModel> getSteps() {
        return steps;
    }

    public void setSteps(Set<ProjectStepReadModel> steps) {
        this.steps = steps;
    }
}
