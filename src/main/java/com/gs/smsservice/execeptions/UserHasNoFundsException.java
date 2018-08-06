package com.gs.smsservice.execeptions;

public class UserHasNoFundsException extends RuntimeException {
    public UserHasNoFundsException(String msg) {
        super(msg);
    }
}
