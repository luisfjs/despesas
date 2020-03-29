package br.com.lsena.despesas.filtro;

import br.com.lsena.despesas.domain.Despesa;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DespesaSpecificationBuilder {
    private List<SearchCriteria> params;

    public DespesaSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public DespesaSpecificationBuilder with(
            String key, String operation, Object value, String prefix, String suffix) {

        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            if (op == SearchOperation.EQUALITY) {
                boolean startWithAsterisk = prefix.contains("*");
                boolean endWithAsterisk = suffix.contains("*");

                if (startWithAsterisk && endWithAsterisk) {
                    op = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    op = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    op = SearchOperation.STARTS_WITH;
                }
            }
            params.add(new SearchCriteria(key, op, value));
        }
        return this;
    }

    public Specification<Despesa> build() {
        if (params.size() == 0) {
            return null;
        }

        Specification result = new DespesaSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result).and(new DespesaSpecification(params.get(i)));
        }

        return result;
    }

}
