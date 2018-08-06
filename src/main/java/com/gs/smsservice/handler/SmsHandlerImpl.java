package com.gs.smsservice.handler;

import com.gs.smsservice.commands.Command;
import com.gs.smsservice.commands.executor.CommandExecutor;
import com.gs.smsservice.commands.factory.CommandFactory;
import org.springframework.stereotype.Service;

import static com.gs.smsservice.constants.ResponseMessages.UNKNOWN_COMMAND;

@Service
public class SmsHandlerImpl implements SmsHandler {

    private CommandFactory commandFactory;

    public SmsHandlerImpl(final CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    public String handleSmsRequest(String smsContent, String senderDeviceId) {
        try {
            final Command command = commandFactory.getCommand(smsContent);
            return new CommandExecutor(smsContent, senderDeviceId).execute(command);
        } catch (RuntimeException ex) {
            return UNKNOWN_COMMAND;
        }
    }
}
