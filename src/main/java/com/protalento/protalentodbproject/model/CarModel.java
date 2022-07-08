package com.protalento.protalentodbproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarModel {

    private String id;

    @NotNull
    @NotEmpty
    private String marca;

    @NotNull
    private Integer modelo;

    @NotNull
    @NotEmpty
    private String placa;

    private String action;
}
