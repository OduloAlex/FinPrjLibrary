package app.domain;

/**
 * Entity User
 *
 * @author Alex Odulo
 */
public class User extends Entity {
    private static final long serialVersionUID = 5692708766041889396L;

    private String username;
    private String password;
    private boolean active;
    private int roleId;
    private String description;
    private String localeName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocaleName() {
        return localeName;
    }

    public void setLocaleName(String localeName) {
        this.localeName = localeName;
    }

    /**
     * Return the entity as a string
     *
     * @return string entity
     */
    @Override
    public String toString() {
        return "User{" +
                "id='" + this.getId() + '\'' +
                ", active=" + active +
                ", roleId=" + roleId +
                ", localeName='" + localeName + '\'' +
                '}';
    }
}
