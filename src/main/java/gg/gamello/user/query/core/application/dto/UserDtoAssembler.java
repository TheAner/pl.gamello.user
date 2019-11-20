package gg.gamello.user.query.core.application.dto;

import gg.gamello.user.command.core.domain.User;

public class UserDtoAssembler {

	public static UserDto convertSimple(User user) {
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setVisibleName(user.getVisibleName());
		userDto.setLanguage(user.getLanguage());
		userDto.setAvatarLocation(user.getAvatarLocation());
		return userDto;
	}

	public static UserDto convertDetailed(User user) {
		UserDto userDto = convertSimple(user);
		userDto.setUsername(user.getUsername());
		userDto.setSlug(user.getSlug());
		userDto.setEmail(user.getEmail());
		userDto.setActive(user.isActive());
		userDto.setRegistered(user.getRegistered());
		return userDto;
	}

	public static UserDto convertWithRoles(User user) {
		UserDto userDto = convertDetailed(user);
		userDto.setRoles(user.getRoles());
		return userDto;
	}
}
