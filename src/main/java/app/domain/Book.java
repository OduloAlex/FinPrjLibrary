package app.domain;

/**
 * Entity Book
 *
 * @author Alex Odulo
 */
public class Book extends Entity {
    private static final long serialVersionUID = 5692708766041889396L;

    private CatalogObj catalogObj;
    private int statusBookId;
    private String invNumber;

    public CatalogObj getCatalogObj() {
        return catalogObj;
    }

    public void setCatalogObj(CatalogObj catalogObj) {
        this.catalogObj = catalogObj;
    }

    public int getStatusBookId() {
        return statusBookId;
    }

    public void setStatusBookId(int statusBookId) {
        this.statusBookId = statusBookId;
    }

    public String getInvNumber() {
        return invNumber;
    }

    public void setInvNumber(String invNumber) {
        this.invNumber = invNumber;
    }

    /**
     * Return the entity as a string
     *
     * @return string entity
     */
    @Override
    public String toString() {
        return "Book{" +
                "catalogObj=" + catalogObj +
                ", statusBookId=" + statusBookId +
                ", invNumber='" + invNumber + '\'' +
                '}';
    }
}
