package app.domain;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

public class CardTest {

    @Test
    public void calendarToString() {
        Calendar calendar = new GregorianCalendar(2020, Calendar.OCTOBER, 12);

        String actual =  Card.calendarToString(calendar);

        String expected = "2020-10-12";

        assertEquals(expected, actual);
    }
    @Test
    public void calendarToStringNull() {
        Calendar calendar = null;

        String actual =  Card.calendarToString(calendar);

        String expected = "";

        assertEquals(expected, actual);
    }

    @Test
    public void calendarIsAfterTrue() {
        Calendar calendar = new GregorianCalendar(2020, Calendar.OCTOBER, 12);

        String actual =  Card.calendarIsAfter(calendar);

        String expected = "true";

        assertEquals(expected, actual);
    }

    @Test
    public void calendarIsAfterFalse() {
        Calendar calendar = new GregorianCalendar(2099, Calendar.OCTOBER, 12);

        String actual =  Card.calendarIsAfter(calendar);

        String expected = "false";

        assertEquals(expected, actual);
    }
}