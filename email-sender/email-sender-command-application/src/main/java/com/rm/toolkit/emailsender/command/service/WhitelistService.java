package com.rm.toolkit.emailsender.command.service;

import com.rm.toolkit.emailsender.command.dto.request.WhitelistRequest;
import com.rm.toolkit.emailsender.command.exception.EmailAlreadyExistInWhitelistException;
import com.rm.toolkit.emailsender.command.model.WhitelistEmail;
import com.rm.toolkit.emailsender.command.repository.WhitelistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WhitelistService {

    private final WhitelistRepository whitelistRepository;

    /**
     * @param whitelistRequest какую почту добавить в whitelist
     * @throws EmailAlreadyExistInWhitelistException email уже есть в whitelist
     */
    @Transactional
    public void saveWhitelist(WhitelistRequest whitelistRequest) {
        if (!whitelistRepository.existsByEmail(whitelistRequest.getEmail())) {
            whitelistRepository.save(new WhitelistEmail(UUID.randomUUID().toString(), whitelistRequest.getEmail()));
        } else {
            throw new EmailAlreadyExistInWhitelistException(whitelistRequest.getEmail());
        }
    }

    @Transactional(readOnly = true)
    public Set<String> getEmailsFromWhitelist() {
        return whitelistRepository.findAllEmails();
    }
}