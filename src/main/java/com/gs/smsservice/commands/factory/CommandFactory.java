package com.gs.smsservice.commands.factory;

import com.gs.smsservice.commands.*;
import com.gs.smsservice.execeptions.UnknownCommandException;
import com.gs.smsservice.gateways.memory.TransferManagerImpl;
import com.gs.smsservice.gateways.memory.UserManagerImpl;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.springframework.util.StringUtils.isEmpty;

@Component
public class CommandFactory {

    private final static Map<String, Supplier<Command>> commandByRegex = new HashMap<>();

    static {
        commandByRegex.put(BalanceCommand.COMMAND_REGEX, ()  -> new BalanceCommand(new UserManagerImpl()));
        commandByRegex.put(SendCommand.COMMAND_REGEX, ()  -> new SendCommand(new UserManagerImpl(), new TransferManagerImpl()));
        commandByRegex.put(TotalSentCommand.COMMAND_REGEX, ()  -> new TotalSentCommand(new UserManagerImpl(), new TransferManagerImpl()));
        commandByRegex.put(TotalSentMsmithCommand.COMMAND_REGEX, ()  -> new TotalSentMsmithCommand(new UserManagerImpl(), new TransferManagerImpl()));
    }

    public static Command getCommand(final String smsContent) {

        if (isEmpty(smsContent)) {
            throw new IllegalArgumentException("Sms content cannot be null");
        }

        return commandByRegex.entrySet().stream()
                .filter(value -> smsContent.toUpperCase().matches(value.getKey()))
                .map(value -> value.getValue())
                .findFirst()
                .orElseThrow(() -> new UnknownCommandException(smsContent))
                .get();
    }

}
