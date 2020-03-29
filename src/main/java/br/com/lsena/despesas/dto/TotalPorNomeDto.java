package br.com.lsena.despesas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TotalPorNomeDto {
    private String nome;
    private BigDecimal valor = BigDecimal.ZERO;
}
