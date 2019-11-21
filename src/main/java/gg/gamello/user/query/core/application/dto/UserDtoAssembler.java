package gg.gamello.user.query.core.application.dto;

import gg.gamello.user.command.core.domain.User;

public class UserDtoAssembler {
	private User user;
	private UserDto userDto;

	private UserDtoAssembler(User user) {
		this.user = user;
		this.userDto = new UserDto();
	}

	public static UserDtoAssembler builder(User user) {
		return new UserDtoAssembler(user);
	}

	public UserDtoAssembler simple() {
		userDto.setId(user.getId());
		userDto.setVisibleName(user.getVisibleName());
		userDto.setAvatarLocation(user.getAvatarLocation());
		return this;
	}

	public UserDtoAssembler detailed() {
		simple();
		userDto.setSlug(user.getSlug());
		userDto.setRegistered(user.getRegistered());
		return this;
	}

	public UserDtoAssembler secured() {
		detailed();
		userDto.setActive(user.isActive());
		userDto.setUsername(user.getUsername());
		userDto.setLanguage(user.getLanguage());
		userDto.setEmail(user.getEmail());
		userDto.setRoles(user.getRoles());
		return this;
	}

	public UserDto build() {
		return userDto;
	}
}
