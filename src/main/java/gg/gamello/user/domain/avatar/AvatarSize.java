package gg.gamello.user.domain.avatar;

public enum AvatarSize {
    mini(24),
    medium(48),
    full(100);

    private final int size;

    AvatarSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
