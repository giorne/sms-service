package com.gs.smsservice.commands.executor;

import com.gs.smsservice.commands.Command;
import com.gs.smsservice.execeptions.UnknownCommandException;
import com.gs.smsservice.execeptions.UserHasNoFundsException;
import com.gs.smsservice.execeptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;

import static com.gs.smsservice.constants.ResponseMessages.UNKNOWN_COMMAND;
import static com.gs.smsservice.constants.ResponseMessages.USER_HAS_NO_FUNDS;
import static com.gs.smsservice.constants.ResponseMessages.USER_NOT_FOUND;
import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
public class CommandExecutor {

    private final String smsContent;
    private final String deviceId;

    public CommandExecutor(final String smsContent, final String deviceId) {
        this.smsContent = isEmpty(smsContent) ? smsContent : smsContent.toUpperCase();
        this.deviceId = deviceId;
    }

    public String execute(final Command command) {
        try {
            log.info("Executing command {} by deviceId {}", smsContent, deviceId);
            final String response = command.execute(smsContent, deviceId);
            log.info("Command {} executed successfully by device {}. Response: {}", smsContent, deviceId, response);
            return response;
        } catch (UnknownCommandException ex) {
            log.error("Invalid command: {}", ex.getMessage());
            return UNKNOWN_COMMAND;
        } catch (UserNotFoundException ex) {
            log.warn("User {} not found", ex.getMessage());
            return USER_NOT_FOUND;
        } catch (UserHasNoFundsException ex) {
            log.warn("User {} has no funds", ex.getMessage());
            return USER_HAS_NO_FUNDS;
        }
    }


}
