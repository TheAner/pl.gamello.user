package gg.gamello.user.confirmation.domain.action;

import java.util.Date;

public enum ActionType {
	ACTIVATION(259200000), //3 days
	EMAIL(1800000),  //30 min
	PASSWORD(1800000), //30 min
	DELETE(1800000); //30 min

	private final long lifespan;

	ActionType(long lifespan) {
		this.lifespan = lifespan;
	}

	public long getLifespan() {
		return lifespan;
	}

	public Date getExpirationDate() {
		return new Date(new Date().getTime() + this.getLifespan());
	}
}
