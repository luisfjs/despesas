package br.com.lsena.despesas.filtro;

import br.com.lsena.despesas.domain.Despesa;
import br.com.lsena.despesas.repository.DespesaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JPASpecificationsTest {
    @Autowired
    private DespesaRepository despesaRepository;

    @Test
    public void deveFiltrarPorNome() {
        DespesaSpecification despesaSpecification = new DespesaSpecification(new SearchCriteria("nome", SearchOperation.EQUALITY, "Eu"));
        List<Despesa> despesas = despesaRepository.findAll(despesaSpecification);

        assertEquals(despesas.size(), 25);
    }

    @Test
    public void deveFiltrarPorValorMaiorQue100() {
        DespesaSpecification despesaSpecification = new DespesaSpecification(new SearchCriteria("valor", SearchOperation.GREATER_THAN, "100"));
        List<Despesa> despesas = despesaRepository.findAll(despesaSpecification);

        assertEquals(despesas.size(), 9);
    }

    @Test
    public void deveFiltrarNomeCartao() {
        DespesaSpecification nomeSpecification = new DespesaSpecification(new SearchCriteria("nome", SearchOperation.EQUALITY, "Eu"));
        DespesaSpecification cartaoSpecification = new DespesaSpecification(new SearchCriteria("cartao", SearchOperation.EQUALITY, "Nubank"));
        List<Despesa> despesas = despesaRepository.findAll(Specification.where(nomeSpecification).and(cartaoSpecification));

        assertEquals(despesas.size(), 11);
    }

    @Test
    public void deveFiltrarPorDescricaoIniciaCom() {
        DespesaSpecification despesaSpecification = new DespesaSpecification(new SearchCriteria("descricao", SearchOperation.STARTS_WITH, "Uber"));
        List<Despesa> despesas = despesaRepository.findAll(despesaSpecification);

        assertEquals(despesas.size(), 2);
    }

    @Test
    public void deveFiltrarPorDescricaoTerminaCom() {
        DespesaSpecification despesaSpecification = new DespesaSpecification(new SearchCriteria("descricao", SearchOperation.ENDS_WITH, "MERCADOLIVRE"));
        List<Despesa> despesas = despesaRepository.findAll(despesaSpecification);

        assertEquals(despesas.size(), 2);
    }

    @Test
    public void deveFiltrarPorNomeNegacao() {
        DespesaSpecification despesaSpecification = new DespesaSpecification(new SearchCriteria("nome", SearchOperation.NEGATION, "Eu"));
        List<Despesa> despesas = despesaRepository.findAll(despesaSpecification);

        assertEquals(despesas.size(), 32);
    }

    @Test
    public void deveFiltrarPorDescricaoContains() {
        DespesaSpecification despesaSpecification = new DespesaSpecification(new SearchCriteria("descricao", SearchOperation.CONTAINS, "EXTRA"));
        List<Despesa> despesas = despesaRepository.findAll(despesaSpecification);

        assertEquals(despesas.size(), 9);
    }

    @Test
    public void deveFiltrarPorValorMaiorQue100MenorQue500() {
        DespesaSpecification despesaSpecification = new DespesaSpecification(new SearchCriteria("valor", SearchOperation.GREATER_THAN, "100"));
        DespesaSpecification despesa1Specification = new DespesaSpecification(new SearchCriteria("valor", SearchOperation.LESS_THAN, "500"));
        List<Despesa> despesas = despesaRepository.findAll(Specification.where(despesaSpecification).and(despesa1Specification));

        assertEquals(despesas.size(), 7);
    }

    //@Test
    public void deveFiltrarData() {
        DespesaSpecification dataSpecification = new DespesaSpecification(new SearchCriteria("data", SearchOperation.EQUALITY, "2020-03-06"));
        List<Despesa> despesas = despesaRepository.findAll(dataSpecification);

        assertEquals(despesas.size(), 1);
    }
}
