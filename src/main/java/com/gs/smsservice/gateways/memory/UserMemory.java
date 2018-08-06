package com.gs.smsservice.gateways.memory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserMemory {

    private static List<User> users;

    static {
        users = new ArrayList<>();
    }

    public static List<User> getUsers() {
        return users;
    }

    public static void newUser(final User user) {
        user.transactions = new ArrayList<>();
        users.add(user);
    }

    public static List<Transaction> getTransactionByUser(final String username) {
        return users.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .findFirst().get()
                .getTransactions();
    }

    public static User getUser(final String username) {
        return users.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .get();
    }

    public static BigDecimal getBalance(final String username) {
        return users.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .map(UserMemory.User::getBalance)
                .findFirst()
                .orElse(BigDecimal.ZERO);
    }

    public static boolean existsUser(final String username) {
        return users.stream()
                .anyMatch(user -> user.getUsername().equalsIgnoreCase(username));
    }

    public static String getUsernameByDeviceId(final String deviceId) {
        return UserMemory.getUsers().stream()
                .filter(user -> user.getDeviceId().equalsIgnoreCase(deviceId))
                .findFirst()
                .orElse(null)
                .getUsername();
    }

    @Getter
    @Setter
    @Builder
    public static class User {
        private String username;
        private BigDecimal balance;
        private String deviceId;
        private List<Transaction> transactions = new ArrayList<>();

        public void addTransaction(final String recipientUser, final BigDecimal amout) {
            final Optional<Transaction> transaction = transactions.stream()
                    .filter(trans -> recipientUser.equalsIgnoreCase(trans.recipientUser))
                    .findFirst();

            if (transaction.isPresent()) {
                transaction.get().values.add(amout);
            } else {
                List<BigDecimal> value = new ArrayList<>();
                value.add(amout);
                transactions.add(new Transaction(recipientUser, value));
            }
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Transaction {
        private String recipientUser;
        private List<BigDecimal> values = new ArrayList<>();
    }



}
