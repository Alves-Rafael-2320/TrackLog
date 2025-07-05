package track.log.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import track.log.demo.dto.RecebimentoRequest;
import track.log.demo.model.AWB;
import track.log.demo.repository.AWBRepository;
import track.log.demo.service.AWBService;
import track.log.demo.specification.AWBSpecification;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/awb")
public class AWBController {

    private final AWBRepository awbRepository;
    private final AWBService awbService;

    public AWBController(AWBRepository awbRepository, AWBService awbService) {
        this.awbRepository = awbRepository;
        this.awbService = awbService;
    }

    /** Muda o status de recebida para true, registra o horario atual e o colaborador responsável
     *  Teste com Postman 200 OK.*/
    @PutMapping("/{numeroOperacional}/receber")
    public ResponseEntity<String> registrarRecebimento(@PathVariable String numeroOperacional,
                                                       @RequestBody RecebimentoRequest request) {
        return awbRepository.findByNumeroOperacional(numeroOperacional)
                .map(awb -> {
                    awb.setRecebida(true);
                    awb.setColaborador(request.getColaborador());
                    awb.setDataDeRecebimento(LocalDateTime.now());
                    awbRepository.save(awb);
                    return ResponseEntity.ok("AWB marcada como recebida.");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /** Consulta com filtragem dinâmica e paginação, consultar sem path variables para retornar todas AWB
     * Teste com Postman 200 OK. */
    @GetMapping
    public Page<AWB> buscarAWBs(
            @RequestParam(required = false) String numeroOperacional,
            @RequestParam(required = false) Boolean recebida,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        Sort.Direction direction = Sort.Direction.ASC;
        String sortBy = "id";

        if (sort.length == 2) {
            sortBy = sort[0];
            direction = sort[1].equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
        } else if (sort.length == 1) {
            sortBy = sort[0];
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return awbService.buscarAWBComFiltros(numeroOperacional, recebida, pageable);
    }
}
