package com.example.demo.device.service;

import com.example.demo.device.Brand;
import com.example.demo.device.Device;
import com.example.demo.device.dto.DeviceDtoRequest;
import com.example.demo.device.dto.DeviceMapper;
import com.example.demo.device.exception.BrandNotFoundException;
import com.example.demo.device.exception.DeviceNotFoundException;
import com.example.demo.device.repository.BrandRepository;
import com.example.demo.device.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceMapper deviceMapper;

    private final BrandRepository brandRepository;
    private final DeviceRepository deviceRepository;

    public Device createDevice(DeviceDtoRequest deviceDto) {
        Device device = deviceMapper.toEntity(deviceDto);
        setDeviceBrand(deviceDto.getBrand(), device);
        return deviceRepository.save(device);
    }

    @Override
    public Device getDevice(int deviceId) throws DeviceNotFoundException {
        return deviceRepository.findById(deviceId).orElseThrow(() -> new DeviceNotFoundException(deviceId));
    }

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public List<Device> findByBrand(String brand) {
        return deviceRepository.findDevicesByBrandName(brand);
    }

    @Override
    public Device updateDevice(int deviceId, DeviceDtoRequest deviceDto) {
        Device device = deviceRepository.findById(deviceId).orElseThrow(() -> new DeviceNotFoundException(deviceId));
        deviceMapper.updateDeviceFromDto(deviceDto, device);
        if (deviceDto.getBrand() != null) {
            setDeviceBrand(deviceDto.getBrand(), device);
        }
        deviceRepository.save(device);
        return device;
    }

    @Override
    public void deleteDevice(int deviceId) {
        Device device = deviceRepository.findById(deviceId).orElseThrow(() -> new DeviceNotFoundException(deviceId));
        deviceRepository.delete(device);
    }

    private void setDeviceBrand(String brandName, Device device) {
        Brand brand = brandRepository.findBrandByName(brandName);
        if (brand == null) throw new BrandNotFoundException(brandName);
        device.setBrand(brand);
    }
}
