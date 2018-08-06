package com.gs.smsservice.commands;

import com.gs.smsservice.execeptions.UnknownCommandException;
import com.gs.smsservice.gateways.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Component
public class BalanceCommand implements Command {

    public final static String COMMAND_REGEX = "^(BALANCE)";

    private UserManager userManager;

    public BalanceCommand(final UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public String execute(final String smsContent, final String deviceId) {
        validateSmsContent(smsContent);

        final String username = userManager.getUserNameForDeviceId(deviceId);
        final BigDecimal balance = getUserBalance(username);

        return balance.toString();
    }

    private void validateSmsContent(final String smsContent) {
        if (!smsContent.matches(COMMAND_REGEX)) {
            throw new UnknownCommandException(smsContent);
        }
    }

    private BigDecimal getUserBalance(final String username) {
        log.info("Getting user {} balance", username);
        return Optional.ofNullable(userManager.getBalance(username))
                .orElse(BigDecimal.ZERO);
    }
}
