package com.example.feedbackqueryapplication.model.type;

public enum Role {
    EMPLOYEE(1),
    ARM(3),
    RM(5),
    SRM(7),
    RD(9),
    ADMIN(10);

    private final Integer rolePriority;

    Role(Integer rolePriority) {
        this.rolePriority = rolePriority;
    }

    public Integer getRolePriority() {
        return this.rolePriority;
    }
}
