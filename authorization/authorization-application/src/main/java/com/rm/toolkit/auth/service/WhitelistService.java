package com.rm.toolkit.auth.service;

import com.rm.toolkit.auth.dto.request.WhitelistRequest;
import com.rm.toolkit.auth.exception.conflict.EmailAlreadyExistInWhitelistException;
import com.rm.toolkit.auth.model.WhitelistEmail;
import com.rm.toolkit.auth.repository.WhitelistRepository;
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
