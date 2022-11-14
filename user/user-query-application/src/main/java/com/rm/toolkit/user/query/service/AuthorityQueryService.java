package com.rm.toolkit.user.query.service;

import com.rm.toolkit.user.query.security.AuthorityType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorityQueryService {

    @Transactional(readOnly = true)
    public AuthorityType[] getAllAuthorities() {
        return AuthorityType.values();
    }
}
