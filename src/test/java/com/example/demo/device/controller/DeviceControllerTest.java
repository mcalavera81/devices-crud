package com.example.demo.device.controller;

import com.example.demo.device.Brand;
import com.example.demo.device.Device;
import com.example.demo.device.dto.DeviceDtoRequest;
import com.example.demo.device.exception.DeviceNotFoundException;
import com.example.demo.device.repository.BrandRepository;
import com.example.demo.device.repository.DeviceRepository;
import com.example.demo.device.service.DeviceService;
import com.example.demo.device.utils.Utils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private DeviceService deviceService;

    @BeforeEach
    public void init() {
        deviceRepository.deleteAll();
        brandRepository.deleteAll();
        seedTestDatabase();
    }

    private void seedTestDatabase() {
        Brand brand1 = brandRepository.save(new Brand("brand1"));
        Brand brand2 = brandRepository.save(new Brand("brand2"));
        Brand brand3 = brandRepository.save(new Brand("brand3"));

        deviceRepository.save(new Device("name1", brand1, new Date().toInstant().atOffset(ZoneOffset.UTC)));
        deviceRepository.save(new Device("name2", brand2, new Date().toInstant().atOffset(ZoneOffset.UTC)));
        deviceRepository.save(new Device("name3", brand3, new Date().toInstant().atOffset(ZoneOffset.UTC)));

    }

    @Test
    public void getAllDevices() throws Exception {
        this.mockMvc.perform(get("/devices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

    }

    @Test
    public void createDevice() throws Exception {
        String deviceInJson = "{\"name\":\"device_name\",\"brand\":\"brand2\", \"creationDateTime\":\"2021-07-07T10:10:10Z\"}";

        MvcResult result = this.mockMvc.perform(
                post("/devices")
                        .content(deviceInJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        String location = result.getResponse().getHeader("Location");
        int deviceId = Integer.parseInt(location.replaceFirst(".*/([^/?]+).*", "$1"));


        Device deviceFromDb = deviceService.getDevice(deviceId);
        Assertions.assertNotNull(deviceFromDb);
        Assertions.assertEquals(deviceId, deviceFromDb.getId());
        Assertions.assertEquals("device_name", deviceFromDb.getName());
        Assertions.assertEquals("brand2", deviceFromDb.getBrand().getName());
        Assertions.assertEquals("2021-07-07T10:10:10Z", Utils.formatDate(deviceFromDb.getCreationDateTime()));


    }

    @Test
    public void createDeviceNotValidBrand() throws Exception {
        String deviceInJson = "{\"name\":\"device_name\",\"brand\":\"brand_non_valid\", \"creationDateTime\":\"2021-07-07T10:10:10Z\"}";

        this.mockMvc.perform(
                post("/devices")
                        .content(deviceInJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getDevice() throws Exception {

        DeviceDtoRequest deviceDto = new DeviceDtoRequest("device_name_test", "brand1", new Date().toInstant().atOffset(ZoneOffset.UTC));
        Device device = deviceService.createDevice(deviceDto);

        this.mockMvc.perform(
                get("/devices/{id}", device.getId()).contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(device.getId())))
                .andExpect(jsonPath("$.name", is(deviceDto.getName())))
                .andExpect(jsonPath("$.brand", is(deviceDto.getBrand())))
                .andExpect(jsonPath("$.creationDateTime", is(Utils.formatDate(deviceDto.getCreationDateTime()))));

    }

    @Test
    public void getNonExistingDevice() throws Exception {
        int nonExistentDeviceId = -1;
        this.mockMvc.perform(
                get("/devices/{id}", nonExistentDeviceId).contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateDeviceName() throws Exception {
        OffsetDateTime now = new Date().toInstant().atOffset(ZoneOffset.UTC);
        DeviceDtoRequest deviceDto = new DeviceDtoRequest("device_name_test", "brand1", now);
        Device device = deviceService.createDevice(deviceDto);

        String partialUpdateBody = "{\"name\":\"new_device_name\"}";

        MvcResult result = this.mockMvc.perform(
                put("/devices/{id}", device.getId())
                        .content(partialUpdateBody)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        Device deviceFromDb = deviceService.getDevice(device.getId());
        Assertions.assertNotNull(deviceFromDb);
        Assertions.assertEquals(device.getId(), deviceFromDb.getId());
        Assertions.assertEquals("new_device_name", deviceFromDb.getName());
        Assertions.assertEquals("brand1", deviceFromDb.getBrand().getName());
        Assertions.assertEquals(Utils.formatDate(now), Utils.formatDate(deviceFromDb.getCreationDateTime()));
    }

    @Test
    public void updateDeviceBrand() throws Exception {
        OffsetDateTime now = new Date().toInstant().atOffset(ZoneOffset.UTC);
        DeviceDtoRequest deviceDto = new DeviceDtoRequest("device_name_test", "brand1", now);
        Device device = deviceService.createDevice(deviceDto);

        String partialUpdateBody = "{\"brand\":\"brand2\"}";

        this.mockMvc.perform(
                put("/devices/{id}", device.getId())
                        .content(partialUpdateBody)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());

        Device deviceFromDb = deviceService.getDevice(device.getId());
        Assertions.assertNotNull(deviceFromDb);
        Assertions.assertEquals(device.getId(), deviceFromDb.getId());
        Assertions.assertEquals("device_name_test", deviceFromDb.getName());
        Assertions.assertEquals("brand2", deviceFromDb.getBrand().getName());
        Assertions.assertEquals(Utils.formatDate(now), Utils.formatDate(deviceFromDb.getCreationDateTime()));
    }

    @Test
    public void updateNonExistingDevice() throws Exception {
        int nonExistentDeviceId = -1;
        String partialUpdateBody = "{\"name\":\"new_device_name\"}";

        this.mockMvc.perform(
                put("/devices/{id}", nonExistentDeviceId)
                        .content(partialUpdateBody)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound());

    }

    @Test
    public void deleteDevice() throws Exception {
        DeviceDtoRequest deviceDto = new DeviceDtoRequest("device_name_test", "brand1", new Date().toInstant().atOffset(ZoneOffset.UTC));
        Device device = deviceService.createDevice(deviceDto);

        this.mockMvc.perform(
                delete("/devices/{id}", device.getId()).contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());

        try {
            deviceService.getDevice(device.getId());
            fail(String.format("The device with id %s should not exist", device.getId()));
        } catch (DeviceNotFoundException exception) {
        }

    }

    @Test
    public void deleteNonExistingDevice() throws Exception {
        int nonExistentDeviceId = -1;
        this.mockMvc.perform(
                delete("/devices/{id}", nonExistentDeviceId).contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound());

    }

    @Test
    public void searchByBrand() throws Exception {
        DeviceDtoRequest deviceDto = new DeviceDtoRequest("device_name_test", "brand2", new Date().toInstant().atOffset(ZoneOffset.UTC));
        Device device = deviceService.createDevice(deviceDto);

        this.mockMvc.perform(
                get("/devices/search/{brandName}", device.getBrand().getName())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name").value(Matchers.containsInAnyOrder("device_name_test", "name2")));


    }


}
