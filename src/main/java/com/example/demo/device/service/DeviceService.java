package com.example.demo.device.service;

import com.example.demo.device.Device;
import com.example.demo.device.dto.DeviceDtoRequest;
import com.example.demo.device.exception.DeviceNotFoundException;

import java.util.List;

public interface DeviceService {
    Device createDevice(DeviceDtoRequest deviceDto);

    Device getDevice(int deviceId) throws DeviceNotFoundException;

    List<Device> getAllDevices();

    Device updateDevice(int deviceId, DeviceDtoRequest deviceDto);

    void deleteDevice(int deviceId);

    List<Device> findByBrand(String brand);
}
