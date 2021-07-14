package com.example.demo.device.controller;

import com.example.demo.device.Device;
import com.example.demo.device.dto.DeviceDtoRequest;
import com.example.demo.device.dto.DeviceDtoResponse;
import com.example.demo.device.dto.DeviceMapper;
import com.example.demo.device.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;
    private final DeviceMapper deviceMapper;


    @PostMapping
    public ResponseEntity<DeviceDtoRequest> addDevice(@Valid @RequestBody DeviceDtoRequest deviceDto) {
        Device device = new Device();
        Device savedDevice = deviceService.createDevice(deviceDto);


        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedDevice.getId())
                .toUri();

        return ResponseEntity.created(location).build();

    }

    @GetMapping("/{id}")
    public EntityModel<DeviceDtoResponse> getDevice(@PathVariable int id) {

        Device device = deviceService.getDevice(id);

        DeviceDtoResponse deviceDtoResponse = deviceMapper.toDto(device);


        EntityModel<DeviceDtoResponse> resource = EntityModel.of(deviceDtoResponse);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).getAllDevices());

        resource.add(linkTo.withRel("all-devices"));

        return resource;

    }


    @GetMapping
    public List<DeviceDtoResponse> getAllDevices() {

        List<Device> allDevices = deviceService.getAllDevices();
        return allDevices.stream().map(device -> deviceMapper.toDto(device)).collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public void updateDevice(@PathVariable int id, @RequestBody DeviceDtoRequest deviceDto) {
        deviceService.updateDevice(id, deviceDto);
    }

    @DeleteMapping("/{id}")
    public void deleteDevice(@PathVariable int id) {
        deviceService.deleteDevice(id);
    }

    @GetMapping("/search/{brand}")
    public List<Device> searchByBrand(@PathVariable String brand) {
        return deviceService.findByBrand(brand);
    }
}
