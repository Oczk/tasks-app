package io.ocz.model.projection.write;

import io.ocz.model.ProjectStep;

public class ProjectStepWriteModel {
    private String description;
    private int daysToDeadline;

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

    public ProjectStep toProjectStep() {
        ProjectStep result = new ProjectStep();
        result.setDescription(description);
        result.setDaysToDeadline(daysToDeadline);
        return result;
    }
}
