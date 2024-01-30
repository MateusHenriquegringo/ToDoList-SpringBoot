package com.mateus.demo.repository;

import com.mateus.demo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	Optional<List<Task>> findByUser_Id(Long id);

}
