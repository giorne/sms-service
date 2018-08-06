package com.gs.smsservice.commands;

import com.gs.smsservice.gateways.TransferManager;
import com.gs.smsservice.gateways.UserManager;
import com.gs.smsservice.execeptions.UnknownCommandException;
import com.gs.smsservice.execeptions.UserHasNoFundsException;
import com.gs.smsservice.execeptions.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class SendCommandTest {

    @Mock
    private UserManager userManager;

    @Mock
    private TransferManager transferManager;

    private SendCommand command;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        command = new SendCommand(userManager, transferManager);
    }

    @Test
    public void should_send_money_when_recipient_user_exists_and_sender_user_has_funds() {
        // given a valid sms content and deviceId and recipient user
        final String smsContent = "SEND-100-GIORNE";
        final String deviceId = "deviceId";
        final BigDecimal userBalance = BigDecimal.valueOf(200);
        final String expectedResponse = "OK";

        // and valid gateways
        when(userManager.getUserNameForDeviceId(anyString())).thenReturn("Flitz");
        when(userManager.getBalance(anyString())).thenReturn(userBalance);
        when(userManager.existsUser("GIORNE")).thenReturn(true);

        // when I execute the command
        final String result = command.execute(smsContent, deviceId);

        // a valid value should be return
        assertEquals(expectedResponse, result);
    }

    @Test(expected = UserNotFoundException.class)
    public void should_not_send_money_when_recipient_user_is_not_found() {
        // given a valid sms content and deviceId
        final String smsContent = "SEND-100-GIORNE";
        final String deviceId = "deviceId";
        final BigDecimal userBalance = BigDecimal.valueOf(200);

        // and valid gateways
        when(userManager.getUserNameForDeviceId(anyString())).thenReturn("Flitz");
        when(userManager.getBalance(anyString())).thenReturn(userBalance);
        when(userManager.existsUser("GIORNE")).thenReturn(false);

        // when I execute the command
        command.execute(smsContent, deviceId);
    }

    @Test(expected = UserHasNoFundsException.class)
    public void should_not_send_money_when_sender_user_has_no_funds() {
        // given a valid sms content and deviceId
        final String smsContent = "SEND-100-GIORNE";
        final String deviceId = "deviceId";
        final BigDecimal userBalance = BigDecimal.valueOf(80);

        // and valid gateways
        when(userManager.getUserNameForDeviceId(anyString())).thenReturn("Flitz");
        when(userManager.getBalance(anyString())).thenReturn(userBalance);
        when(userManager.existsUser("GIORNE")).thenReturn(true);

        // when I execute the command
        command.execute(smsContent, deviceId);
    }

    @Test(expected = UnknownCommandException.class)
    public void should_throw_an_exception_when_the_sms_content_is_invalid() {
        // given a sms content and deviceId
        final String smsContent = "SEND-100-GIORN-E";
        final String deviceId = "deviceId";

        // when I execute the command
        command.execute(smsContent, deviceId);
    }


}
