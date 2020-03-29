package br.com.lsena.despesas.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TotalPorCartaoDto {
    private String cartao;
    @JsonProperty("totalPorNome")
    private List<TotalPorNomeDto> totalPorNomeDto;
}
