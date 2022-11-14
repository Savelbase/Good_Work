package com.rm.toolkit.oneToOne.query.service;

import com.rm.toolkit.oneToOne.query.dto.response.OneToOneInfoResponse;
import com.rm.toolkit.oneToOne.query.exception.OneToOneNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import com.rm.toolkit.oneToOne.query.model.OneToOne;
import com.rm.toolkit.oneToOne.query.repository.OneToOneRepository;
import com.rm.toolkit.oneToOne.query.util.OneToOneUtil;

@Deprecated
@Slf4j
@RequiredArgsConstructor
@Service
public class OneToOneQueryService {

    private final OneToOneRepository oneToOneRepository;
    private final OneToOneUtil oneToOneUtil;

    @Transactional(readOnly = true)
    public Page<OneToOne> getFinishedOneToOne(String userId, int page, int size) {

        oneToOneUtil.checkExistsByUserId(userId);
        Pageable pageable = PageRequest.of(page, size,Sort.by("dateTime"));

        return oneToOneRepository.findAllByUserIdAndIsOverTrueAndDeletedFalse(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<OneToOne> getScheduledOneToOne(String userId, int page, int size) {

        oneToOneUtil.checkExistsByUserId(userId);
        Pageable pageable = PageRequest.of(page, size,Sort.by("dateTime"));

        return oneToOneRepository.findAllByUserIdAndIsOverFalseAndDeletedFalse(userId, pageable);
    }

    @Transactional(readOnly = true)
    public OneToOne getOneToOne(String oneToOneId) {
        return oneToOneRepository.findById(oneToOneId)
                .orElseThrow(() -> {
                    log.info("121 с id {} не найден", oneToOneId);
                    throw new OneToOneNotFoundException(oneToOneId);
                });
    }

    @Transactional(readOnly = true)
    public Page<OneToOne> getCompletedOneToOneByRM(String rmId, int page, int size) {

        oneToOneUtil.checkExistsByRecourseManagerId(rmId);
        Pageable pageable = PageRequest.of(page, size,Sort.by("dateTime"));

        return oneToOneRepository.findAllByResourceManagerIdAndIsOverTrueAndDeletedFalse
                (rmId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<OneToOne> getAppointedOneToOneByRM(String rmId, int page, int size) {

        oneToOneUtil.checkExistsByRecourseManagerId(rmId);
        Pageable pageable = PageRequest.of(page, size,Sort.by("dateTime"));

        return oneToOneRepository.findAllByResourceManagerIdAndIsOverFalseAndDeletedFalse
                (rmId, pageable);
    }
}