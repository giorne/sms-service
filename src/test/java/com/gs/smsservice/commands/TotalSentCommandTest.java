package com.gs.smsservice.commands;

import com.gs.smsservice.gateways.UserManager;
import com.gs.smsservice.execeptions.UnknownCommandException;
import com.gs.smsservice.execeptions.UserNotFoundException;
import com.gs.smsservice.gateways.TransferManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class TotalSentCommandTest {

    @Mock
    private UserManager userManager;

    @Mock
    private TransferManager transferManager;

    private TotalSentCommand command;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        command = new TotalSentCommand(userManager, transferManager);
    }

    @Test
    public void should_get_result_summed_when_recipient_user_exists_and_there_are_transactions() {
        // given a valid sms content and deviceId
        final String smsContent = "TOTAL-SENT-GIORNE";
        final String deviceId = "deviceId";
        final String expectedResult = "150";
        final List<BigDecimal> userTransactions = Arrays.asList(BigDecimal.valueOf(100), BigDecimal.valueOf(50));


        // and valid gateways
        when(userManager.getUserNameForDeviceId(anyString())).thenReturn("Flitz");
        when(userManager.existsUser("GIORNE")).thenReturn(true);
        when(transferManager.getAllTransactions(anyString(), anyString())).thenReturn(userTransactions);

        // when I execute the command
        final String result = command.execute(smsContent, deviceId);

        // a valid value should be return
        assertEquals(result, expectedResult);
    }

    @Test(expected = UserNotFoundException.class)
    public void should_not_get_result_when_recipient_user_is_not_found() {
        // given a valid sms content and deviceId
        final String smsContent = "TOTAL-SENT-GIORNE";
        final String deviceId = "deviceId";

        // and valid gateways
        when(userManager.getUserNameForDeviceId(anyString())).thenReturn("Flitz");
        when(userManager.existsUser("GIORNE")).thenReturn(false);

        // when I execute the command
        command.execute(smsContent, deviceId);
    }

    @Test
    public void should_get_zero_as_result_when_there_are_no_transactions() {
        // given a valid sms content and deviceId
        final String smsContent = "TOTAL-SENT-GIORNE";
        final String deviceId = "deviceId";
        final String expectedResult = "0";

        // and valid gateways
        when(userManager.getUserNameForDeviceId(anyString())).thenReturn("Flitz");
        when(userManager.existsUser("GIORNE")).thenReturn(true);

        // when I execute the command
        final String result = command.execute(smsContent, deviceId);

        // zero should be return
        assertEquals(result, expectedResult);
    }

    @Test(expected = UnknownCommandException.class)
    public void should_throw_an_exception_when_the_sms_content_is_invalid() {
        // given a sms content and deviceId
        final String smsContent = "TOTAL_SENT-GIORNE";
        final String deviceId = "deviceId";

        // when I execute the command
        command.execute(smsContent, deviceId);
    }


}
