package com.mateus.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user_tasks")
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Long id;

	@JsonProperty(access= JsonProperty.Access.WRITE_ONLY)
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false, updatable = false)
	private User user;

	@NotBlank()
	@Column(name = "description", length = 255, nullable = false)
	@Size(min = 1, max = 255)
	private String description;

	public Task() {
	}

	public Task(Long id, User user, String description) {
		this.id = id;
		this.user = user;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Task task)) return false;
		return Objects.equals(getId(), task.getId()) && Objects.equals(getUser(), task.getUser()) && Objects.equals(getDescription(), task.getDescription());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getUser(), getDescription());
	}
}
