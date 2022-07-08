package com.protalento.protalentodbproject.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Car")
public class CarEntity {

    @Id
    private String id;

    @Column
    private String marca;

    @Column
    private Integer modelo;

    @Column
    private String placa;

}
