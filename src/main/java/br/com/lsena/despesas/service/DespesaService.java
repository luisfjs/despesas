package br.com.lsena.despesas.service;

import br.com.lsena.despesas.domain.Despesa;
import br.com.lsena.despesas.dto.TotalPorCartaoDto;
import br.com.lsena.despesas.dto.TotalPorNomeDto;
import br.com.lsena.despesas.error.RequestParamException;
import br.com.lsena.despesas.error.ResourceNotFoundExeption;
import br.com.lsena.despesas.filtro.DespesaSpecificationBuilder;
import br.com.lsena.despesas.filtro.SearchOperation;
import br.com.lsena.despesas.repository.AbstractRepository;
import br.com.lsena.despesas.repository.DespesaRepository;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class DespesaService extends AbstractService<Despesa> {


    private DespesaRepository repository;

    @Autowired
    public DespesaService(DespesaRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<Despesa> getAll(String filtro) {
        DespesaSpecificationBuilder builder = new DespesaSpecificationBuilder();
        String operationSetExper = Joiner.on("|").join(SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(filtro + ",");
        while (matcher.find()) {
            builder.with(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(4),
                    matcher.group(3),
                    matcher.group(5));
        }

        Specification<Despesa> spec = builder.build();
        try {
            return repository.findAll(spec);
        } catch (Exception e) {
            throw new RequestParamException("Algum parametro informado não é válido, verificar resposta");
        }
    }

    public Despesa get(Long id) {
        Optional<Despesa> entity = repository.findById(id);
        return entity.orElseThrow(() -> new ResourceNotFoundExeption("Recurso não encontrado com o ID: " + id));
    }

    public void delete(Despesa despesa) {
        try {
            repository.delete(despesa);
        } catch (Exception e) {
            throw new RequestParamException("Algum parametro informado não é válido, verificar resposta");
        }
    }

    public Despesa save(Despesa despesa) {
        try {
            return repository.save(despesa);
        } catch (Exception e) {
            throw new RequestParamException("Erro ao salvar recurso, verifique os parametros digitados");
        }
    }

    public List<Despesa> saveAll(List<Despesa> despesas) {
        try {
            return repository.saveAll(despesas);
        } catch (Exception e) {
            throw new RequestParamException("Erro ao salvar recurso, verifique os parametros digitados");
        }
    }

    public List<TotalPorNomeDto> somaPorNome(){
        List<Despesa> despesas = repository.findAll();
        return somaPorNome(despesas);
    }

    public List<TotalPorCartaoDto> somaPorCartao(){
        List<Despesa> despesas = repository.findAll();
        return despesas.stream()
                .collect(groupingBy(Despesa::getCartao, Collectors.toList()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> somaPorNome(entry.getValue())
                ))
                .entrySet()
                .stream()
                .map(entry -> TotalPorCartaoDto.builder().cartao(entry.getKey()).totalPorNomeDto(entry.getValue()).build())
                .collect(Collectors.toList());
    }

    private List<TotalPorNomeDto> somaPorNome(List<Despesa> despesas){
        return despesas.stream()
                .collect(groupingBy(Despesa::getNome, Collectors.reducing(BigDecimal.ZERO, Despesa::getValor, BigDecimal::add)))
                .entrySet()
                .stream()
                .map(entry -> TotalPorNomeDto.builder().nome(entry.getKey()).valor(entry.getValue()).build())
                .collect(Collectors.toList());
    }
}
