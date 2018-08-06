package com.gs.smsservice.commands;

public interface Command {

    String execute(String smsContent, String deviceId);

}
