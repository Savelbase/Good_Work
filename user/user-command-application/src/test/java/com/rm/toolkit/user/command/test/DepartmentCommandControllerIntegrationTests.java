package com.rm.toolkit.user.command.test;

import com.rm.toolkit.user.command.UserCommandApplication;
import com.rm.toolkit.user.command.dto.command.department.CreateDepartmentCommand;
import com.rm.toolkit.user.command.dto.command.department.EditDepartmentCommand;
import com.rm.toolkit.user.command.dto.command.user.ChangeUserDepartmentCommand;
import com.rm.toolkit.user.command.dto.response.SuccessResponse;
import com.rm.toolkit.user.command.model.Department;
import com.rm.toolkit.user.command.model.User;
import com.rm.toolkit.user.command.test.repository.DepartmentTestRepository;
import com.rm.toolkit.user.command.test.repository.UserTestRepository;
import com.rm.toolkit.user.command.test.util.DbUtil;
import com.rm.toolkit.user.command.test.util.NetUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.net.URI;
import java.util.*;
import java.util.function.Function;
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
class DepartmentCommandControllerIntegrationTests {

    private static final String DEV_DEPARTMENT_ID = "62f69579-ad7f-4dfd-9fea-b718d7c0d968";
    private static final String QA_DEPARTMENT_ID = "a1fa6daf-f31f-4fa3-87f2-797467743f0c";
    private static final String BOOKKEEPING_DEPARTMENT_ID = "a3580948-12b7-4a79-936c-69cece8584d3";
    private static final String EMPTY_DEPARTMENT_ID = "3a9b4a8a-9e8c-4b72-b3cd-4e72090c259b";
    private static final String ADMIN_ID = "e45bfff0-1b04-4b8a-ac95-9629dff88a3e";

    private URI createDepartmentUri;
    private UriComponentsBuilder editDepartmentUriComponentBuilder;
    private UriComponentsBuilder deleteDepartmentUriComponentBuilder;

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserTestRepository userRepository;
    @Autowired
    private DepartmentTestRepository departmentRepository;
    @Autowired
    private NetUtil netUtil;
    @Autowired
    private DbUtil dbUtil;

    private static Stream<Arguments> createDepartment_ValidRequest_ValidTree() {
        return Stream.of(
                Arguments.of("Test Department", "2f4e97a5-e631-4578-b570-0d656f2bbd5b",
                        new HashSet<>(List.of("7fbdcccb-f166-49af-ac63-a14d87a0d914",
                                "def5cc13-cd01-483c-916d-476ec22cdc68", "9645183c-8834-434f-be85-336d68fdbee9"))),
                Arguments.of("Test Department", "2f4e97a5-e631-4578-b570-0d656f2bbd5b",
                        new HashSet<>(List.of("33f0e4e0-0f16-4f7a-83a0-633daa763557",
                                "dde4e690-c634-4991-a751-5af71d3bdf70", "26db13ab-c76f-4f76-ae57-a9770a46e8ed")))
        );
    }

    private static Stream<Arguments> editDepartment_ValidRequest_ValidTree() {
        return Stream.of(
                Arguments.of(DEV_DEPARTMENT_ID, "Test Department", "2f4e97a5-e631-4578-b570-0d656f2bbd5b",
                        new HashSet<>(Set.of("7fbdcccb-f166-49af-ac63-a14d87a0d914",
                                "def5cc13-cd01-483c-916d-476ec22cdc68", "9645183c-8834-434f-be85-336d68fdbee9")))
        );
    }

    private static Stream<Arguments> deleteDepartment_ValidRequest_ValidTree() {
        return Stream.of(
                Arguments.of(DEV_DEPARTMENT_ID),
                Arguments.of(QA_DEPARTMENT_ID),
                Arguments.of(BOOKKEEPING_DEPARTMENT_ID)
        );
    }

