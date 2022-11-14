package com.rm.toolkit.feedbackcommandapplication.message;

import com.rm.toolkit.feedbackcommandapplication.command.EmailCommand;
import com.rm.toolkit.feedbackcommandapplication.command.UnsentCommand;
import com.rm.toolkit.feedbackcommandapplication.repository.CommandRepository;
import com.rm.toolkit.feedbackcommandapplication.repository.UnsentCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
public class CommandPublisher {

    private final CommandSender commandSender;
    private final CommandRepository commandRepository;
    private final UnsentCommandRepository unsentCommandRepository;

    /**
     * Метод пытается отправить первую команду. Если отправка была успешной, то продолжает отправлять до конца.
     * Если отправка первого сообщения не была успешной, то кидается ошибка, чтобы её увидел пользователь.
     * Если первое сообщений отправилось успешно, но потом где-то в середине возникла ошибка, то оставшиеся команды добавляются в очередь неотправленных команд. Это так называемый Outbox pattern.
     * Перед отправкой первой команды метод проверяет, нет ли неотравленных команд, и если есть, то сначала пытается отправить их.
     *
     * @param command Команда, которую нужно отправить
     * @throws RuntimeException Команду не удалось отправить
     */
    @Transactional
    public void publish(EmailCommand command) {
        try {
                publishAllUnsentCommands();
                commandSender.send(command);
                commandRepository.save(command);
        } catch (RuntimeException ex) {
            commandRepository.save(command);
            unsentCommandRepository.save(UnsentCommand.builder()
                    .id(UUID.randomUUID().toString())
                    .commandId(command.getId()).build()
            );
        }
    }

    /**
     * Раз в 1 минуту отправляет неотравленные команды, если они есть
     */
    @Scheduled(fixedDelay = 60000)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void publishAllUnsentCommands() {
        for (UnsentCommand unsentCommand : unsentCommandRepository.findAll()) {
            EmailCommand command = commandRepository.findById(unsentCommand.getCommandId()).orElseThrow();
            commandSender.send(command);
            unsentCommandRepository.delete(unsentCommand);

            log.info("Отправлена неотправленная команда с id {}", unsentCommand.getCommandId());
        }
    }
}