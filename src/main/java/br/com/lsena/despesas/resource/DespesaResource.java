package br.com.lsena.despesas.resource;

import br.com.lsena.despesas.domain.Despesa;
import br.com.lsena.despesas.dto.TotalPorCartaoDto;
import br.com.lsena.despesas.dto.TotalPorNomeDto;
import br.com.lsena.despesas.service.DespesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/despesa")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DespesaResource {

    private final DespesaService despesaService;

    @GetMapping
    public ResponseEntity<Despesa> listar(@RequestParam(required = false) String filtro) {
        return new ResponseEntity(despesaService.getAll(filtro), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Despesa> salvar(@Valid @RequestBody Despesa despesa) {
        return new ResponseEntity(despesaService.save(despesa), HttpStatus.CREATED);
    }

    @PostMapping("/salvarLista")
    public ResponseEntity<Despesa> salvarLista(@Valid @RequestBody List<Despesa> despesas) {
        return new ResponseEntity(despesaService.saveAll(despesas), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Despesa> atualizar(@Valid @RequestBody Despesa despesa) {
        despesaService.get(despesa.getId());
        return new ResponseEntity(despesaService.save(despesa), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable("id") Long id) {
        despesaService.delete(despesaService.get(id));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/totalPorNome")
    public ResponseEntity<List<TotalPorNomeDto>> totalPorNome(){
        return new ResponseEntity(despesaService.somaPorNome(), HttpStatus.OK);
    }

    @GetMapping("/totalPorCartao")
    public ResponseEntity<List<TotalPorCartaoDto>> totalPorCartao(){
        return new ResponseEntity(despesaService.somaPorCartao(), HttpStatus.OK);
    }
}
