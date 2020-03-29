package br.com.lsena.despesas.filtro;

import br.com.lsena.despesas.domain.Despesa;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DespesaSpecificationsBuilder {
    private final List<SearchCriteria> params;

    public DespesaSpecificationsBuilder() {
        params = new ArrayList<SearchCriteria>();
    }

    public DespesaSpecificationsBuilder with(String key, String operation, Object value, boolean orPredicate) {
        params.add(new SearchCriteria(key, operation, value, orPredicate));
        return this;
    }

    public Specification<Despesa> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification> specs = params.stream()
                .map(DespesaSpecification::new)
                .collect(Collectors.toList());

        Specification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i)
                    .isOrPredicate()
                    ? Specification.where(result)
                    .or(specs.get(i))
                    : Specification.where(result)
                    .and(specs.get(i));
        }
        return result;
    }
}
