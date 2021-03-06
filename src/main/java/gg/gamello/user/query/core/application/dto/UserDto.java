package gg.gamello.user.query.core.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import gg.gamello.user.command.core.domain.language.Language;
import gg.gamello.user.command.core.domain.role.Role;
import lombok.Data;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data @JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
	private UUID id;

	private String username;

	private String visibleName;

	private String slug;

	private String email;

	private Boolean active;

	private Date registered;

	private String avatarLocation;

	private Language language;

	private Set<Role> roles;
}
