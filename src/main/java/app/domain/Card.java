package app.domain;

import java.util.Calendar;

public class Card  extends Entity{
    private static final long serialVersionUID = 5692708766041889396L;

    private Book book;
    private Calendar createTime;
    private Calendar returnTime;
}
