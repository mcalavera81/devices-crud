package com.example.demo.device.exception;


public class BrandNotFoundException extends RuntimeException {
    public BrandNotFoundException(String name) {
        super(String.format("Not found brand with name %s", name));
    }
}

