package com.gs.smsservice.commands.factory;

import com.gs.smsservice.commands.*;
import com.gs.smsservice.execeptions.UnknownCommandException;
import com.gs.smsservice.gateways.TransferManager;
import com.gs.smsservice.gateways.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.springframework.util.StringUtils.isEmpty;

@Component
public class CommandFactory {

    @Autowired
    private UserManager userManager;

    @Autowired
    private TransferManager transferManager;

    private Map<String, Supplier<Command>> commandByRegex = new HashMap<String, Supplier<Command>>() {{
        put(BalanceCommand.COMMAND_REGEX, ()  -> new BalanceCommand(userManager));
        put(SendCommand.COMMAND_REGEX, ()  -> new SendCommand(userManager, transferManager));
        put(TotalSentCommand.COMMAND_REGEX, ()  -> new TotalSentCommand(userManager, transferManager));
    }};

    public Command getCommand(final String smsContent) {

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
