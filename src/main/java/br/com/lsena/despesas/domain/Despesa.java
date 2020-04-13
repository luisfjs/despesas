package br.com.lsena.despesas.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Despesa extends AbstractDomain<Long> {
    private static final long serialVersionUID = -6029453953733514084L;
    private String descricao;
    private String cartao;
    private String nome;
    private BigDecimal valor;
    private LocalDate data;
    private String mes;
}
