package io.ocz.object;

import io.ocz.model.TaskGroup;
import io.ocz.model.contract.TaskGroupRepository;

import java.util.*;

public final class TaskGroupRepositories {
    private static TaskGroupRepositories instance;
    int index = 0;

    private TaskGroupRepositories() {
    }

    public static TaskGroupRepositories getInstance() {
        if(instance == null) {
            instance = new TaskGroupRepositories();
        }
        return instance;
    }

    public TaskGroupRepository createTaskGroupRepository() {
        return new InMemoryGroupRepository();
    }

    private class InMemoryGroupRepository implements TaskGroupRepository {
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
