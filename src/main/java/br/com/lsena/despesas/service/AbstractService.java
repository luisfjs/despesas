package br.com.lsena.despesas.service;

import br.com.lsena.despesas.error.ValidationExeption;
import br.com.lsena.despesas.filter.GenericSpecification;
import br.com.lsena.despesas.filter.SearchCriteria;
import br.com.lsena.despesas.repository.AbstractRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractService<T> {
    protected AbstractRepository<T> repository;

    public AbstractService(AbstractRepository repository) {
        this.repository = repository;
    }

    public Page<T> listarFiltro(List<SearchCriteria> criterias, Pageable pageable, BinaryOperator<Specification<T>> accumulator) {
        try {
            Optional<Specification<T>> optionalSpec = filter(criterias, accumulator);
            return optionalSpec.isPresent()
                    ? listar(pageable, optionalSpec.get())
                    : listarPaginado(pageable);
        } catch (Exception e) {
            return Page.empty(pageable);
        }
    }

    public Page<T> listarPaginado(Pageable pageable) {
        try {
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ValidationExeption();
        }
    }

    public Optional<Specification<T>> filter(List<SearchCriteria> criterias, BinaryOperator<Specification<T>> accumulator) {
        List<Specification<T>> lsSpec = criterias.stream()
                .filter(criteria -> Objects.nonNull(criteria.getValue()))
                .map(GenericSpecification<T>::new).collect(Collectors.toList());

//        Specification<T> spec = lsSpec.stream().reduce(Specification.where(null), (spec1, spec2) -> Specification.where(spec1).and(spec2));
        Specification<T> spec = lsSpec.stream().reduce(Specification.where(null), accumulator);
        return Optional.of(spec);
    }

    private Page<T> listar(Pageable pageable, Specification<T> specification) {
        try {
            return repository.findAll(specification, pageable);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ValidationExeption();
        }
    }
}
