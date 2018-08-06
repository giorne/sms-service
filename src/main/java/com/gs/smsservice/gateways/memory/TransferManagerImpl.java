package com.gs.smsservice.gateways.memory;

import com.gs.smsservice.gateways.TransferManager;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Component
public class TransferManagerImpl implements TransferManager {

    @Override
    public void sendMoney(String senderUsername, String recipientUsername, BigDecimal amount) {
        final UserMemory.User senderUser = UserMemory.getUsers().stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(senderUsername))
                .findFirst()
                .get();

        final UserMemory.User recipientUser = UserMemory.getUsers().stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(recipientUsername))
                .findFirst()
                .get();

        senderUser.setBalance(senderUser.getBalance().subtract(amount));
        recipientUser.setBalance(recipientUser.getBalance().add(amount));
        senderUser.addTransaction(recipientUsername, amount);
    }

    @Override
    public List<BigDecimal> getAllTransactions(String senderUsername, String recipientUsername) {
        final List<UserMemory.Transaction> transactions = UserMemory.getTransactionByUser(senderUsername);
        if (isEmpty(transactions)) {
            return new ArrayList<>();
        }
        return transactions.stream()
                .filter(transaction -> transaction.getRecipientUser().equalsIgnoreCase(recipientUsername))
                .findFirst()
                .orElse(new UserMemory.Transaction(recipientUsername, new ArrayList<>()))
                .getValues();
    }
}
