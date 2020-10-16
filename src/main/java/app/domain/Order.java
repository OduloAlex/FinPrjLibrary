package app.domain;

/**
 * Entity Order
 *
 * @author Alex Odulo
 */
public class Order extends Entity {
    private static final long serialVersionUID = 5692708766041889396L;

    private CatalogObj catalogObj;
    private User user;
    private int state;

    public CatalogObj getCatalogObj() {
        return catalogObj;
    }

    public void setCatalogObj(CatalogObj catalogObj) {
        this.catalogObj = catalogObj;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    /**
     * Return the entity as a string
     *
     * @return string entity
     */
    @Override
    public String toString() {
        return "Order{" +
                "catalogObj=" + catalogObj +
                ", user=" + user +
                ", state=" + state +
                '}';
    }
}
