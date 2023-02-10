package io.ocz.model.contract;

import io.ocz.model.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    List<Project> findAll();

    Optional<Project> findById(Integer id);

    Project save(Project entity);
}
