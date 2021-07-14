package com.example.demo.device.repository;

import com.example.demo.device.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Integer> {

    Brand findBrandByName(String name);
}
