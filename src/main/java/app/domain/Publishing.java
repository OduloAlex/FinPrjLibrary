package app.domain;

/**
 * Entity Publishing
 *
 * @author Alex Odulo
 */
public class Publishing extends Entity {
    private static final long serialVersionUID = 5692708766041889396L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the entity as a string
     *
     * @return string entity
     */
    @Override
    public String toString() {
        return "Publishing{" +
                "name='" + name + '\'' +
                '}';
    }
}
