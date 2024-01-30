package com.mateus.demo.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum ProfileEnum {

	ADMIN(1,"ROLE_ADMIN"),
	USER(2,"ROLE_USER");

	private Integer code;
	private String description;

	public static ProfileEnum toEnum(Integer code){
		if (Objects.isNull(code)){
			return null;
		}
		for (ProfileEnum e: ProfileEnum.values()){
			if (code.equals(e.code)){
				return e;
			}
		}
		throw new IllegalArgumentException("Invalidid code: "+ code);
	}
}
