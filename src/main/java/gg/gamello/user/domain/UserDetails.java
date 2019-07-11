package gg.gamello.user.domain;

import gg.gamello.user.dao.type.Language;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data @AllArgsConstructor
public class UserDetails {
	private UUID id;

	private String visibleName;

	private Language language;

	private String avatarLocation;
}
