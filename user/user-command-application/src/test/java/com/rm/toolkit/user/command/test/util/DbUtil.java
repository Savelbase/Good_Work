package com.rm.toolkit.user.command.test.util;

import com.rm.toolkit.user.command.exception.notfound.DepartmentNotFoundException;
import com.rm.toolkit.user.command.exception.notfound.RoleNotFoundException;
import com.rm.toolkit.user.command.exception.notfound.UserNotFoundException;
import com.rm.toolkit.user.command.model.Department;
import com.rm.toolkit.user.command.model.Role;
import com.rm.toolkit.user.command.model.User;
import com.rm.toolkit.user.command.test.repository.DepartmentTestRepository;
import com.rm.toolkit.user.command.test.repository.RoleTestRepository;
import com.rm.toolkit.user.command.test.repository.UserTestRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class DbUtil {

    private final JdbcTemplate jdbcTemplate;
    private final UserTestRepository userRepository;
    private final DepartmentTestRepository departmentRepository;
    private final RoleTestRepository roleRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void truncateAllTables() {
        List<String> tableNames = getAllTableNames();

        String tableNamesConcatenated = tableNames.stream().reduce("", (s1, s2) ->
                String.format("%s, %s", s1, s2)).substring(2);
        jdbcTemplate.execute("TRUNCATE TABLE " + tableNamesConcatenated);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void dropAllTables() {
        List<String> tableNames = getAllTableNames();

        String tableNamesConcatenated = tableNames.stream().reduce("", (s1, s2) ->
                String.format("%s, %s", s1, s2)).substring(2);
        jdbcTemplate.execute("DROP TABLE " + tableNamesConcatenated);
    }

    @Transactional(readOnly = true)
    @SneakyThrows
    public List<String> getAllTableNames() {
        return JdbcUtils.extractDatabaseMetaData(Objects.requireNonNull(jdbcTemplate.getDataSource()),
                dbMetadata -> {
                    ResultSet rs = dbMetadata.getTables(dbMetadata.getUserName(), null, null,
                            new String[]{"TABLE"});
                    List<String> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(rs.getString(3));
                    }
                    return list;
                });
    }

    public Department findDepartmentById(String departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));
    }

    public User findUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    public Role findRoleById(String roleId) {
        return roleRepository.findById(roleId).orElseThrow(() -> new RoleNotFoundException(roleId));
    }
}
