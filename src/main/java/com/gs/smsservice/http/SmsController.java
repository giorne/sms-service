package com.gs.smsservice.http;

import com.gs.smsservice.handler.SmsHandler;
import com.gs.smsservice.http.json.SmsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/sms")
public class SmsController {

    private final SmsHandler smsHandler;

    public SmsController(final SmsHandler smsHandler) {
        this.smsHandler = smsHandler;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String post(@RequestBody @Valid SmsRequest request) {
        log.info("Receiving request: {}", request);
        return smsHandler.handleSmsRequest(request.getSmsContent(), request.getDeviceId());
    }

}
