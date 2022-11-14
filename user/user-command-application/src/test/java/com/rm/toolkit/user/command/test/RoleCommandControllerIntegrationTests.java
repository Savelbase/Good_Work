package com.rm.toolkit.user.command.test;

import com.rm.toolkit.user.command.UserCommandApplication;
import com.rm.toolkit.user.command.dto.command.role.EditRoleCommand;
import com.rm.toolkit.user.command.dto.command.user.ChangeUserDepartmentCommand;
import com.rm.toolkit.user.command.dto.response.SuccessResponse;
import com.rm.toolkit.user.command.model.Role;
import com.rm.toolkit.user.command.model.User;
import com.rm.toolkit.user.command.security.AuthorityType;
import com.rm.toolkit.user.command.test.repository.UserTestRepository;
import com.rm.toolkit.user.command.test.util.DbUtil;
import com.rm.toolkit.user.command.test.util.NetUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@ActiveProfiles("integration-test")
@SpringBootTest(classes = {UserCommandApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(value = "classpath:/db/testdata/afterMigrate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9093", "port=9093"})
class RoleCommandControllerIntegrationTests {

    private static final String ARM_ROLE_ID = "9d859c1c-b5f8-4db6-99cd-2784368d4178";
    private static final String RM_ROLE_ID = "0fea835f-dfbe-400a-bb46-b0a815d2493b";
    private static final String SRM_ROLE_ID = "a7626246-bc25-4671-9f21-710a1e83f914";
    private static final String RD_ROLE_ID = "43b768e7-c117-4da6-9f41-9b6497aa7b31";
    private static final String ADMIN_ID = "e45bfff0-1b04-4b8a-ac95-9629dff88a3e";

    @Value("${role.employee}")
    private String EMPLOYEE_ROLE_ID;
    @Value("${role.admin}")
    private String ADMIN_ROLE_ID;
    private UriComponentsBuilder editRoleUriComponentBuilder;
    private UriComponentsBuilder deleteRoleUriComponentBuilder;

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserTestRepository userRepository;
    @Autowired
    private NetUtil netUtil;
    @Autowired
    private DbUtil dbUtil;

    private static Stream<Arguments> editRole_ValidRequest_ValidTree() {
        return Stream.of(
                Arguments.of(SRM_ROLE_ID, "BRM", 5, new AuthorityType[]{AuthorityType.AUTHORIZATION,
                        AuthorityType.VIEW_ROLES, AuthorityType.VIEW_DEPARTMENTS, AuthorityType.EMPLOYEE_LIST,
                        AuthorityType.ONE_TO_ONE, AuthorityType.EDIT_FEEDBACKS, AuthorityType.ASSESSMENT_GOALS,
                        AuthorityType.EDIT_COMMENTS, AuthorityType.EMPLOYEE_CARD})
        );
    }

    private static Stream<Arguments> deleteRole_ValidRequest_ValidTree() {
        return Stream.of(
                Arguments.of(ARM_ROLE_ID),
                Arguments.of(RM_ROLE_ID),
                Arguments.of(SRM_ROLE_ID)
        );
    }

    @PostConstruct
    public void init() {
        String baseAddress = "http://127.0.0.1:" + port;
        editRoleUriComponentBuilder = UriComponentsBuilder
                .fromUriString(baseAddress + "/api/v1/roles/{roleId}");
        deleteRoleUriComponentBuilder = UriComponentsBuilder
                .fromUriString(baseAddress + "/api/v1/roles/{roleId}");
    }

    @AfterEach
    @Transactional
    public void resetDatabaseState() {
        dbUtil.truncateAllTables();
    }

    @AfterAll
    public void dropAllTables() {
        dbUtil.dropAllTables();
    }

    @ParameterizedTest
    @MethodSource("editRole_ValidRequest_ValidTree")
    void editRole_ValidRequest_ValidTree(String roleId, String roleName, Integer rolePriority,
                                         AuthorityType[] roleAuthorities) {
        // GIVEN
        Role role = dbUtil.findRoleById(roleId);
        Set<User> usersWithThatRole = userRepository.findAllByRoleId(roleId);

        // Pair<Id нового RMа, прямые подчинённые>
        Map<String, Set<String>> subordinatesMap = new HashMap<>();
        for (User user : usersWithThatRole) {
            Set<String> subordinatesIds = userRepository.findAllByResourceManagerId(user.getId()).stream()
                    .filter(u -> {
                        Role subordinateRole = dbUtil.findRoleById(u.getRoleId());
                        return role.getPriority() > subordinateRole.getPriority()
                                && subordinateRole.getPriority() >= rolePriority;
                    }).map(User::getId).collect(Collectors.toSet());
            if (!subordinatesIds.isEmpty()) {
                User rm = dbUtil.findUserById(user.getResourceManagerId());
                subordinatesMap.put(rm.getId(), subordinatesIds);
            }
        }

        EditRoleCommand command = new EditRoleCommand(roleName, rolePriority, Arrays.stream(roleAuthorities)
                .collect(Collectors.toSet()));
        HttpEntity<EditRoleCommand> httpEntity = new HttpEntity<>(command,
                netUtil.createHttpHeaders(ADMIN_ID));

        // WHEN
        ResponseEntity<SuccessResponse> response = restTemplate.exchange(editRoleUriComponentBuilder
                .build(roleId), HttpMethod.PUT, httpEntity, SuccessResponse.class);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        for (Map.Entry<String, Set<String>> subordinatesEntry : subordinatesMap.entrySet()) {
            Set<User> subordinates = subordinatesEntry.getValue().stream().map(i -> dbUtil.findUserById(i))
                    .collect(Collectors.toSet());
            assertThat(subordinates)
                    .hasSameSizeAs(subordinatesEntry.getValue())
                    .allMatch(u -> u.getResourceManagerId().equals(subordinatesEntry.getKey()));
        }
    }

    @ParameterizedTest
    @MethodSource("deleteRole_ValidRequest_ValidTree")
    void deleteRole_ValidRequest_ValidTree(String roleId) {
        // GIVEN
        Set<User> futureEmployees = userRepository.findAllByRoleId(roleId);

        // Pair<новый RM, бывшие прямые подчинённые>
        Map<String, Set<String>> subordinatesMap = new HashMap<>();
        for (User user : futureEmployees) {
            Set<String> subordinatesIds = userRepository.findAllByResourceManagerId(user.getId()).stream()
                    .map(User::getId).collect(Collectors.toSet());
            if (!subordinatesIds.isEmpty()) {
                subordinatesMap.put(user.getId(), subordinatesIds);
            }
        }

        HttpEntity<ChangeUserDepartmentCommand> httpEntity = new HttpEntity<>(netUtil.createHttpHeaders(ADMIN_ID));

        // WHEN
        ResponseEntity<SuccessResponse> response =
                restTemplate.exchange(deleteRoleUriComponentBuilder.build(roleId),
                        HttpMethod.DELETE, httpEntity, SuccessResponse.class);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Set<User> currentEmployees = userRepository.findAllByRoleId(EMPLOYEE_ROLE_ID);
        Set<String> currentEmployeesIds = currentEmployees.stream().map(User::getId).collect(Collectors.toSet());

        assertThat(currentEmployeesIds)
                .containsAll(futureEmployees.stream().map(User::getId).collect(Collectors.toSet()));

        for (Map.Entry<String, Set<String>> subordinatesEntry : subordinatesMap.entrySet()) {
            Set<User> subordinates = subordinatesEntry.getValue().stream().map(i -> dbUtil.findUserById(i))
                    .collect(Collectors.toSet());
            assertThat(subordinates)
                    .hasSameSizeAs(subordinatesEntry.getValue())
                    .allMatch(u -> u.getResourceManagerId().equals(subordinatesEntry.getKey()));
        }
    }
}
