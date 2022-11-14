//package oneToOne.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.apache.http.auth.BasicUserPrincipal;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.http.MediaType;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import rmt.mobile.toolkit.oneToOne.query.dto.request.CreateOneToOneRequest;
//import rmt.mobile.toolkit.oneToOne.query.exception.InappropriateTimeException;
//import rmt.mobile.toolkit.oneToOne.query.exception.OneToOneNotFoundException;
//import rmt.mobile.toolkit.oneToOne.query.model.OneToOne;
//import rmt.mobile.toolkit.oneToOne.query.security.AuthorityType;
//import rmt.mobile.toolkit.oneToOne.query.security.jwt.CustomUser;
//import rmt.mobile.toolkit.oneToOne.query.service.OneToOneService;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.doThrow;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ActiveProfiles("test")
//@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9093", "port=9093"})
//@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
//@SpringBootTest
//@AutoConfigureMockMvc()
//class OneToOneControllerTest {
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private OneToOneService service;
//
//    private static final String CREATE_PATH = "/api/121/";
//    private static final String GET_121_PATH = "/api/121/{oneToOneId}";
//    private static final String UPDATE_PATH = "/api/121/{oneToOneId}";
//    private static final String COMPLETE_121_PATH = "/api/121/complete/{oneToOneId}";
//    private static final String FINISHED_121_PATH = "/api/121/finished/{oneToOneId}";
//    private static final String SCHEDULED_121_PATH = "/api/121/scheduled/{oneToOneId}";
//    private static final String DELETE_121_PATH = "/api/121/{oneToOneId}";
//    private static final String COMPLETED_121_FOR_RM_PATH = "/api/121/completed-by-rm/{rmId}";
//    private static final String APPOINTED_121_FOR_RM_PATH = "/api/121/appointed-by-rm/{rmId}";
//    private static final String ID = "1";
//
//    @Test
//    public void createMethodShouldReturnStatusIsOk() throws Exception {
//
//        CreateOneToOneRequest request = new CreateOneToOneRequest("11", "1",
//                "1", LocalDate.of(2999, 9, 9), LocalTime.of(9, 9));
//
//        List<String> authority = new ArrayList<>();
//        authority.add(AuthorityType.ONE_TO_ONE.name());
//
//        CustomUser user = new CustomUser("1", authority );
//
//        mvc.perform(post(CREATE_PATH)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper()
//                                .registerModule(new JavaTimeModule())
//                                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//                                .writeValueAsString(request))
//                        .principal(new BasicUserPrincipal("MikeFromBerlin")))
//                .andExpect(status().isOk())
//                .andExpect(content()
//                        .contentType(MediaType.APPLICATION_JSON));
//    }
//    @Test
//    public void createMethodShouldReturnErrorResponse() throws Exception {
//
//        CreateOneToOneRequest wrongRequest = new CreateOneToOneRequest("11", "1",
//                "1", LocalDate.of(1999, 9, 9), LocalTime.of(9, 9));
//
//        doThrow(InappropriateTimeException.class).when(service).createOneToOne(wrongRequest);
//
//        mvc.perform(post(CREATE_PATH)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper()
//                                .registerModule(new JavaTimeModule())
//                                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//                                .writeValueAsString(wrongRequest))
//                        .principal(new BasicUserPrincipal("MikeFromBerlin")))
//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InappropriateTimeException));
//    }
//
//    @Test
//    public void getOneToOneShouldReturnStatusIsOk() throws Exception {
//
//        mvc.perform(get(GET_121_PATH, ID)
//                        .principal(new BasicUserPrincipal("MikeFromBerlin")))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void getOneToOneShouldReturnErrorResponseWhen121NotFound() throws Exception {
//
//        doThrow(OneToOneNotFoundException.class).when(service).getOneToOne(ID);
//
//        mvc.perform(get(GET_121_PATH, ID)
//                        .principal(new BasicUserPrincipal("MikeFromBerlin")))
//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof OneToOneNotFoundException));
//    }
//
//    @Test
//    public void updateOneToOneShouldReturnStatusIsOk() throws Exception {
//        CreateOneToOneRequest request = new CreateOneToOneRequest("2", "1",
//                "1", LocalDate.of(3999, 9, 9), LocalTime.of(9, 9));
//
//        mvc.perform(patch(UPDATE_PATH, ID)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper()
//                                .registerModule(new JavaTimeModule())
//                                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//                                .writeValueAsString(request))
//                        .principal(new BasicUserPrincipal("MikeFromBerlin")))
//                .andExpect(status().isOk())
//                .andExpect(content()
//                        .contentType(MediaType.APPLICATION_JSON));
//    }
//
//    @Test
//    public void completeOneToOneShouldReturnStatusIsOk() throws Exception {
//
//        mvc.perform(patch(COMPLETE_121_PATH, ID)
//                        .principal(new BasicUserPrincipal("MikeFromBerlin")))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void getFinishedOneToOneShouldReturnStatusIsOk() throws Exception {
//
//        doReturn(new PageImpl<OneToOne>(new ArrayList<>())).when(service).getFinishedOneToOne(ID, 0, 1);
//        mvc.perform(get(FINISHED_121_PATH, ID)
//                        .principal(new BasicUserPrincipal("MikeFromBerlin"))
//                        .param("page", "0")
//                        .param("size", "1"))
//                .andExpect(status().isOk())
//                .andExpect(content()
//                        .contentType(MediaType.APPLICATION_JSON));
//    }
//
//    @Test
//    public void getScheduledOneToOneShouldReturnStatusIsOk() throws Exception {
//
//        doReturn(new PageImpl<OneToOne>(new ArrayList<>())).when(service).getScheduledOneToOne(ID, 0, 1);
//
//        mvc.perform(get(SCHEDULED_121_PATH, ID)
//                        .principal(new BasicUserPrincipal("MikeFromBerlin"))
//                        .param("page", "0")
//                        .param("size", "1"))
//                .andExpect(status().isOk())
//                .andExpect(content()
//                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
//    }
//
//    @Test
//    public void deleteOneToOneShouldReturnStatusIsOk() throws Exception {
//
//        mvc.perform(delete(DELETE_121_PATH, ID)
//                        .principal(new BasicUserPrincipal("MikeFromBerlin")))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void getCompletedOneToOneByRMShouldReturnStatusIsOk() throws Exception {
//
//        doReturn(new PageImpl<OneToOne>(new ArrayList<>())).when(service).getCompletedOneToOneByRM(ID, 0, 1);
//
//        mvc.perform(get(COMPLETED_121_FOR_RM_PATH, ID)
//                        .principal(new BasicUserPrincipal("MikeFromBerlin"))
//                        .param("page", "0")
//                        .param("size", "1"))
//                .andExpect(status().isOk())
//                .andExpect(content()
//                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
//    }
//
//    @Test
//    public void getAppointedOneToOneByRMShouldReturnStatusIsOk() throws Exception {
//
//        doReturn(new PageImpl<OneToOne>(new ArrayList<>())).when(service).getAppointedOneToOneByRM(ID, 0, 1);
//
//        mvc.perform(get(APPOINTED_121_FOR_RM_PATH, ID)
//                        .principal(new BasicUserPrincipal("MikeFromBerlin"))
//                        .param("page", "0")
//                        .param("size", "1"))
//                .andExpect(status().isOk())
//                .andExpect(content()
//                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
//    }
//}