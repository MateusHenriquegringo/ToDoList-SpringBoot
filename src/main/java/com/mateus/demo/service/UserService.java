package com.mateus.demo.service;

import com.mateus.demo.model.User;
import com.mateus.demo.model.enums.ProfileEnum;
import com.mateus.demo.repository.TaskRepository;
import com.mateus.demo.repository.UserRepository;
import com.mateus.demo.service.exceptions.DataBindingViolationException;
import com.mateus.demo.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private BCryptPasswordEncoder Bcrypt;

	public User findById(Long id) {
		Optional<User> user = this.userRepository.findById(id);
		return user.orElseThrow(() -> new ObjectNotFoundException(
				"User not finded"
		));
	}


	@Transactional
	public User createUser(User obj) {

		obj.setPassword(this.Bcrypt.encode(obj.getPassword()));
		obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
		obj = this.userRepository.save(obj);
		return obj;

	}

	;

	@Transactional
	public User update(User obj) {
		User newObj = this.findById(obj.getId());

		obj.setPassword(this.Bcrypt.encode(obj.getPassword()));

		newObj.setPassword(obj.getPassword());
		return this.userRepository.save(newObj);
	}

	;

	public void delete(Long id) {
		try {
			this.userRepository.deleteById(id);
		} catch (Exception e) {
			throw new DataBindingViolationException("delete isn't possible, there are tasks in the user");
		}
	}
}
