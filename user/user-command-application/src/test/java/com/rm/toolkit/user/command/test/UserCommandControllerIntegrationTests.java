package com.rm.toolkit.user.command.test;

import com.rm.toolkit.user.command.UserCommandApplication;
import com.rm.toolkit.user.command.dto.command.user.ChangeUserDepartmentCommand;
import com.rm.toolkit.user.command.dto.response.SuccessResponse;
import com.rm.toolkit.user.command.model.Department;
import com.rm.toolkit.user.command.model.User;
import com.rm.toolkit.user.command.repository.UserRepository;
import com.rm.toolkit.user.command.test.util.DbUtil;
import com.rm.toolkit.user.command.test.util.NetUtil;
import com.rm.toolkit.user.command.util.ValidationUtil;
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
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@ActiveProfiles("integration-test")
@SpringBootTest(classes = {UserCommandApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(value = "classpath:/db/testdata/afterMigrate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9093", "port=9093"})
class UserCommandControllerIntegrationTests {

    private static final String DEV_DEPARTMENT_ID = "62f69579-ad7f-4dfd-9fea-b718d7c0d968";
    private static final String QA_DEPARTMENT_ID = "a1fa6daf-f31f-4fa3-87f2-797467743f0c";
    private static final String BOOKKEEPING_DEPARTMENT_ID = "a3580948-12b7-4a79-936c-69cece8584d3";
    private static final String ADMIN_ID = "e45bfff0-1b04-4b8a-ac95-9629dff88a3e";

    private UriComponentsBuilder changeDepartmentUriComponentBuilder;

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ValidationUtil validationUtil;
    @Autowired
    private NetUtil netUtil;
    @Autowired
    private DbUtil dbUtil;

    private static Stream<Arguments> changeUserDepartment_ValidRequest_ValidTree() {
        return Stream.of(
                Arguments.of("7fbdcccb-f166-49af-ac63-a14d87a0d914", QA_DEPARTMENT_ID),
                Arguments.of("33f0e4e0-0f16-4f7a-83a0-633daa763557", DEV_DEPARTMENT_ID),
                Arguments.of("8b690fa4-f5ae-4dc7-a6c4-43332a215075", BOOKKEEPING_DEPARTMENT_ID)
        );
    }

    @PostConstruct
    public void init() {
        String baseAddress = "http://127.0.0.1:" + port;
        changeDepartmentUriComponentBuilder = UriComponentsBuilder
                .fromUriString(baseAddress + "/api/v1/users/{userId}/department");
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
    @MethodSource("changeUserDepartment_ValidRequest_ValidTree")
    void changeUserDepartment_ValidRequest_ValidTree(String userToMoveId, String destinationDepartmentId) {
        // GIVEN
        User user = validationUtil.findUserIfExist(userToMoveId);
        User resourceManagerBefore = validationUtil.findUserIfExist(user.getResourceManagerId());
        Set<User> subordinatesBefore = userRepository.findAllByResourceManagerId(userToMoveId);
        Department departmentBefore = validationUtil.findDepartmentIfExist(user.getDepartmentId());
        Department departmentAfter = validationUtil.findDepartmentIfExist(destinationDepartmentId);

        ChangeUserDepartmentCommand command = new ChangeUserDepartmentCommand(destinationDepartmentId);
        HttpEntity<ChangeUserDepartmentCommand> httpEntity = new HttpEntity<>(command,
                netUtil.createHttpHeaders(ADMIN_ID));
        URI changeDepartmentUri = changeDepartmentUriComponentBuilder.build(userToMoveId);

        // WHEN
        ResponseEntity<SuccessResponse> response = restTemplate.exchange(changeDepartmentUri,
                HttpMethod.PATCH, httpEntity, SuccessResponse.class);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        user = validationUtil.findUserIfExist(userToMoveId);
        assertThat(user.getDepartmentId()).isEqualTo(destinationDepartmentId);
        assertThat(user.getResourceManagerId()).isEqualTo(departmentAfter.getHeadId());

        Set<User> subordinatesAfter = userRepository.findAllByResourceManagerId(resourceManagerBefore.getId());
        assertThat(subordinatesAfter)
                .containsAll(subordinatesBefore)
                .allMatch(s -> s.getDepartmentId().equals(departmentBefore.getId()));
    }
}


