package gg.gamello.user.core.ui;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Response {
	private String status;
	private int code;
	private Object body;
}
