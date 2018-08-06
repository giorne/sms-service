package com.gs.smsservice.http.json;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
public class SmsRequest {

    @NotEmpty
    private String deviceId;

    private String smsContent;

}
