package br.com.lsena.despesas.repository;

import br.com.lsena.despesas.domain.Despesa;

import java.util.List;

public interface DespesaRepository extends AbstractRepository<Despesa> {
    List<Despesa> findByMes(String mes);
}
