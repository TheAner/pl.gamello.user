package gg.gamello.user.query.core.application.query;

import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class UsersQuery {
	Set<UUID> userIds;

	public static UsersQuery from(List<UUID> userIds) {
		UsersQuery query = new UsersQuery();
		query.setUserIds(Set.copyOf(userIds));
		return query;
	}
}
