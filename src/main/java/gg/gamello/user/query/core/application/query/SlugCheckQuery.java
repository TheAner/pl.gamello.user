package gg.gamello.user.query.core.application.query;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class SlugCheckQuery {
	@NotBlank(message = "Slug can not be empty")
	@Size(min = 3, max = 24, message = "Slug must be between {min} and {max} characters long")
	@Pattern(regexp = "^[a-z0-9]*$", message = "Slug must be lower case, letters and digits only")
	String slug;
}
