package com.mateus.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Cascade;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="user_table")
public class User {

	public interface CreateUser {};
	public interface UpdateUser {};

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private long id;

	@Size(groups = {CreateUser.class},min = 2, max = 50)
	@NotBlank(groups = {CreateUser.class})
	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Size(groups = {CreateUser.class, UpdateUser.class}, min = 8)
	@NotBlank(groups = {CreateUser.class, UpdateUser.class})
	@Column(name = "password", nullable = false)
	private String password;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Task> userTasks =  new ArrayList<Task>();

	public User() {
	}

	public User(long id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@JsonIgnore
	public List<Task> getUserTasks() {
		return userTasks;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof User user)) return false;
		return getId() == user.getId() && Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getUserTasks(), user.getUserTasks());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getUsername(), getPassword(), getUserTasks());
	}
}
