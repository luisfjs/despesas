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
        DespesaSpecification despesaSpecification = new DespesaSpecification(new SearchCriteria("nome", ":", "Eu"));
        List<Despesa> despesas = despesaRepository.findAll(despesaSpecification);

        assertEquals(despesas.size(), 25);
    }

    @Test
    public void deveFiltrarPorValorMaiorQue100() {
        DespesaSpecification despesaSpecification = new DespesaSpecification(new SearchCriteria("valor", ">", "100"));
        List<Despesa> despesas = despesaRepository.findAll(despesaSpecification);

        assertEquals(despesas.size(), 10);
    }

    @Test
    public void deveFiltrarNomeCartao() {
        DespesaSpecification nomeSpecification = new DespesaSpecification(new SearchCriteria("nome", ":", "Eu"));
        DespesaSpecification cartaoSpecification = new DespesaSpecification(new SearchCriteria("cartao", ":", "Nubank"));
        List<Despesa> despesas = despesaRepository.findAll(Specification.where(nomeSpecification).and(cartaoSpecification));

        assertEquals(despesas.size(), 11);
    }
}
