//package oneToOne.service;
//
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//import rmt.mobile.toolkit.oneToOne.query.dto.request.CreateOneToOneRequest;
//import rmt.mobile.toolkit.oneToOne.query.exception.InappropriateTimeException;
//import rmt.mobile.toolkit.oneToOne.query.exception.OneToOneForUserNotFoundException;
//import rmt.mobile.toolkit.oneToOne.query.exception.OneToOneNotFoundException;
//import rmt.mobile.toolkit.oneToOne.query.model.OneToOne;
//import rmt.mobile.toolkit.oneToOne.query.repository.OneToOneRepository;
//import rmt.mobile.toolkit.oneToOne.query.service.OneToOneService;
//import rmt.mobile.toolkit.oneToOne.query.util.OneToOneUtil;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.ZoneId;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@ActiveProfiles("test")
//@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
//@SpringBootTest
//public class OneToOneServiceTest {
//
//    OneToOneRepository repository;
//
//    OneToOneUtil util;
//
//    private OneToOneService service;
//
//    private final String ID_1 = "1";
//    private final String ID_2 = "2";
//    private final String WRONG_EMAIL = "email address that doesn't exist";
//
//    @BeforeAll
//    @Transactional
//    public void populateDatabaseWithTestData() {
//        OneToOne past121 = new OneToOne();
//        past121.setId("1");
//        past121.setUserId("1");
//        past121.setResourceManagerId("1");
//        past121.setComment("1");
//        past121.setIsOver(true);
//        past121.setDeleted(false);
//        LocalDateTime pastDateAndTime = LocalDateTime.of(LocalDate.of(1999, 9, 9),
//                LocalTime.of(9, 9));
//        ZoneId zoneId = ZoneId.of("UTC");
//        past121.setDateTime(pastDateAndTime.atZone(zoneId));
//
//        OneToOne future121 = new OneToOne();
//        future121.setId("2");
//        future121.setUserId("2");
//        future121.setResourceManagerId("2");
//        future121.setComment("2");
//        future121.setIsOver(false);
//        future121.setDeleted(false);
//        LocalDateTime futureDateAndTime = LocalDateTime.of(LocalDate.of(1999, 9, 9),
//                LocalTime.of(9, 9));
//        future121.setDateTime(futureDateAndTime.atZone(zoneId));
//
//        repository.save(past121);
//        repository.save(future121);
//    }
//
//    @Test
//    public void createMethod() {
//
//        CreateOneToOneRequest request = new CreateOneToOneRequest("11", "1",
//                "1", LocalDate.of(2999, 9, 9), LocalTime.of(9, 9));
//
//        service.createOneToOne(request);
//
//        assertTrue(repository.existsByUserId(request.getUserId()));
//    }
//
//    @Test
//    public void shouldThrowInappropriateTimeExceptionWhenTimeHasPassed() {
//
//        CreateOneToOneRequest request = new CreateOneToOneRequest("11", "1",
//                "1", LocalDate.of(1999, 9, 9), LocalTime.of(9, 9));
//
//        Throwable exception = assertThrows(InappropriateTimeException.class, () -> {
//            util.checkTimeNotPassed(LocalDateTime.of(request.getDate(), request.getTime()));
//
//        });
//        assertTrue(exception.getMessage().contains(request.getTime().toString()));
//    }
//
//    @Test
//    public void getOneToOne() {
//        assertEquals(service.getOneToOne(ID_1).getOneToOneId(), ID_1);
//    }
//
//    @Test
//    public void itShouldThrowOneToOneNotFoundException() {
//
//        Throwable exception = assertThrows(OneToOneNotFoundException.class, () -> {
//            util.findByIdOrThrowException(WRONG_EMAIL);
//        });
//
//        assertTrue(exception.getMessage().contains(WRONG_EMAIL));
//    }
//
//    @Test
//    public void updateOneToOne() {
//
//        CreateOneToOneRequest request = new CreateOneToOneRequest("1", "1",
//                "1", LocalDate.of(3999, 9, 9), LocalTime.of(9, 9));
//
//        service.updateOneToOne(ID_1, request);
//
//        assertEquals(repository.findById(ID_1).get().getDateTime().getYear(), request.getDate().getYear());
//    }
//
//    @Test
//    public void completeOneToOne() {
//
//        service.completeOneToOne(ID_2);
//        assertTrue(repository.findById(ID_2).get().getIsOver());
//    }
//
//    @Test
//    public void getFinishedOneToOne() {
//
//        assertTrue(service.getFinishedOneToOne(ID_1, 0, 1)
//                .getContent().contains(repository.findById(ID_1).get()));
//    }
//
//    @Test
//    public void itShouldThrowOneToOneForUserNotFoundException() {
//
//        Throwable exception = assertThrows(OneToOneForUserNotFoundException.class, () -> {
//            util.checkExistsByUserId(WRONG_EMAIL);
//        });
//
//        assertTrue(exception.getMessage().contains(WRONG_EMAIL));
//    }
//
//    @Test
//    public void getScheduledOneToOne() {
//
//        assertTrue(service.getScheduledOneToOne(ID_2, 0, 1)
//                .getContent().contains(repository.findById(ID_2).get()));
//    }
//
//    @Test
//
//    public void getCompletedOneToOneByRM() {
//
//        assertNotNull(service.getCompletedOneToOneByRM(ID_1, 0, 1).getContent());
//    }
//
//    @Test
//    public void getAppointedOneToOneByRM() {
//
//        assertTrue(service.getAppointedOneToOneByRM(ID_2, 0, 1)
//                .getContent().contains(repository.findById(ID_2).get()));
//    }
//
//    @Test
//    public void deleteOneToOne() {
//
//        service.deleteOneToOne(ID_1);
//        assertEquals(repository.findById(ID_1), Optional.empty());
//    }
//
//}
