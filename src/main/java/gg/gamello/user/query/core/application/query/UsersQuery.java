package gg.gamello.user.query.core.application.query;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class UsersQuery {
	Set<UUID> userIds;
}
