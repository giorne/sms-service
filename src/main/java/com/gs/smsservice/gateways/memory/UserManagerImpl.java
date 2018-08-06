package com.gs.smsservice.gateways.memory;

import com.gs.smsservice.gateways.UserManager;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class UserManagerImpl implements UserManager {

    @Override
    public boolean existsUser(String username) {
        return UserMemory.existsUser(username);
    }

    @Override
    public BigDecimal getBalance(String username) {
        return UserMemory.getBalance(username);
    }

    @Override
    public String getUserNameForDeviceId(String deviceId) {
        return UserMemory.getUsernameByDeviceId(deviceId);
    }

}
