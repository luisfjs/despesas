package br.com.lsena.despesas.service;

import br.com.lsena.despesas.repository.AbstractRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractService<T> {
    protected AbstractRepository<T> repository;

    public AbstractService(AbstractRepository repository) {
        this.repository = repository;
    }
}
