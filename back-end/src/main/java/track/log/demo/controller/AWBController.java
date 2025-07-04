package track.log.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import track.log.demo.dto.RecebimentoRequest;
import track.log.demo.model.AWB;
import track.log.demo.repository.AWBRepository;
import track.log.demo.service.AWBService;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/awb")
public class AWBController {

    private final AWBRepository awbRepository;

    public AWBController(AWBRepository awbRepository) {
        this.awbRepository = awbRepository;
    }

    /** Retorna todas as AWB
     *  Teste com Postman 200 OK.*/
    @GetMapping
    public List<AWB> listarTodasAWB(){
        return awbRepository.findAll();
    }

    /** Retorna uma AWB
     *  Teste com Postman 200 OK.*/
    @GetMapping("/findByAWB/{numeroOperacional}")
    public ResponseEntity<AWB> buscarPorNumeroOperacional(@PathVariable String numeroOperacional){
        return awbRepository.findByNumeroOperacional(numeroOperacional)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
}
