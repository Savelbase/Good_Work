package com.rm.toolkit.user.command.testUtil;

import com.rm.toolkit.user.command.model.*;
import com.rm.toolkit.user.command.model.type.StatusType;
import com.rm.toolkit.user.command.security.AuthorityType;
import org.springframework.stereotype.Component;

@Component
public class TestModelsBuilder {
    private final String value = "Test";

    public User getTestUser() {
        return User.builder()
                .id(value)
                .firstName(value)
                .lastName(value)
                .departmentId(value)
                .resourceManagerId(value)
                .roleId(value)
                .email(value)
                .status(StatusType.ACTIVE)
                .version(0)
                .build();
    }

    public Department getTestDepartment() {
        return Department.builder()
                .id(value)
                .headId(value)
                .name(value)
                .version(0)
                .deletable(false)
                .deleted(false)
                .build();
    }

    public Role getTestRole() {
        return Role.builder()
                .authorities(new AuthorityType[]{})
                .id(value)
                .name(value)
                .priority(0)
                .version(0)
                .deleted(false)
                .immutable(false)
                .build();
    }

    public City getTestCity() {
        return City.builder()
                .countryId(value)
                .id(value)
                .name(value)
                .version(0)
                .deleted(false)
                .build();
    }

    public Country getTestCountry() {
        return Country.builder()
                .id(value)
                .name(value)
                .deleted(false)
                .version(0)
                .build();
    }
}