    @PostConstruct
    public void init() {
        String baseAddress = "http://127.0.0.1:" + port;
        createDepartmentUri = URI.create(baseAddress + "/api/v1/departments");
        editDepartmentUriComponentBuilder = UriComponentsBuilder
                .fromUriString(baseAddress + "/api/v1/departments/{departmentId}");
        deleteDepartmentUriComponentBuilder = UriComponentsBuilder
                .fromUriString(baseAddress + "/api/v1/departments/{departmentId}");
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
    @MethodSource("createDepartment_ValidRequest_ValidTree")
    void createDepartment_ValidRequest_ValidTree(String departmentName, String headId, Set<String> membersIds) {
        // GIVEN
        membersIds.add(headId);

        // Map<Id пользователя, Pair<Id нового RM бывших прямых подчинённых, Set<Id бывших прямых подчинённых>>>
        Map<String, Pair<String, Set<String>>> usersAndDirectSubordinatesFromPreviousDepartments = membersIds.stream()
                .collect(Collectors.toMap(Function.identity(), i -> {
                    User user = dbUtil.findUserById(i);

                    Set<String> subordinates = userRepository.findAllByResourceManagerId(user.getId()).stream()
                            .map(User::getId).filter(id -> !membersIds.contains(id)).collect(Collectors.toSet());
                    User rm = dbUtil.findUserById(user.getResourceManagerId());
                    while (membersIds.contains(rm.getId())) {
                        rm = dbUtil.findUserById(rm.getResourceManagerId());
                    }
                    return Pair.of(rm.getId(), subordinates);
                }));

        CreateDepartmentCommand command = new CreateDepartmentCommand(departmentName, headId, membersIds);
        HttpEntity<CreateDepartmentCommand> httpEntity = new HttpEntity<>(command,
                netUtil.createHttpHeaders(ADMIN_ID));

        // WHEN
        ResponseEntity<SuccessResponse> response = restTemplate.exchange(createDepartmentUri,
                HttpMethod.POST, httpEntity, SuccessResponse.class);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Optional<Department> departmentOptional = departmentRepository.findByName(departmentName);
        assertThat(departmentOptional).isPresent();

        Department department = departmentOptional.get();

        assertThat(userRepository.findAllByDepartmentId(department.getId()))
                .hasSameSizeAs(membersIds)
                .allMatch(u -> membersIds.contains(u.getId()));

        User head = dbUtil.findUserById(headId);
        assertThat(head.getDepartmentId()).isEqualTo(department.getId());
        assertThat(department.getHeadId()).isEqualTo(head.getId());

        // Проверка, что в деревья зависимостей перестроились в предыдущих отделах пользователей
        assertThat(usersAndDirectSubordinatesFromPreviousDepartments.entrySet())
                .hasSameSizeAs(membersIds)
                .allMatch(entry -> {
                    Set<String> subordinatesIds = entry.getValue().getRight();
                    if (!subordinatesIds.isEmpty()) {
                        String rmId = entry.getValue().getLeft();
                        return subordinatesIds.stream().map(i -> dbUtil.findUserById(i))
                                .allMatch(u -> u.getResourceManagerId().equals(rmId));
                    }
                    return true;
                });
    }

    @ParameterizedTest
    @MethodSource("editDepartment_ValidRequest_ValidTree")
    void editDepartment_ValidRequest_ValidTree(String departmentId, String departmentName, String headId,
                                               Set<String> membersIds) {
        // GIVEN
        Set<String> oldMembersIds = userRepository.findAllByDepartmentId(departmentId).stream().map(User::getId)
                .collect(Collectors.toSet());

        membersIds.add(headId);
        Set<String> newMembersIds = membersIds.stream().filter(i -> !oldMembersIds.contains(i))
                .collect(Collectors.toSet());
        Set<String> removedMembersIds = oldMembersIds.stream().filter(i -> !membersIds.contains(i))
                .collect(Collectors.toSet());

        // Map<Id пользователя, Pair<Id нового RM бывших прямых подчинённых, Set<Id бывших прямых подчинённых>>>
        Map<String, Pair<String, Set<String>>> usersAndDirectSubordinatesFromPreviousDepartments =
                newMembersIds.stream().collect(Collectors.toMap(Function.identity(), i -> {
                    User user = dbUtil.findUserById(i);

                    Set<String> subordinates = userRepository.findAllByResourceManagerId(user.getId()).stream()
                            .map(User::getId).filter(id -> !membersIds.contains(id)).collect(Collectors.toSet());
                    User rm = dbUtil.findUserById(user.getResourceManagerId());
                    while (membersIds.contains(rm.getId())) {
                        rm = dbUtil.findUserById(rm.getResourceManagerId());
                    }
                    return Pair.of(rm.getId(), subordinates);
                }));

        EditDepartmentCommand command = new EditDepartmentCommand(departmentName, headId);
        HttpEntity<EditDepartmentCommand> httpEntity = new HttpEntity<>(command,
                netUtil.createHttpHeaders(ADMIN_ID));

        // WHEN
        ResponseEntity<SuccessResponse> response = restTemplate.exchange(editDepartmentUriComponentBuilder
                .build(departmentId), HttpMethod.PUT, httpEntity, SuccessResponse.class);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Optional<Department> departmentOptional = departmentRepository.findByName(departmentName);
        assertThat(departmentOptional).isPresent();

        Department departmentAfter = departmentOptional.get();
        assertThat(userRepository.findAllByDepartmentId(departmentAfter.getId()))
                .hasSameSizeAs(membersIds)
                .allMatch(u -> membersIds.contains(u.getId()));

        User head = dbUtil.findUserById(headId);
        assertThat(head.getDepartmentId()).isEqualTo(departmentAfter.getId());
        assertThat(departmentAfter.getHeadId()).isEqualTo(head.getId());

        if (!usersAndDirectSubordinatesFromPreviousDepartments.isEmpty()) {
            // Проверка, что в деревья зависимостей перестроились в предыдущих отделах пользователей
            assertThat(usersAndDirectSubordinatesFromPreviousDepartments.entrySet())
                    .allMatch(entry -> {
                        Set<String> subordinatesIds = entry.getValue().getRight();
                        if (!subordinatesIds.isEmpty()) {
                            String rmId = entry.getValue().getLeft();
                            return subordinatesIds.stream().map(i -> dbUtil.findUserById(i))
                                    .allMatch(u -> u.getResourceManagerId().equals(rmId));
                        }
                        return true;
                    });
        }

        if (!removedMembersIds.isEmpty())
            assertThat(removedMembersIds.stream().map(id -> dbUtil.findUserById(id)))
                    .allMatch(u -> u.getDepartmentId().equals(EMPTY_DEPARTMENT_ID)
                            && u.getResourceManagerId().equals(ADMIN_ID));
    }

    @ParameterizedTest
    @MethodSource("deleteDepartment_ValidRequest_ValidTree")
    void deleteDepartment_ValidRequest_ValidTree(String departmentId) {
        // GIVEN
        Set<String> usersToMoveIds = userRepository.findAllByDepartmentId(departmentId).stream().map(User::getId)
                .collect(Collectors.toSet());

        Set<User> emptyDepartmentInitialUsers = userRepository
                .findAllByDepartmentId(EMPTY_DEPARTMENT_ID);

        HttpEntity<ChangeUserDepartmentCommand> httpEntity = new HttpEntity<>(netUtil.createHttpHeaders(ADMIN_ID));

        // WHEN
        ResponseEntity<SuccessResponse> response =
                restTemplate.exchange(deleteDepartmentUriComponentBuilder.build(departmentId),
                        HttpMethod.DELETE, httpEntity, SuccessResponse.class);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Set<User> movedUsers = userRepository.findAllByDepartmentId(EMPTY_DEPARTMENT_ID);
        movedUsers.removeAll(emptyDepartmentInitialUsers);

        assertThat(movedUsers)
                .hasSameSizeAs(usersToMoveIds)
                .allMatch(u -> usersToMoveIds.contains(u.getId())
                        && u.getDepartmentId().equals(EMPTY_DEPARTMENT_ID)
                        && u.getResourceManagerId().equals(ADMIN_ID));
    }
}
