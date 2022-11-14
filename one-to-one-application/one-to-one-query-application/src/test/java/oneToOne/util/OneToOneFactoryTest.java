//package oneToOne.util;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.test.context.ActiveProfiles;
//import rmt.mobile.toolkit.oneToOne.query.dto.request.CreateOneToOneRequest;
//import rmt.mobile.toolkit.oneToOne.query.model.OneToOne;
//import rmt.mobile.toolkit.oneToOne.query.util.OneToOneFactory;
//import rmt.mobile.toolkit.oneToOne.query.util.UuidProvider;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.ZoneId;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9093", "port=9093"})
//@ActiveProfiles("test")
//@SpringBootTest
//class OneToOneFactoryTest {
//
//    @Autowired
//    private OneToOneFactory factory;
//
//    @Test
//    public void oneToOneFactoryShouldCreateMeeting() {
//
//        CreateOneToOneRequest request = new CreateOneToOneRequest("11", "1",
//                "1", LocalDate.of(2999, 9, 9), LocalTime.of(9, 9));
//
//        OneToOne actualOneToOne = factory.create(request);
//
//        UuidProvider fakeProvider = new UuidProvider.Fake();
//
//        actualOneToOne.setId(fakeProvider.uuid());
//
//        OneToOne expectedOneToOne = new OneToOne();
//        expectedOneToOne.setId(fakeProvider.uuid());
//        expectedOneToOne.setUserId(request.getUserId());
//        expectedOneToOne.setResourceManagerId(request.getResourceManagerId());
//        expectedOneToOne.setComment(request.getComment());
//        expectedOneToOne.setIsOver(false);
//        expectedOneToOne.setDeleted(false);
//        LocalDateTime expectedOneToOneDateAndTime = LocalDateTime.of(request.getDate(), request.getTime());
//        ZoneId zoneId = ZoneId.of("UTC");
//        expectedOneToOne.setDateTime(expectedOneToOneDateAndTime.atZone(zoneId));
//
//        assertEquals(expectedOneToOne, actualOneToOne);
//    }
//
//}