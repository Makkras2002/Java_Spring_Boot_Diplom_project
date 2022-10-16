package com.makkras.shop.entity;

public enum RoleType {
    GUEST ("GUEST",2L),
    CLIENT ("CLIENT",0L),
    EMPLOYEE ("EMPLOYEE",1L);

    private final String roleName;
    private final Long roleId;

    RoleType(String role,Long roleId) {
        this.roleName = role;
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public Long getRoleId() {
        return roleId;
    }
}
