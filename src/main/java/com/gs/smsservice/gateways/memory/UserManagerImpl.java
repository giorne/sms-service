package com.gs.smsservice.gateways.memory;

import com.gs.smsservice.gateways.UserManager;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class UserManagerImpl implements UserManager {

    @Override
    public boolean existsUser(String username) {
        return UserMemory.getUsers().stream()
                .anyMatch(user -> user.getUsername().equalsIgnoreCase(username));
    }

    @Override
    public BigDecimal getBalance(String username) {
        return UserMemory.getUsers().stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .map(UserMemory.User::getBalance)
                .findFirst()
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public String getUserNameForDeviceId(String deviceId) {
        return UserMemory.getUsers().stream()
                .filter(user -> user.getDeviceId().equalsIgnoreCase(deviceId))
                .findFirst()
                .orElse(null)
                .getUsername();
    }

}
