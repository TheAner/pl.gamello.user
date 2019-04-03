package gg.gamello.user.dao.type;

public enum TokenType {
    ACTIVATION(259200000), //3 days
    EMAIL(1800000),  //30 min
    PASSWORD(1800000), //30 min
    DELETE(1800000); //30 min

    private final long lifespan;

    TokenType(long lifespan) {
        this.lifespan = lifespan;
    }

    public long getLifespan() {
        return lifespan;
    }
}
