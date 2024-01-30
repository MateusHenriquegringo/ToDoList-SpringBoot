package com.mateus.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mateus.demo.model.enums.ProfileEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "user_table")
public class User {
	public interface CreateUser {
	}


	public interface UpdateUser {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private long id;


	@Size(groups = {CreateUser.class}, min = 2, max = 50)
	@NotBlank(groups = {CreateUser.class})
	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Size(groups = {CreateUser.class, UpdateUser.class}, min = 8)
	@NotBlank(groups = {CreateUser.class, UpdateUser.class})
	@Column(name = "password", nullable = false)
	private String password;

	@OneToMany(mappedBy = "user")
	private List<Task> userTasks = new ArrayList<Task>();

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	public List<Task> getUserTasks() {
		return userTasks;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@CollectionTable(name = "user_profile")
	@Column(name = "profile", nullable = false)
	private Set<Integer> profiles = new HashSet<>();

	public Set<ProfileEnum> getProfiles(){
		return this.profiles.stream().map(ProfileEnum::toEnum).collect(Collectors.toSet());
	}

	public void addProfiles(ProfileEnum profileEnum){
		this.profiles.add(profileEnum.getCode());
	}

}
