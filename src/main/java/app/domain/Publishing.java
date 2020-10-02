package app.domain;

public class Publishing  extends Entity{
    private static final long serialVersionUID = 5692708766041889396L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Publishing{" +
                "name='" + name + '\'' +
                '}';
    }
}
