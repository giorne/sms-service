package com.gs.smsservice.handler;

public interface SmsHandler {

    String handleSmsRequest(String smsContent, String senderDeviceId);
}
