package com.example.demo.device.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDtoRequest {

    @NotNull
    private String name;

    @NotNull
    private String brand;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX",
            timezone = "UTC")
    private OffsetDateTime creationDateTime;
}
