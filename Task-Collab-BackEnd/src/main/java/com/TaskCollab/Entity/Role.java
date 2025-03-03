package com.TaskCollab.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @Column(nullable = false, unique = true)
    private String roleName;

    @Column(nullable = false)
    private boolean createPermission;

    @Column(nullable = false)
    private boolean readPermission;

    @Column(nullable = false)
    private boolean deletePermission;

    @Column(nullable = false)
    private boolean updatePermission;

    // Getters and Setters (REQUIRED for Hibernate)
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }


    // Getter methods (Fix for isCreate(), isRead(), isUpdate(), isDelete())
    public boolean isCreatePermission() {
        return createPermission;
    }

    public boolean isReadPermission() {
        return readPermission;
    }

    public boolean isDeletePermission() {
        return deletePermission;
    }

    public boolean isUpdatePermission() {
        return updatePermission;
    }

    // Setters
    public void setCreatePermission(boolean createPermission) {
        this.createPermission = createPermission;
    }

    public void setReadPermission(boolean readPermission) {
        this.readPermission = readPermission;
    }

    public void setDeletePermission(boolean deletePermission) {
        this.deletePermission = deletePermission;
    }

    public void setUpdatePermission(boolean updatePermission) {
        this.updatePermission = updatePermission;
    }
}
