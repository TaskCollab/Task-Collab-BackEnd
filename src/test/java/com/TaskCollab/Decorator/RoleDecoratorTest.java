package com.TaskCollab.Decorator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.TaskCollab.Entity.RoleInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoleDecoratorTest {

    // Create a concrete subclass for testing since RoleDecorator is abstract.
    private static class TestRoleDecorator extends RoleDecorator {
        public TestRoleDecorator(RoleInterface decoratedRole) {
            super(decoratedRole);
        }
    }

    private RoleInterface decoratedMock;
    private TestRoleDecorator testDecorator;

    @BeforeEach
    public void setUp() {
        decoratedMock = mock(RoleInterface.class);
        testDecorator = new TestRoleDecorator(decoratedMock);
    }

    @Test
    public void testGetRoleId() {
        when(decoratedMock.getRoleId()).thenReturn(123);
        assertEquals(123, testDecorator.getRoleId());
        verify(decoratedMock, times(1)).getRoleId();
    }

    @Test
    public void testSetRoleId() {
        testDecorator.setRoleId(456);
        verify(decoratedMock, times(1)).setRoleId(456);
    }

    @Test
    public void testGetRoleName() {
        when(decoratedMock.getRoleName()).thenReturn("TestRole");
        assertEquals("TestRole", testDecorator.getRoleName());
        verify(decoratedMock, times(1)).getRoleName();
    }

    @Test
    public void testSetRoleName() {
        testDecorator.setRoleName("NewRole");
        verify(decoratedMock, times(1)).setRoleName("NewRole");
    }

    @Test
    public void testIsCreatePermission() {
        when(decoratedMock.isCreatePermission()).thenReturn(true);
        assertEquals(true, testDecorator.isCreatePermission());
        verify(decoratedMock, times(1)).isCreatePermission();
    }

    @Test
    public void testSetCreatePermission() {
        testDecorator.setCreatePermission(false);
        verify(decoratedMock, times(1)).setCreatePermission(false);
    }

    @Test
    public void testIsReadPermission() {
        when(decoratedMock.isReadPermission()).thenReturn(true);
        assertEquals(true, testDecorator.isReadPermission());
        verify(decoratedMock, times(1)).isReadPermission();
    }

    @Test
    public void testSetReadPermission() {
        testDecorator.setReadPermission(false);
        verify(decoratedMock, times(1)).setReadPermission(false);
    }

    @Test
    public void testIsDeletePermission() {
        when(decoratedMock.isDeletePermission()).thenReturn(true);
        assertEquals(true, testDecorator.isDeletePermission());
        verify(decoratedMock, times(1)).isDeletePermission();
    }

    @Test
    public void testSetDeletePermission() {
        testDecorator.setDeletePermission(false);
        verify(decoratedMock, times(1)).setDeletePermission(false);
    }

    @Test
    public void testIsUpdatePermission() {
        when(decoratedMock.isUpdatePermission()).thenReturn(true);
        assertEquals(true, testDecorator.isUpdatePermission());
        verify(decoratedMock, times(1)).isUpdatePermission();
    }

    @Test
    public void testSetUpdatePermission() {
        testDecorator.setUpdatePermission(false);
        verify(decoratedMock, times(1)).setUpdatePermission(false);
    }

    @Test
    public void testGetUserName() {
        when(decoratedMock.getUserName()).thenReturn("user123");
        assertEquals("user123", testDecorator.getUserName());
        verify(decoratedMock, times(1)).getUserName();
    }

    @Test
    public void testSetUserName() {
        testDecorator.setUserName("newUser");
        verify(decoratedMock, times(1)).setUserName("newUser");
    }
}
