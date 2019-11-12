package gg.gamello.user.core.application.command;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class SlugChangeCommand {
	@NotEmpty(message = "Slug can not be empty")
	@Size(min = 3, max = 24, message = "Slug must be between {min} and {max} characters long")
	String slug;
}
