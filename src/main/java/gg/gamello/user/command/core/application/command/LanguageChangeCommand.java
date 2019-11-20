package gg.gamello.user.command.core.application.command;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LanguageChangeCommand {
	@NotEmpty(message = "Language can not be empty")
	String language;
}
