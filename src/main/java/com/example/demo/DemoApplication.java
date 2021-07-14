package com.example.demo;

import com.example.demo.device.Brand;
import com.example.demo.device.Device;
import com.example.demo.device.repository.BrandRepository;
import com.example.demo.device.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZoneOffset;
import java.util.Date;

@SpringBootApplication
public class DemoApplication implements ApplicationRunner {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private BrandRepository brandRepository;


    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {


        Brand brand1 = brandRepository.save(new Brand("brand1"));
        Brand brand2 = brandRepository.save(new Brand("brand2"));
        Brand brand3 = brandRepository.save(new Brand("brand3"));

        deviceRepository.save(new Device("name1", brand1, new Date().toInstant().atOffset(ZoneOffset.UTC)));
        deviceRepository.save(new Device("name2", brand2, new Date().toInstant().atOffset(ZoneOffset.UTC)));
        deviceRepository.save(new Device("name3", brand3, new Date().toInstant().atOffset(ZoneOffset.UTC)));
    }
}
