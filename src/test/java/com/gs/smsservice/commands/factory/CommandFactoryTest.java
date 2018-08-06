package com.gs.smsservice.commands.factory;

import com.gs.smsservice.commands.*;
import com.gs.smsservice.execeptions.UnknownCommandException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommandFactoryTest {

    @Test
    public void should_return_balance_command() {
        // given a BalanceCommand content
        final String smsContent = "BALANCE";
        final Class expectedClass = BalanceCommand.class;

        // when I execute the factory
        final Command command = new CommandFactory().getCommand(smsContent);

        // the typeClass should be BalanceCommand
        assertEquals(expectedClass, command.getClass());
    }

    @Test
    public void should_return_send_command() {
        // given a SendCommand content
        final String smsContent = "SEND-100-GIORNE";
        final Class expectedClass = SendCommand.class;

        // when I execute the factory
        final Command command = new CommandFactory().getCommand(smsContent);

        // the typeClass should be SendCommand
        assertEquals(expectedClass, command.getClass());
    }

    @Test
    public void should_return_total_sent_command() {
        // given a TotalSentCommand content
        final String smsContent = "TOTAL-SENT-GIORNE";
        final Class expectedClass = TotalSentCommand.class;

        // when I execute the factory
        final Command command = new CommandFactory().getCommand(smsContent);

        // the typeClass should be TotalSentCommand
        assertEquals(expectedClass, command.getClass());
    }

    @Test
    public void should_return_total_sent_msmith_command() {
        // given a TotalSentMsmithCommand content
        final String smsContent = "TOTAL-SENT-GIORNE-MSMITH";
        final Class expectedClass = TotalSentMsmithCommand.class;

        // when I execute the factory
        final Command command = new CommandFactory().getCommand(smsContent);

        // the typeClass should be TotalSentMsmithCommand
        assertEquals(expectedClass, command.getClass());
    }

    @Test(expected = UnknownCommandException.class)
    public void should_throw_an_unknown_command_exception_when_sms_content_is_invalid() {
        // given a invalid content
        final String smsContent = "TOTAL_SENT-GIORNE-MSMITH";

        // when I execute the factory
        new CommandFactory().getCommand(smsContent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_an_illegal_argument_exception_when_sms_content_is_empty() {
        // given a invalid content
        final String smsContent = "";

        // when I execute the factory
        new CommandFactory().getCommand(smsContent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_an_illegal_argument_exception_when_sms_content_is_null() {
        // given a invalid content
        final String smsContent = null;

        // when I execute the factory
        new CommandFactory().getCommand(smsContent);
    }

}
