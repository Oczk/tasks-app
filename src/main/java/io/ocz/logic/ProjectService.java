package io.ocz.logic;

import io.ocz.TaskConfigurationProperties;
import io.ocz.model.Project;
import io.ocz.model.Task;
import io.ocz.model.TaskGroup;
import io.ocz.model.contract.ProjectRepository;
import io.ocz.model.contract.TaskGroupRepository;
import io.ocz.model.projection.read.GroupReadModel;
import io.ocz.model.projection.read.ProjectReadModel;
import io.ocz.model.projection.write.ProjectWriteModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository repository;

    private final TaskGroupRepository groupRepository;

    private final TaskConfigurationProperties config;

    public ProjectService(final ProjectRepository repository,
                          TaskGroupRepository groupRepository,
                          TaskConfigurationProperties properties) {
        this.repository = repository;
        this.groupRepository = groupRepository;
        this.config = properties;
    }

    public List<ProjectReadModel> readAll() {
        return repository.findAll().stream()
                .map(ProjectReadModel::new)
                .collect(Collectors.toList());
    }

    public ProjectReadModel save(ProjectWriteModel source) {
        Project result = repository.save(source.toProject());
        return new ProjectReadModel(result);
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime deadline) {
        if (groupRepository.existsByDoneIsFalseAndProject_Id(projectId)
                && !config.getTemplate().isAllowMultipleTasks()) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }

        TaskGroup result = repository.findById(projectId)
                .map(project -> {
                    TaskGroup group = new TaskGroup();
                    group.setDescription(project.getDescription());
                    group.setTasks(project.getSteps().stream()
                            .map(s -> {
                                Task task = new Task();
                                task.setDeadline(deadline.minusDays(s.getDaysToDeadline()));
                                task.setDescription(s.getDescription());
                                return task;
                            })
                            .collect(Collectors.toSet()));
                    return group;
                })
                .orElseThrow(() -> new IllegalArgumentException("Project with given id was not found"));
        return new GroupReadModel(result);
    }

}
