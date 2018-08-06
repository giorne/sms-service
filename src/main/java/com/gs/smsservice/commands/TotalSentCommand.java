package com.gs.smsservice.commands;

import com.gs.smsservice.execeptions.UnknownCommandException;
import com.gs.smsservice.execeptions.UserNotFoundException;
import com.gs.smsservice.gateways.TransferManager;
import com.gs.smsservice.gateways.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;

import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Component
public class TotalSentCommand implements Command {

    public final static String COMMAND_REGEX = "^(TOTAL-SENT)(-[A-Z]+)";

    private final long ZERO = 0l;

    private final UserManager userManager;

    private final TransferManager transferManager;

    public TotalSentCommand(final UserManager userManager, final TransferManager transferManager) {
        this.userManager = userManager;
        this.transferManager = transferManager;
    }

    @Override
    public String execute(final String smsContent, final String deviceId) {
        validateSmsContent(smsContent);

        final String senderUsername = userManager.getUserNameForDeviceId(deviceId);
        final String recipientUsername = getRecipientUsername(smsContent);

        checkUser(recipientUsername);

        final Long  summedTransactions = getAllTransactionsSummed(senderUsername, recipientUsername);

        return summedTransactions.toString();
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
        log.info("Checking recipient user {}", username);
        if (!userManager.existsUser(username)) {
            throw new UserNotFoundException(username);
        }
    }

    private long getAllTransactionsSummed(final String senderUsername, final String recipientUsername) {
        log.info("Getting all user {} transactions to {}", senderUsername, recipientUsername);
        final Collection<BigDecimal> transactions = transferManager.getAllTransactions(senderUsername, recipientUsername);
        if (isEmpty(transactions)) {
            return ZERO;
        }
        return transactions.stream()
                .mapToLong(BigDecimal::intValue)
                .sum();
    }

}
