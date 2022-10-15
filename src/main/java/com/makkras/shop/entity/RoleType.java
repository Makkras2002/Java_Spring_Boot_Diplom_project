package com.makkras.shop.entity;

public enum RoleType {
    GUEST ("GUEST"),
    CLIENT ("CLIENT"),
    EMPLOYEE ("EMPLOYEE");

    private final String roleName;

    RoleType(String role) {
        this.roleName = role;
    }

    public String getRoleName() {
        return roleName;
    }

}
