package com.example.demo.device.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDtoResponse {

    private int id;

    private String name;

    private String brand;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX",
            timezone = "UTC")
    private OffsetDateTime creationDateTime;
}
