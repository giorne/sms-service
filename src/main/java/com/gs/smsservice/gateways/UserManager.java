package com.gs.smsservice.gateways;

import java.math.BigDecimal;

public interface UserManager {

    boolean existsUser(String username);

    BigDecimal getBalance(String username);

    String getUserNameForDeviceId(String deviceId);

}
