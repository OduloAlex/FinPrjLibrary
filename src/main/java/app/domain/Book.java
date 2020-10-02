package app.domain;

public class Book extends Entity {
    private static final long serialVersionUID = 5692708766041889396L;

    private CatalogObj catalogObj;
    private int statusBookId;

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

    @Override
    public String toString() {
        return "Book{" +
                "catalogObj=" + catalogObj +
                ", statusBookId=" + statusBookId +
                '}';
    }
}
