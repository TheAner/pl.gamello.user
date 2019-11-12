package gg.gamello.user.core.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import gg.gamello.user.core.domain.language.Language;
import gg.gamello.user.core.domain.role.Role;
import lombok.Data;

import java.util.Date;
import java.util.Set;
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

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Set<Role> roles;
}
