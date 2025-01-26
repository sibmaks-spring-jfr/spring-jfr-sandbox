package io.github.sibmaks.springjfr.sandbox.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class LongPostConstructService {
    @PostConstruct
    public void init() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}
