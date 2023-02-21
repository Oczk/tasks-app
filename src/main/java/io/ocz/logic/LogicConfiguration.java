package io.ocz.logic;

import io.ocz.TaskConfigurationProperties;
import io.ocz.model.contract.ProjectRepository;
import io.ocz.model.contract.TaskGroupRepository;
import io.ocz.model.contract.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogicConfiguration {

    @Bean
    ProjectService projectService(final ProjectRepository repository,
                                  final TaskGroupRepository taskGroupRepository,
                                  final TaskConfigurationProperties config) {
        return new ProjectService(repository, taskGroupRepository, config);
    }

    @Bean
    TaskGroupService taskGroupService(final TaskGroupRepository groupRepository,
                                      final TaskRepository taskRepository) {
        return new TaskGroupService(groupRepository, taskRepository);
    }
}
