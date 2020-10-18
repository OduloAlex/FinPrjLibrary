package app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class AuthorTest {

    @Test
    public void testToString() {
        Author author = new Author();
        author.setName("abc");
        assertEquals("abc",author.getName());
        assertEquals("Author{name='abc'}",author.toString());
    }
}