package io.ocz.object;

import io.ocz.model.Project;
import io.ocz.model.ProjectStep;

import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Projects {

    public static Project projectWith(String projectDescription, Set<Integer> daysToDeadline) {
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        var steps = daysToDeadline.stream()
                .map(days -> {
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("foo");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                })
                .collect(Collectors.toSet());
        when(result.getSteps()).thenReturn(steps);
        return result;
    }

}
