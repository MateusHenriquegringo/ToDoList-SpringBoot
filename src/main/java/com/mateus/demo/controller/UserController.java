package com.mateus.demo.controller;

import com.mateus.demo.model.User;
import com.mateus.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/user")
@Validated
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		User user = this.userService.findById(id);
		return ResponseEntity.ok().body(user);
	}

	@PostMapping()
	@Validated(User.CreateUser.class)
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		try {
			this.userService.createUser(user);
			URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();

			return ResponseEntity.created(uri).build();
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.unprocessableEntity().body("user already exists");
		}
	}

	@PutMapping("/{id}")
	@Validated(User.UpdateUser.class)
	public ResponseEntity<Void> updateUser(@Valid @RequestBody User user, @PathVariable Long id) {

		user.setId(id);
		this.userService.update(user);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(User user) {
		this.userService.delete(user.getId());

		return ResponseEntity.noContent().build();
	}
}
