package com.bookstore.model;

public class Role {

    private int roleId;
    private String roleName;
    private String status;

    public Role() {
    }

    public Role(int roleId, String roleName, String status) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.status = status;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}