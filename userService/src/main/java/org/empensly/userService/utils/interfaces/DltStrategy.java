package org.empensly.userService.utils.interfaces;

import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.BackOff;

public interface DltStrategy<T extends BackOff> {
    DeadLetterPublishingRecoverer recoverer();
    T defineBackoff();
    DefaultErrorHandler errorHandler();
}

