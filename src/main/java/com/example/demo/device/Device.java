package com.example.demo.device;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Device {


    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @Column(unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Brand brand;

    @NotNull
    public OffsetDateTime creationDateTime;

    public Device(String name, Brand brand, OffsetDateTime creationDateTime) {
        this.name = name;
        this.brand = brand;
        this.creationDateTime = creationDateTime;
    }

}
