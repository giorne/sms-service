package com.gs.smsservice.commands;

import com.gs.smsservice.execeptions.UnknownCommandException;
import com.gs.smsservice.execeptions.UserHasNoFundsException;
import com.gs.smsservice.execeptions.UserNotFoundException;
import com.gs.smsservice.gateways.TransferManager;
import com.gs.smsservice.gateways.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

import static com.gs.smsservice.constants.ResponseMessages.SUCCESS;

@Slf4j
@Component
public class SendCommand implements Command {

    public final static String COMMAND_REGEX = "^(SEND-)[0-9]+(-[A-Z]+)";

    private final UserManager userManager;

    private final TransferManager transferManager;

    public SendCommand(final UserManager userManager, final TransferManager transferManager) {
        this.userManager = userManager;
        this.transferManager = transferManager;
    }

    @Override
    public String execute(final String smsContent, final String deviceId) {
        validateSmsContent(smsContent);

        final String senderUsername = userManager.getUserNameForDeviceId(deviceId);
        final String recipientUsername = getRecipientUsername(smsContent);
        final BigDecimal amount = getAmount(smsContent);

        checkUser(recipientUsername);
        checkFunds(senderUsername, amount);

        transferManager.sendMoney(senderUsername, recipientUsername, amount);

        return SUCCESS;
    }

    private void validateSmsContent(final String smsContent) {
        if (!smsContent.matches(COMMAND_REGEX)) {
            throw new UnknownCommandException(smsContent);
        }
    }

    private BigDecimal getAmount(final String smsContent) {
        return new BigDecimal(smsContent.split("-")[1]);
    }

    private String getRecipientUsername(final String smsContent) {
        return smsContent.split("-")[2];
    }

    private void checkFunds(final String username, final BigDecimal amount) {
        log.info("Checking sender user {} funds", username);
        if (!hasFunds(username, amount)) {
            throw new UserHasNoFundsException(username);
        }
    }

    private boolean hasFunds(final String username, final BigDecimal amount) {
        final BigDecimal balance = getUserBalance(username);
        return balance.intValue() >= amount.intValue();
    }

    private BigDecimal getUserBalance(final String username) {
        log.info("Getting sender user {} balance", username);
        return Optional.ofNullable(userManager.getBalance(username))
                .orElse(BigDecimal.ZERO);
    }

    private void checkUser(final String username) {
        log.info("Checking recipient user {}", username);
        if (!userManager.existsUser(username)) {
            throw new UserNotFoundException(username);
        }
    }

}
