package app.domain;

/**
 * Enum Role for users
 *
 * @author Alex Odulo
 */
public enum Role {
    ADMIN, LIBRARIAN, READER;

    /**
     * Get user's role
     *
     * @param user user
     * @return role
     */
    public static Role getRole(User user) {
        int roleId = user.getRoleId() - 1;
        return Role.values()[roleId];
    }

    public String getName() {
        return name().toLowerCase();
    }
}
