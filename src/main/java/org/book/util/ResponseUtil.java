package org.book.util;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class ResponseUtil {

    @Getter
    private Instant timestamp = Instant.now();

    @Getter
    @Setter
    private int status;

    @Getter
    @Setter
    private Object message;
}
