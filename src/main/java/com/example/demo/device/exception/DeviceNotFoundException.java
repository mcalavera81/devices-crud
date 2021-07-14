package com.example.demo.device.exception;


public class DeviceNotFoundException extends RuntimeException {
    public DeviceNotFoundException(int id) {
        super(String.format("Not found device with id %s", id));
    }
}

