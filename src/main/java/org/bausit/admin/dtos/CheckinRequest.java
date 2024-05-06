package org.bausit.admin.dtos;

import lombok.Value;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Value
public class CheckinRequest {
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H")
        .withZone(ZoneId.of("America/New_York"));

    String emailOrPhone;
    String date;
    int hour;

    public Instant getCheckoutDate() {
        String checkoutDateString = date + " " + hour;
        return ZonedDateTime.parse(checkoutDateString, formatter)
            .toInstant();
    }
}
