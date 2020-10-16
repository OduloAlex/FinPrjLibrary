package app.domain;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Entity Card
 *
 * @author Alex Odulo
 */
public class Card extends Entity {
    private static final long serialVersionUID = 5692708766041889396L;

    private Book book;
    private User user;
    private Calendar createTime;
    private Calendar returnTime;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    public Calendar getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Calendar returnTime) {
        this.returnTime = returnTime;
    }

    /**
     * Return the entity as a string
     *
     * @return string entity
     */
    @Override
    public String toString() {
        return "Card{" +
                "book=" + book +
                ", user=" + user +
                ", createTime=" + calendarToString(createTime) +
                ", returnTime=" + calendarToString(returnTime) +
                '}';
    }

    /**
     * Convert date Calendar to String
     *
     * @param calendar Calendar
     * @return string
     */
    public static String calendarToString(Calendar calendar) {
        if (calendar != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(calendar.getTime());
        }
        return "";
    }

    /**
     * Will Calendar date be in the future
     *
     * @param calendar Calendar
     * @return string true or false
     */
    public static String calendarIsAfter(Calendar calendar) {
        Calendar today = Calendar.getInstance();
        today.setTime(Calendar.getInstance().getTime());
        if (calendar.compareTo(today) < 0) {
            return "true";
        }
        return "false";
    }
}
