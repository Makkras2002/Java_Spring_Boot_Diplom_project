package com.makkras.shop.entity;

import javax.persistence.*;

@Entity(name = "user_roles")
public class UserRole extends CustomEntity {
    @Id
    @Column(name = "role_id")
    private Long roleId;
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private RoleType roleType;

    public UserRole() {
    }

    public UserRole(RoleType roleType) {
        this.roleType = roleType;
    }

    public UserRole(Long roleId, RoleType roleType) {
        this.roleId = roleId;
        this.roleType = roleType;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleName) {
        this.roleType = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRole role = (UserRole) o;

        if (roleId != null ? !roleId.equals(role.roleId) : role.roleId != null) return false;
        return roleType == role.roleType;
    }

    @Override
    public int hashCode() {
        int result = roleId != null ? roleId.hashCode() : 0;
        result = 31 * result + (roleType != null ? roleType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserRole{");
        sb.append("roleId=").append(roleId);
        sb.append(", roleName=").append(roleType);
        sb.append('}');
        return sb.toString();
    }
}
