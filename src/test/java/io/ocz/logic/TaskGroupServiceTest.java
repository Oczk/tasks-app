package io.ocz.logic;

import io.ocz.model.TaskGroup;
import io.ocz.model.contract.TaskGroupRepository;
import io.ocz.model.contract.TaskRepository;
import io.ocz.object.TaskGroupRepositories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    TaskGroupService sut;
    private TaskGroupRepository taskGroupRepository;
    private TaskRepository taskRepository;

    @BeforeEach
    void setup() {
        taskGroupRepository = mock(TaskGroupRepository.class);
        taskRepository = mock(TaskRepository.class);

    }

    @Test
    void toggleGroup_undoneTaskExists_throwsIllegalStateException() {
        //given
        groupRepositoryReturning(true);
        sut = new TaskGroupService(taskGroupRepository, taskRepository);

        //when
        var exception = catchThrowable(() -> sut.toggleGroup(0));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("undone tasks");
    }

    @Test
    void toggleGroup_groupRepositoryEmpty_throwsIllegalArgumentException() {
        //given
        sut = new TaskGroupService(taskGroupRepository, taskRepository);

        //when
        var exception = catchThrowable(() -> sut.toggleGroup(0));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Task group with given id not found");
    }

    @Test
    void toggleGroup_groupExists_doneChanged() {
        //given
        TaskGroup group = new TaskGroup();
        group.setDone(false);
        TaskGroupRepository inMemoryGroupRepository = TaskGroupRepositories.getInstance().createTaskGroupRepository();
        inMemoryGroupRepository.save(group);
        sut = new TaskGroupService(inMemoryGroupRepository, taskRepository);

        //when
        sut.toggleGroup(1);

        //then
        assertThat(inMemoryGroupRepository.findById(1).get().isDone()).isEqualTo(true);

    }

    private void groupRepositoryReturning(final boolean result) {
        when(taskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(result);
    }
}