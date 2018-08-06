package com.gs.smsservice.execeptions;

public class UnknownCommandException extends RuntimeException {
    public UnknownCommandException(String msg) {
        super(msg);
    }
}
