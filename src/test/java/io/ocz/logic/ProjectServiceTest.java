package io.ocz.logic;

import io.ocz.TaskConfigurationProperties;
import io.ocz.model.Project;
import io.ocz.model.ProjectStep;
import io.ocz.model.TaskGroup;
import io.ocz.model.contract.ProjectRepository;
import io.ocz.model.contract.TaskGroupRepository;
import io.ocz.model.projection.read.GroupReadModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    private ProjectService sut;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties config;
    private TaskConfigurationProperties.Template configTemplate;
    private ProjectRepository projectRepository;

    @BeforeEach
    void setup() {
        taskGroupRepository = mock(TaskGroupRepository.class);
        config = mock(TaskConfigurationProperties.class);
        configTemplate = mock(TaskConfigurationProperties.Template.class);
        projectRepository = mock(ProjectRepository.class);

    }

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and the other undone group exists")
    void createGroup_noMultipleGroupsConfigAndGroupWithUndoneTasksExists_throwsIllegalStateException() {
        //given
        groupRepositoryReturning(true);
        configurationReturning(false);
        sut = new ProjectService(null, taskGroupRepository, config);

        //when
        var exception = catchThrowable(() -> sut.createGroup(0, LocalDateTime.now()));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one undone group");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configuration is OK and no projects for a given id")
    void createGroup_configurationOkAndNoProjects_throwsIllegalArgumentException() {
        //given
        configurationReturning(true);
        when(projectRepository.findById(anyInt())).thenReturn(Optional.empty());
        sut = new ProjectService(projectRepository, taskGroupRepository, config);

        //when
        var exception = catchThrowable(() -> sut.createGroup(0, LocalDateTime.now()));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id was not found");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configuration allow just one group" +
            "and no projects for a given id")
    void createGroup_noMultipleGroupConfigAndNoUndoneGroupExistsAndNoProjects_throwsIllegalArgumentException() {
        //given
        configurationReturning(true);
        groupRepositoryReturning(false);
        when(projectRepository.findById(anyInt())).thenReturn(Optional.empty());
        sut = new ProjectService(projectRepository, taskGroupRepository, config);

        //when
        var exception = catchThrowable(() -> sut.createGroup(0, LocalDateTime.now()));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id was not found");
    }

    @Test
    @DisplayName("should create a new group")
    void createGroup_configurationOk_createsAndSavesGroup() {
        //given
        configurationReturning(true);
        var project = projectWith("bar", Set.of(1, 2));
        when(projectRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));
        InMemoryGroupRepository inMemoryGroupRepository = inMemoryGroupRepository();
        sut = new ProjectService(projectRepository, inMemoryGroupRepository, config);
        var today = LocalDate.now().atStartOfDay();

        //when
        GroupReadModel result = sut.createGroup(1, today);

        //then
        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("foo"));
    }

    private Project projectWith(String projectDescription, Set<Integer> daysToDeadline) {
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

    private void groupRepositoryReturning(final boolean result) {
        when(taskGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
    }

    private void configurationReturning(final boolean result) {
        when(configTemplate.isAllowMultipleTasks()).thenReturn(result);
        when(config.getTemplate()).thenReturn(configTemplate);
    }

    private InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository();
    }

    private static class InMemoryGroupRepository implements TaskGroupRepository {
        private int index = 0;
        private final Map<Integer, TaskGroup> map = new HashMap<>();

        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(TaskGroup entity) {
            if (entity.getId() == 0) {
                entity.setId(++index);
            }
            map.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
            return map.values().stream()
                    .filter(group -> !group.isDone())
                    .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
        }
    }

}