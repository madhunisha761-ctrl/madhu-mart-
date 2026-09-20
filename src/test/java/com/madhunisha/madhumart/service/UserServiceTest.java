package com.madhunisha.madhumart.service;

import com.madhunisha.madhumart.dao.UserDao;
import com.madhunisha.madhumart.exception.AppException;
import com.madhunisha.madhumart.model.User;
import com.madhunisha.madhumart.util.PasswordUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Test
    void registerRejectsShortPassword() throws Exception {
        UserDao dao = mock(UserDao.class);
        UserService service = new UserService(dao);
        AppException e = assertThrows(AppException.class,
                () -> service.register("Madhu", "a@b.com", "short", "BUYER"));
        assertTrue(e.getMessage().contains("8 characters"));
        verify(dao, never()).create(any());
    }

    @Test
    void registerRejectsAdminRole() {
        UserService service = new UserService(mock(UserDao.class));
        assertThrows(AppException.class,
                () -> service.register("Madhu", "a@b.com", "password123", "ADMIN"));
    }

    @Test
    void registerRejectsDuplicateEmail() throws Exception {
        UserDao dao = mock(UserDao.class);
        when(dao.findByEmail("a@b.com")).thenReturn(new User());
        UserService service = new UserService(dao);
        AppException e = assertThrows(AppException.class,
                () -> service.register("Madhu", "a@b.com", "password123", "BUYER"));
        assertEquals("Email already registered", e.getMessage());
    }

    @Test
    void loginSucceedsWithCorrectPassword() throws Exception {
        UserDao dao = mock(UserDao.class);
        User u = new User();
        u.setEmail("a@b.com");
        u.setActive(true);
        u.setPasswordHash(PasswordUtil.hash("password123"));
        when(dao.findByEmail("a@b.com")).thenReturn(u);
        UserService service = new UserService(dao);
        assertEquals("a@b.com", service.login("a@b.com", "password123").getEmail());
    }

    @Test
    void loginFailsWithWrongPassword() throws Exception {
        UserDao dao = mock(UserDao.class);
        User u = new User();
        u.setActive(true);
        u.setPasswordHash(PasswordUtil.hash("password123"));
        when(dao.findByEmail("a@b.com")).thenReturn(u);
        UserService service = new UserService(dao);
        assertThrows(AppException.class, () -> service.login("a@b.com", "wrongpass"));
    }
}
