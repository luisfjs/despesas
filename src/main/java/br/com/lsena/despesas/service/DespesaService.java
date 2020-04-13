package br.com.lsena.despesas.service;

import br.com.lsena.despesas.domain.Despesa;
import br.com.lsena.despesas.dto.TotalPorCartaoDto;
import br.com.lsena.despesas.dto.TotalPorNomeDto;
import br.com.lsena.despesas.error.RequestParamException;
import br.com.lsena.despesas.error.ResourceNotFoundExeption;
import br.com.lsena.despesas.repository.AbstractRepository;
import br.com.lsena.despesas.repository.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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
        try {
            return repository.findAll();
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

    public List<TotalPorNomeDto> somaPorNome(String mes){
        List<Despesa> despesas = repository.findByMes(mes);
        return somaPorNome(despesas);
    }

    public List<TotalPorCartaoDto> somaPorCartao(String mes){
        List<Despesa> despesas = repository.findByMes(mes);
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
