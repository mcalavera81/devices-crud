package com.example.demo.device.dto;


import com.example.demo.device.Device;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DeviceMapper {


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "brand", ignore = true)
    void updateDeviceFromDto(DeviceDtoRequest dto, @MappingTarget Device device);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "brand", ignore = true)
    Device toEntity(DeviceDtoRequest source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "brand.name", target = "brand")
    DeviceDtoResponse toDto(Device target);

}

