package com.onseju.matchingservice.producer.exception;

import org.springframework.http.HttpStatus;

import com.onseju.matchingservice.exception.BaseException;

public class MatchingEventProduceFailException extends BaseException {

    public MatchingEventProduceFailException() {
        super("체결 이벤트 발행 실패", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
