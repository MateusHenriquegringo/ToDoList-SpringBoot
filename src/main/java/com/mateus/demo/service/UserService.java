package com.mateus.demo.service;

import com.mateus.demo.model.Task;
import com.mateus.demo.model.User;
import com.mateus.demo.repository.TaskRepository;
import com.mateus.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TaskRepository taskRepository;

	public User findById(Long id){
		Optional<User> user = this.userRepository.findById(id);
		return user.orElseThrow(()-> new RuntimeException(
				"User not finded"
		));
	}


	@Transactional
	public User createUser(User obj){

		BCryptPasswordEncoder cript = new BCryptPasswordEncoder();

		String senhaCripto = cript.encode(obj.getPassword());
		obj.setPassword(senhaCripto);

		obj = this.userRepository.save(obj);
		return obj;

	};

	@Transactional
	public User update(User obj){
		User newObj = this.findById(obj.getId());
		BCryptPasswordEncoder cript = new BCryptPasswordEncoder();

		String senhaCripto = cript.encode(obj.getPassword());
		obj.setPassword(senhaCripto);

		newObj.setPassword(obj.getPassword());
		return this.userRepository.save(newObj);
	};

	public void delete(Long id){
		try {
			this.userRepository.deleteById(id);
		} catch (Exception e){
			throw new RuntimeException("delete isn't possible, there are tasks in the user");
		}
	}
 }
