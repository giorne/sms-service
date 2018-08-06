package com.gs.smsservice.handler;

import com.gs.smsservice.commands.Command;
import com.gs.smsservice.commands.factory.CommandFactory;
import com.gs.smsservice.constants.ResponseMessages;
import com.gs.smsservice.execeptions.UnknownCommandException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class SmsHandlerTest {

    @Mock
    private Command command;

    @Mock
    private CommandFactory commandFactory;

    private SmsHandler smsHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        smsHandler = new SmsHandlerImpl(commandFactory);
    }

    @Test
    public void should_execute_the_command_successfully() {
        // given a valid sms content and a deviceId
        final String smsContent = "SEND-100-GIORNE";
        final String deviceId = "deviceId";

        // and valid gateways
        when(command.execute(anyString(), anyString())).thenReturn(ResponseMessages.SUCCESS);
        when(commandFactory.getCommand(anyString())).thenReturn(command);

        // when I execute the command
        final String result = smsHandler.handleSmsRequest(smsContent, deviceId);

        // the result should be as expected
        assertEquals(ResponseMessages.SUCCESS, result);
    }

    @Test
    public void should_return_an_unknown_command_when_sms_content_is_invalid() {
        // given an invalid sms content and a deviceId
        final String smsContent = "invalid";
        final String deviceId = "deviceId";

        // and valid gateways
        when(commandFactory.getCommand(anyString())).thenThrow(new UnknownCommandException(smsContent));

        // when I execute the command
        final String result = smsHandler.handleSmsRequest(smsContent, deviceId);

        // the result should be as expected
        assertEquals(ResponseMessages.UNKNOWN_COMMAND, result);
    }

    @Test
    public void should_return_an_unknown_command_when_sms_content_is_null() {
        // given an invalid sms content and a deviceId
        final String smsContent = null;
        final String deviceId = "deviceId";

        // and valid gateways
        when(commandFactory.getCommand(smsContent)).thenThrow(new IllegalArgumentException(smsContent));

        // when I execute the command
        final String result = smsHandler.handleSmsRequest(smsContent, deviceId);

        // the result should be as expected
        assertEquals(ResponseMessages.UNKNOWN_COMMAND, result);
    }

}
