package com.mateus.demo.service;

import com.mateus.demo.model.Task;
import com.mateus.demo.model.User;
import com.mateus.demo.repository.TaskRepository;
import com.mateus.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private UserService userService;


	public Task findById(Long id) {
		Optional<Task> task = taskRepository.findById(id);
		return task.orElseThrow(() -> new RuntimeException("task dont finded"));
	}


	public List<Task> findAllTaskByUser (Long userId){
		Optional<List<Task>> allTasks = this.taskRepository.findByUser_Id(userId);
		return allTasks.orElseThrow(()-> new RuntimeException("no tasks found"));
	}

	@Transactional
	public Task createTask(Task task) {
		this.userService.findById(task.getUser().getId());
		task.setId(null);
		return taskRepository.save(task);
	}

	@Transactional
	public Task update(Task task) {
		Task newObj = this.findById(task.getId());
		newObj.setDescription(task.getDescription());
		return this.taskRepository.save(newObj);
	}

	public void delete(Long id){
		try {
			this.taskRepository.deleteById(id);
		} catch (Exception e){
			throw new RuntimeException("delete isn't possible, there are tasks in the user");
		}
	}
}
