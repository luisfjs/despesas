package br.com.lsena.despesas.resource;

import br.com.lsena.despesas.LogarExecucao;
import br.com.lsena.despesas.domain.Despesa;
import br.com.lsena.despesas.dto.TotalPorCartaoDto;
import br.com.lsena.despesas.dto.TotalPorNomeDto;
import br.com.lsena.despesas.service.DespesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/despesa")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DespesaResource {

    private final Environment environment;
    private final DespesaService despesaService;

    @LogarExecucao
    @GetMapping
    public ResponseEntity<Despesa> listar(@RequestParam(required = false) String filtro) {
        System.out.println(environment.getProperty("DB_HOSTNAME"));
        return new ResponseEntity(despesaService.getAll(filtro), HttpStatus.OK);
    }

    @LogarExecucao
    @PostMapping
    public ResponseEntity<Despesa> salvar(@Valid @RequestBody Despesa despesa) {
        return new ResponseEntity(despesaService.save(despesa), HttpStatus.CREATED);
    }

    @LogarExecucao
    @PostMapping("/salvarLista")
    public ResponseEntity<Despesa> salvarLista(@Valid @RequestBody List<Despesa> despesas) {
        return new ResponseEntity(despesaService.saveAll(despesas), HttpStatus.CREATED);
    }

    @LogarExecucao
    @PutMapping
    public ResponseEntity<Despesa> atualizar(@Valid @RequestBody Despesa despesa) {
        despesaService.get(despesa.getId());
        return new ResponseEntity(despesaService.save(despesa), HttpStatus.OK);
    }

    @LogarExecucao
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable("id") Long id) {
        despesaService.delete(despesaService.get(id));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @LogarExecucao
    @GetMapping("/totalPorNome/{mes}")
    public ResponseEntity<List<TotalPorNomeDto>> totalPorNome(@PathVariable("mes") String mes){
        return new ResponseEntity(despesaService.somaPorNome(mes), HttpStatus.OK);
    }

    @LogarExecucao
    @GetMapping("/totalPorCartao/{mes}")
    public ResponseEntity<List<TotalPorCartaoDto>> totalPorCartao(@PathVariable("mes") String mes){
        return new ResponseEntity(despesaService.somaPorCartao(mes), HttpStatus.OK);
    }
}
