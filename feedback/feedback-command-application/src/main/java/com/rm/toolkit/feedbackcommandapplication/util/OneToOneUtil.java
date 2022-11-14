package com.rm.toolkit.feedbackcommandapplication.util;

import com.rm.toolkit.feedbackcommandapplication.exception.notfound.OneToOneNotFoundException;
import com.rm.toolkit.feedbackcommandapplication.model.OneToOne;
import com.rm.toolkit.feedbackcommandapplication.repository.OneToOneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OneToOneUtil {

    private final OneToOneRepository oneToOneRepository;

    public OneToOne getCompletedOneToOne(String oneToOneId) {
        return oneToOneRepository.findCompletedOneToOneById(oneToOneId)
                .orElseThrow(() -> {
                    log.error("121 встреча с id = {} не найдена, или еще не завершена, или удалена", oneToOneId);
                    throw new OneToOneNotFoundException(oneToOneId);
                });
    }
}
