package com.TaskCollab.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class RoleDTOTest {

    @Test
    public void testRoleIdGetterSetter() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleId(1);
        assertEquals(1, roleDTO.getRoleId());
    }

    @Test
    public void testRoleNameGetterSetter() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleName("Admin");
        assertEquals("Admin", roleDTO.getRoleName());
    }

    @Test
    public void testCreatePermissionGetterSetter() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setCreatePermission(Boolean.TRUE);
        // Test both the generated getter and the overridden isCreatePermission()
        assertEquals(Boolean.TRUE, roleDTO.getCreatePermission());
        assertEquals(Boolean.TRUE, roleDTO.isCreatePermission());
    }

    @Test
    public void testReadPermissionGetterSetter() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setReadPermission(Boolean.FALSE);
        assertEquals(Boolean.FALSE, roleDTO.getReadPermission());
        assertEquals(Boolean.FALSE, roleDTO.isReadPermission());
    }

    @Test
    public void testDeletePermissionGetterSetter() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setDeletePermission(Boolean.TRUE);
        assertEquals(Boolean.TRUE, roleDTO.getDeletePermission());
        assertEquals(Boolean.TRUE, roleDTO.isDeletePermission());
    }

    @Test
    public void testUpdatePermissionGetterSetter() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setUpdatePermission(Boolean.FALSE);
        assertEquals(Boolean.FALSE, roleDTO.getUpdatePermission());
        assertEquals(Boolean.FALSE, roleDTO.isUpdatePermission());
    }

    @Test
    public void testUserNameGetterSetter() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setUserName("user_admin");
        assertEquals("user_admin", roleDTO.getUserName());
    }

    @Test
    public void testEqualsAndHashCode() {
        RoleDTO role1 = new RoleDTO();
        role1.setRoleId(1);
        role1.setRoleName("Admin");
        role1.setCreatePermission(Boolean.TRUE);
        role1.setReadPermission(Boolean.TRUE);
        role1.setDeletePermission(Boolean.TRUE);
        role1.setUpdatePermission(Boolean.TRUE);
        role1.setUserName("user_admin");

        RoleDTO role2 = new RoleDTO();
        role2.setRoleId(1);
        role2.setRoleName("Admin");
        role2.setCreatePermission(Boolean.TRUE);
        role2.setReadPermission(Boolean.TRUE);
        role2.setDeletePermission(Boolean.TRUE);
        role2.setUpdatePermission(Boolean.TRUE);
        role2.setUserName("user_admin");

        // They should be equal and have the same hash code
        assertEquals(role1, role2);
        assertEquals(role1.hashCode(), role2.hashCode());
    }

    @Test
    public void testToString() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleId(1);
        roleDTO.setRoleName("Admin");
        roleDTO.setCreatePermission(Boolean.TRUE);
        roleDTO.setReadPermission(Boolean.TRUE);
        roleDTO.setDeletePermission(Boolean.TRUE);
        roleDTO.setUpdatePermission(Boolean.TRUE);
        roleDTO.setUserName("user_admin");

        String str = roleDTO.toString();
        // Check that key values appear in the toString output.
        assertTrue(str.contains("roleId=1"));
        assertTrue(str.contains("roleName=Admin"));
        assertTrue(str.contains("createPermission=true"));
        assertTrue(str.contains("userName=user_admin"));
    }
}
