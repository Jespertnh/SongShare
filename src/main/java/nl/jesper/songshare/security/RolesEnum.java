package nl.jesper.songshare.security;

public enum RolesEnum {
    USER,
    ADMIN;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}

