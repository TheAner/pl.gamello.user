package gg.gamello.user.core.application.dto;

import gg.gamello.user.core.domain.language.Language;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UserDto {
	private UUID id;

	private String username;

	private String visibleName;

	private String slug;

	private String email;

	private boolean active;

	private Date registered;

	private String avatarLocation;

	private Language language;
}
