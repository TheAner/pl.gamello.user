package gg.gamello.user.core.application.dto;

import gg.gamello.user.core.domain.User;

public class UserDtoAssembler {

	public static UserDto convertDefault(User user) {
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setUsername(user.getUsername());
		userDto.setVisibleName(user.getVisibleName());
		userDto.setSlug(user.getSlug());
		userDto.setEmail(user.getEmail());
		userDto.setActive(user.isActive());
		userDto.setRegistered(user.getRegistered());
		userDto.setAvatarLocation(user.getAvatarLocation());
		userDto.setLanguage(user.getLanguage());
		return userDto;
	}
}
