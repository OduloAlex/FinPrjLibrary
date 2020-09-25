package app.domain;

public enum RoleUser {
    ADMIN, LIBRARIAN, READER;

    public static RoleUser getRole(User user) {
        int roleId = user.getRoleId();
        return RoleUser.values()[roleId];
    }

    public String getName() {
        return name().toLowerCase();
    }
}
