package com.tads.hardware.model;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Hardware {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date deleted;
    private String imgUri;
    @Size(min = 3, max = 30, message = ("Nome: testo abaixo de 3 ou maior que 30 cacaracteres"))
    @NotBlank
    private String nome;
    @Size(min = 3, max = 30, message = ("Nome: testo abaixo de 3 ou maior que 30 cacaracteres"))
    private String marca;
    @Size(min = 3, max = 30, message = ("Nome: testo abaixo de 3 ou maior que 30 cacaracteres"))
    private String modelo;
    @NotBlank(message = ("Não pode ficar vazio"))
    private String valor;
}
