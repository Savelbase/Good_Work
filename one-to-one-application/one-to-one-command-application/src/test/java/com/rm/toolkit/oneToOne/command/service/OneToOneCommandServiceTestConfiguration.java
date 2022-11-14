package com.rm.toolkit.oneToOne.command.service;

import com.rm.toolkit.oneToOne.command.message.CommandPublisher;
import com.rm.toolkit.oneToOne.command.message.EventPublisher;
import com.rm.toolkit.oneToOne.command.message.projector.OneToOneProjector;
import com.rm.toolkit.oneToOne.command.repository.OneToOneRepository;
import com.rm.toolkit.oneToOne.command.util.CommandUtil;
import com.rm.toolkit.oneToOne.command.util.EventUtil;
import com.rm.toolkit.oneToOne.command.util.OneToOneUtil;
import com.rm.toolkit.oneToOne.command.util.UserUtil;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class OneToOneCommandServiceTestConfiguration {
    @Bean
    public OneToOneCommandService oneToOneCommandService(
            OneToOneRepository oneToOneRepository,
            OneToOneUtil oneToOneUtil,
            EventUtil eventUtil,
            UserUtil userUtil,
            CommandUtil commandUtil,
            EventPublisher eventPublisher,
            CommandPublisher commandPublisher,
            OneToOneProjector oneToOneProjector
    ) {
        return new OneToOneCommandService(oneToOneRepository, oneToOneUtil, eventUtil, userUtil,
                commandUtil, eventPublisher, commandPublisher, oneToOneProjector);
    }
}
