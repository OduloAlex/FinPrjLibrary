package app.domain;

public class CatalogObj  extends Entity{
    private static final long serialVersionUID = 5692708766041889396L;

    private String name;
    private Author author;
    private Publishing publishing;
    private int year;
    private int fine;
    private String description;
    private int quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Publishing getPublishing() {
        return publishing;
    }

    public void setPublishing(Publishing publishing) {
        this.publishing = publishing;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CatalogObj{" +
                "name='" + name + '\'' +
                ", author=" + author +
                ", publishing=" + publishing +
                ", year=" + year +
                ", fine=" + fine +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
