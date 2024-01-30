package com.mateus.demo.controller;

import com.mateus.demo.model.Task;
import com.mateus.demo.service.TaskService;
import com.mateus.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/task")
@Validated
public class TaskController {

	@Autowired
	private TaskService taskService;
	@Autowired
	private UserService userService;

	@GetMapping("/{id}")
	public ResponseEntity<Task> findById(@PathVariable Long id) {
		Task task = this.taskService.findById(id);
		return ResponseEntity.ok().body(task);
	}

	@Validated
	@PostMapping
	public ResponseEntity<Void> createTask(@RequestBody @Valid Task task) {
		this.taskService.createTask(task);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(task.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@PutMapping("/{id}")
	@Validated()
	public ResponseEntity<Void> updateTask(@Valid @RequestBody Task task, @PathVariable Long id) {
		task.setId(id);
		this.taskService.update(task);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
		this.taskService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<List<Task>> findAllTasks(@PathVariable Long id) {
		userService.findById(id);
		List<Task> tasks = this.taskService.findAllTaskByUser(id);
		return ResponseEntity.ok().body(tasks);
	}

}
