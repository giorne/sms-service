package com.gs.smsservice.commands;

import com.gs.smsservice.gateways.UserManager;
import com.gs.smsservice.execeptions.UnknownCommandException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class BalanceCommandTest {

    @Mock
    private UserManager userManager;

    private BalanceCommand command;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        command = new BalanceCommand(userManager);
    }

    @Test
    public void should_get_result_when_the_device_has_a_valid_user() {
        // given a valid sms content and deviceId
        final String smsContent = "BALANCE";
        final String deviceId = "deviceId";
        final BigDecimal expectedBalance = BigDecimal.valueOf(100);

        // and valid gateways
        when(userManager.getUserNameForDeviceId(anyString())).thenReturn("Flitz");
        when(userManager.getBalance(anyString())).thenReturn(expectedBalance);

        // when I execute the command
        final String returnedBalance = command.execute(smsContent, deviceId);

        // a valid value should be return
        assertEquals(expectedBalance.toString(), returnedBalance);
    }

    @Test
    public void should_get_zero_as_result_when_balance_service_returns_null() {
        // given a valid sms content and deviceId
        final String smsContent = "BALANCE";
        final String deviceId = "deviceId";
        final BigDecimal expectedBalance = BigDecimal.ZERO;

        // and valid gateways
        when(userManager.getUserNameForDeviceId(anyString())).thenReturn("Flitz");
        when(userManager.getBalance(anyString())).thenReturn(null);

        // when I execute the command
        final String returnedBalance = command.execute(smsContent, deviceId);

        // a valid value should be return
        assertEquals(expectedBalance.toString(), returnedBalance);
    }

    @Test(expected = UnknownCommandException.class)
    public void should_throw_an_exception_when_the_sms_content_is_invalid() {
        // given a sms content and deviceId
        final String smsContent = "invalid-command";
        final String deviceId = "deviceId";

        // when I execute the command
        command.execute(smsContent, deviceId);

    }


}
