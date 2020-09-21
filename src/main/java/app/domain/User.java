package app.domain;

import java.util.List;

public class User {
    private int id;
    private String username;
    private String password;
    private boolean active;
    private RoleUser role;
    private String description;
    private List<Card> cards;
}
