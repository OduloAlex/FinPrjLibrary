package app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class RoleTest {

    @Test
    public void getRole() {
        User user = new User();
        user.setRoleId(1);
        Role role = Role.getRole(user);
        assertEquals(Role.ADMIN, role);
    }

    @Test
    public void getName() {
        assertEquals("admin", Role.ADMIN.getName());
    }
}