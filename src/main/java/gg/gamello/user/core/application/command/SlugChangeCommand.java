package gg.gamello.user.core.application.command;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SlugChangeCommand {
	@NotEmpty(message = "Slug can not be empty")
	String slug;
}
