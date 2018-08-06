package com.gs.smsservice.http;

import com.gs.smsservice.handler.SmsHandler;
import com.gs.smsservice.http.json.SmsRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;

@Slf4j
@RestController
@RequestMapping("/api/sms/commands")
@Api(tags = "Api for handling sms commands", produces = MediaType.APPLICATION_JSON_VALUE)
public class SmsController {

    private final SmsHandler smsHandler;

    public SmsController(final SmsHandler smsHandler) {
        this.smsHandler = smsHandler;
    }

    @ApiOperation(value = "Send a command for a deviceId")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Command executed", response = String.class),
            @ApiResponse(code = 400, message = "Bad request", response = ValidationException.class)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String sendCommand(@RequestBody @Valid final SmsRequest request) {
        log.info("Receiving request: {}", request);
        return smsHandler.handleSmsRequest(request.getSmsContent(), request.getDeviceId());
    }

}
