package io.ocz.model.projection.write;

import io.ocz.model.Project;

import java.util.Set;
import java.util.stream.Collectors;

public class ProjectWriteModel {
    private String description;
    private Set<GroupWriteModel> groups;
    private Set<ProjectStepWriteModel> steps;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<GroupWriteModel> getGroups() {
        return groups;
    }

    public void setGroups(Set<GroupWriteModel> groups) {
        this.groups = groups;
    }

    public Set<ProjectStepWriteModel> getSteps() {
        return steps;
    }

    public void setSteps(Set<ProjectStepWriteModel> steps) {
        this.steps = steps;
    }

    public Project toProject() {
        Project result = new Project();
        result.setDescription(description);
        result.setGroups(
                groups.stream()
                        .map(GroupWriteModel::toTaskGroup)
                        .collect(Collectors.toSet())
        );
        result.setSteps(
                steps.stream()
                        .map(ProjectStepWriteModel::toProjectStep)
                        .collect(Collectors.toSet())
        );

        return result;
    }
}
