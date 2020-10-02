package app.domain;

public enum Role {
    ADMIN, LIBRARIAN, READER;

    public static Role getRole(User user) {
        int roleId = user.getRoleId()-1;
        return Role.values()[roleId];
    }

    public String getName() {
        return name().toLowerCase();
    }
}
