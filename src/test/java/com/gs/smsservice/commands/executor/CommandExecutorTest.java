package com.gs.smsservice.commands.executor;

import com.gs.smsservice.commands.Command;
import com.gs.smsservice.commands.executor.CommandExecutor;
import com.gs.smsservice.execeptions.UserHasNoFundsException;
import com.gs.smsservice.execeptions.UserNotFoundException;
import com.gs.smsservice.execeptions.UnknownCommandException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class CommandExecutorTest {

    @Mock
    private Command command;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_return_a_success_response() {
        // given a valid sms content and deviceId
        final String smsContent = "SEND-100-GIORNE";
        final String deviceId = "deviceId";
        final String expectedResult = "OK";

        // and valid mocked command
        when(command.execute(smsContent, deviceId)).thenReturn(expectedResult);

        // when I execute command
        final String result  = new CommandExecutor(smsContent, deviceId).execute(command);

        // the result should be as expected
        assertEquals(expectedResult, result);
    }

    @Test
    public void should_return_an_unknown_command_response() {
        // given a valid sms content and deviceId
        final String smsContent = "SEND-100-GIORNE-A";
        final String deviceId = "deviceId";
        final String expectedResult = "ERR – UNKNOWN COMMAND";

        // and valid mocked command
        when(command.execute(smsContent, deviceId)).thenThrow(new UnknownCommandException(smsContent));

        // when I execute command
        final String result  = new CommandExecutor(smsContent, deviceId).execute(command);

        // the result should be as expected
        assertEquals(expectedResult, result);
    }

    @Test
    public void should_return_an_user_not_found_response() {
        // given a valid sms content and deviceId
        final String smsContent = "SEND-100-GIORNE";
        final String deviceId = "deviceId";
        final String expectedResult = "ERR – NO USER";

        // and valid mocked command
        when(command.execute(smsContent, deviceId)).thenThrow(new UserNotFoundException("GIORNE"));

        // when I execute command
        final String result  = new CommandExecutor(smsContent, deviceId).execute(command);

        // the result should be as expected
        assertEquals(expectedResult, result);
    }

    @Test
    public void should_return_an_user_has_no_funds_response() {
        // given a valid sms content and deviceId
        final String smsContent = "SEND-100-GIORNE";
        final String deviceId = "deviceId";
        final String expectedResult = "ERR – INSUFFICIENT FUNDS";

        // and valid mocked command
        when(command.execute(smsContent, deviceId)).thenThrow(new UserHasNoFundsException("USER"));

        // when I execute command
        final String result  = new CommandExecutor(smsContent, deviceId).execute(command);

        // the result should be as expected
        assertEquals(expectedResult, result);
    }

}
