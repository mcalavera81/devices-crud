package com.example.demo.device;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Brand {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String name;

    public Brand(String name) {
        this.name = name;
    }
}
