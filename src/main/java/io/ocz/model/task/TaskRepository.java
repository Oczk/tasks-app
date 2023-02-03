package io.ocz.model.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();

    Page<Task> findAll(Pageable pageable);

    Optional<Task> findById(Integer id);

    boolean existsById(Integer id);

    List<Task> findByDone(@Param("state") boolean done);

    Task save(Task entity);
}
