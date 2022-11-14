package com.rm.toolkit.oneToOne.query.service;

import com.rm.toolkit.oneToOne.query.dto.response.OneToOneResponse;
import com.rm.toolkit.oneToOne.query.exception.OneToOneNotFoundException;
import com.rm.toolkit.oneToOne.query.model.OneToOne;
import com.rm.toolkit.oneToOne.query.repository.OneToOneRepository;
import com.rm.toolkit.oneToOne.query.security.SecurityUtil;
import com.rm.toolkit.oneToOne.query.util.OneToOneUtil;
import com.rm.toolkit.oneToOne.query.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class OneToOneQueryInfoService {

    private static final String DATE_TIME_FIELD = "dateTime";
    private final OneToOneRepository oneToOneRepository;
    private final ValidationUtil validationUtil;
    private final OneToOneUtil oneToOneUtil;

    /**
     * @param userId id пользователя, для которого ищем встречи
     * @param page   индекс страницы
     * @param size   количество элементов на возвращаемой странице
     * @throws com.rm.toolkit.oneToOne.query.exception.UserNotFoundException пользователь с таким id не найден
     * @throws com.rm.toolkit.oneToOne.query.exception.NoAuthorityException  Нет прав для просмотра one-to-one встреч у других пользователей
     */
    @Transactional(readOnly = true)
    public Page<OneToOneResponse> getFinishedOneToOne(String userId, int page, int size) {
        validationUtil.findUserIfExist(userId);

        if (!Objects.equals(SecurityUtil.getCurrentUserId(), userId)) {
            validationUtil.checkAuthority();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(DATE_TIME_FIELD));
        return oneToOneUtil.toOneToOneResponsePage(
                oneToOneRepository.findAllByUserIdAndIsOverTrueAndDeletedFalse(userId, pageable)
        );
    }

    /**
     * @param userId id пользователя, для которого ищем встречи
     * @param page   индекс страницы
     * @param size   количество элементов на возвращаемой странице
     * @throws com.rm.toolkit.oneToOne.query.exception.UserNotFoundException пользователь с таким id не найден
     * @throws com.rm.toolkit.oneToOne.query.exception.NoAuthorityException  Нет прав для просмотра one-to-one встреч у других пользователей
     */
    @Transactional(readOnly = true)
    public Page<OneToOneResponse> getScheduledOneToOne(String userId, int page, int size) {
        validationUtil.findUserIfExist(userId);

        if (!Objects.equals(SecurityUtil.getCurrentUserId(), userId)) {
            validationUtil.checkAuthority();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(DATE_TIME_FIELD));
        return oneToOneUtil.toOneToOneResponsePage(
                oneToOneRepository.findAllByUserIdAndIsOverFalseAndDeletedFalse(userId, pageable)
        );
    }

    /**
     * @param oneToOneId id встречи
     * @throws com.rm.toolkit.oneToOne.query.exception.OneToOneNotFoundException встреча с таким id не найдена
     */
    @Transactional(readOnly = true)
    public OneToOneResponse getOneToOne(String oneToOneId) {
        OneToOne oneToOne = oneToOneRepository.findById(oneToOneId)
                .orElseThrow(() -> {
                    log.info("121 с id {} не найден", oneToOneId);
                    throw new OneToOneNotFoundException(oneToOneId);
                });
        return oneToOneUtil.getOneToOneResponse(oneToOne);
    }

    /**
     * @param rmId id пользователя, для которого ищем встречи
     * @param page индекс страницы
     * @param size количество элементов на возвращаемой странице
     * @throws com.rm.toolkit.oneToOne.query.exception.UserNotFoundException пользователь с таким id не найден
     */
    @Transactional(readOnly = true)
    public Page<OneToOneResponse> getCompletedOneToOneByRM(String rmId, int page, int size) {
        validationUtil.findUserIfExist(rmId);
        Pageable pageable = PageRequest.of(page, size, Sort.by(DATE_TIME_FIELD));
        return oneToOneUtil.toOneToOneResponsePage(
                oneToOneRepository.findAllByResourceManagerIdAndIsOverTrueAndDeletedFalse(rmId, pageable)
        );
    }

    /**
     * @param rmId id пользователя, для которого ищем встречи
     * @param page индекс страницы
     * @param size количество элементов на возвращаемой странице
     * @throws com.rm.toolkit.oneToOne.query.exception.UserNotFoundException пользователь с таким id не найден
     */
    @Transactional(readOnly = true)
    public Page<OneToOneResponse> getAppointedOneToOneByRM(String rmId, int page, int size) {
        validationUtil.findUserIfExist(rmId);
        Pageable pageable = PageRequest.of(page, size, Sort.by(DATE_TIME_FIELD));
        return oneToOneUtil.toOneToOneResponsePage(
                oneToOneRepository.findAllByResourceManagerIdAndIsOverFalseAndDeletedFalse(rmId, pageable)
        );
    }
}