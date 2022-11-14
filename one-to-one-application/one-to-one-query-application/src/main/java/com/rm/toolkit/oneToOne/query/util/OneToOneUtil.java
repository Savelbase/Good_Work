package com.rm.toolkit.oneToOne.query.util;

import com.rm.toolkit.oneToOne.query.dto.response.OneToOneResponse;
import com.rm.toolkit.oneToOne.query.exception.OneToOneForRMNotFoundException;
import com.rm.toolkit.oneToOne.query.exception.OneToOneForUserNotFoundException;
import com.rm.toolkit.oneToOne.query.exception.OneToOneNotFoundException;
import com.rm.toolkit.oneToOne.query.model.OneToOne;
import com.rm.toolkit.oneToOne.query.model.User;
import com.rm.toolkit.oneToOne.query.repository.OneToOneRepository;
import com.rm.toolkit.oneToOne.query.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Component
public class OneToOneUtil {

    private final OneToOneRepository oneToOneRepository;
    private final UserRepository userRepository;
    private static final String UKNOWN = "UKNOWN";

    public OneToOne findByIdOrThrowException(String oneToOneId) {
        return oneToOneRepository.findById(oneToOneId).orElseThrow(() -> {
            log.error("121 встреча с id: " + oneToOneId + " не найдена");
            return new OneToOneNotFoundException(oneToOneId);
        });
    }

    public void checkExistsByUserId(String userId) {
        if (!oneToOneRepository.existsByUserId(userId)) {
            log.error("Для пользователя с id: %s встреч не существует" + userId);
            throw new OneToOneForUserNotFoundException(userId);
        }
    }

    public void checkExistsByRecourseManagerId(String rmId) {
        if (!oneToOneRepository.existsByResourceManagerId(rmId)) {
            log.error("Для ресурсного менеджера с id: %s встреч не существует" + rmId);
            throw new OneToOneForRMNotFoundException(rmId);
        }
    }

    /**
     * Преобразует страницы с моделями в список страниц DTO для 1-2-1 встреч
     *
     * @param oneToOnes список моделей встреч
     * @throws com.rm.toolkit.oneToOne.query.exception.UserNotFoundException пользователь с таким id, указанным во встрече, не найден
     */
    public Page<OneToOneResponse> toOneToOneResponsePage(Page<OneToOne> oneToOnes) {
        Set<String> usersId = oneToOnes.stream()
                .flatMap(oneToOne -> Stream.of(oneToOne.getUserId(), oneToOne.getResourceManagerId()))
                .collect(Collectors.toSet());

        Map<String, User> users = usersId.stream().collect(Collectors.toMap(
                Function.identity(),
                this::getTrueOrMockUser
        ));

        return oneToOnes.map(oneToOne -> new OneToOneResponse(
                oneToOne,
                users.get(oneToOne.getUserId()),
                users.get(oneToOne.getResourceManagerId())
        ));
    }

    /**
     * Преобразует модель в DTO для 1-2-1 встреч
     *
     * @param oneToOne модель встречи
     * @throws com.rm.toolkit.oneToOne.query.exception.UserNotFoundException пользователь с таким id, указанным во встрече, не найден
     */
    public OneToOneResponse getOneToOneResponse(OneToOne oneToOne) {
        String userId = oneToOne.getUserId();
        String managerId = oneToOne.getResourceManagerId();

        return new OneToOneResponse(oneToOne, getTrueOrMockUser(userId), getTrueOrMockUser(managerId));
    }

    private User getTrueOrMockUser(String userId) {
        return userRepository.findById(userId).orElseGet(
                () -> {
                    log.info("Пользователь с id {} не найден", userId);
                    return User.builder().id(userId).firstName(UKNOWN).lastName(UKNOWN).build();
                }
        );
    }
}
