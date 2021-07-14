package com.example.demo.device.repository;

import com.example.demo.device.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Integer> {

    List<Device> findDevicesByBrandName(String name);

}
