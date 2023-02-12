package io.ocz.model.projection.read;

import io.ocz.model.ProjectStep;

public class ProjectStepReadModel {
    private String description;
    private int daysToDeadline;

    public ProjectStepReadModel(ProjectStep source) {
        this.description = source.getDescription();
        this.daysToDeadline = source.getDaysToDeadline();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDaysToDeadline() {
        return daysToDeadline;
    }

    public void setDaysToDeadline(int daysToDeadline) {
        this.daysToDeadline = daysToDeadline;
    }
}
