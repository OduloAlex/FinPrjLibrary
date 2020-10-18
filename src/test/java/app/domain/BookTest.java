package app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class BookTest {

    @Test
    public void testToString() {
        Book entity = new Book();
        entity.setInvNumber("a");
        assertEquals("a",entity.getInvNumber());
        entity.setStatusBookId(1);
        assertEquals(1,entity.getStatusBookId());
        assertEquals("Book{catalogObj=null, statusBookId=1, invNumber='a'}",entity.toString());
    }
}