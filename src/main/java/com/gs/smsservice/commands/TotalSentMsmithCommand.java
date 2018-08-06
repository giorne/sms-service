package com.gs.smsservice.commands;

import com.gs.smsservice.execeptions.UnknownCommandException;
import com.gs.smsservice.execeptions.UserNotFoundException;
import com.gs.smsservice.gateways.TransferManager;
import com.gs.smsservice.gateways.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;

import static java.util.stream.Collectors.joining;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Component
public class TotalSentMsmithCommand implements Command {

    public final static String COMMAND_REGEX = "^(TOTAL-SENT)(-[A-Z]+)(-MSMITH)";

    public final static String ZERO = "0";

    private final UserManager userManager;

    private final TransferManager transferManager;

    public TotalSentMsmithCommand(final UserManager userManager, final TransferManager transferManager) {
        this.userManager = userManager;
        this.transferManager = transferManager;
    }

    @Override
    public String execute(final String smsContent, final String deviceId) {
        validateSmsContent(smsContent);

        final String senderUsername = userManager.getUserNameForDeviceId(deviceId);
        final String recipientUsername = getRecipientUsername(smsContent);

        checkUser(recipientUsername);

        return getAllTansactionsMsmith(senderUsername, recipientUsername);
    }

    private void validateSmsContent(final String smsContent) {
        if (!smsContent.matches(COMMAND_REGEX)) {
            throw new UnknownCommandException(smsContent);
        }
    }

    private String getRecipientUsername(final String smsContent) {
        return smsContent.split("-")[2];
    }

    private void checkUser(final String username) {
        log.info("Checking user {}", username);
        if (!userManager.existsUser(username)) {
            throw new UserNotFoundException(username);
        }
    }

    private String getAllTansactionsMsmith(final String senderUsername, final String recipientUsername) {
        log.info("Getting all user {} transactions to {}", senderUsername, recipientUsername);
        final Collection<BigDecimal> transactions = transferManager.getAllTransactions(senderUsername, recipientUsername);
        if (isEmpty(transactions)) {
            return ZERO;
        }
        return transactions.stream()
                .map(Object::toString)
                .collect(joining(","));
    }


}
