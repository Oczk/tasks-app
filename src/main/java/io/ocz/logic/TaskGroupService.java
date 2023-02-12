package io.ocz.logic;

import io.ocz.model.TaskGroup;
import io.ocz.model.contract.TaskGroupRepository;
import io.ocz.model.contract.TaskRepository;
import io.ocz.model.projection.read.GroupReadModel;
import io.ocz.model.projection.write.GroupWriteModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskGroupService {
    private TaskGroupRepository groupRepository;
    private TaskRepository taskRepository;

    public TaskGroupService(TaskGroupRepository groupRepository, TaskRepository taskRepository) {
        this.groupRepository = groupRepository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(final GroupWriteModel source) {
        TaskGroup result = groupRepository.save(source.toTaskGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return groupRepository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if(taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone tasks. Done all tasks first");
        }
        TaskGroup result = groupRepository.findById(groupId).orElseThrow(
                () -> new IllegalArgumentException("Task group with given id not found"));
        result.setDone(!result.isDone());
    }
}
