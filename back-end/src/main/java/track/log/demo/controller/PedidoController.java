package track.log.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import track.log.demo.dto.EntregaRequest;
import track.log.demo.model.Pedido;
import track.log.demo.repository.PedidoRepository;
import track.log.demo.service.PedidoService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedido")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final PedidoService pedidoService;

    public PedidoController(PedidoRepository pedidoRepository, PedidoService pedidoService) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoService = pedidoService;
    }


    /** Muda o status de entregue para true, registra o horario atual e o colaborador responsável
     *  Teste com Postman 200 OK.*/
    @PutMapping("{notaFiscal}/entregar")
    public ResponseEntity<String> registrarEntrega(@PathVariable String notaFiscal, @RequestBody EntregaRequest request){
        return pedidoRepository.findByNotaFiscal(notaFiscal).map(pedido -> {
            pedido.setEntregue(true);
            pedido.setDataDaEntrega(LocalDateTime.now());
            pedido.setColaborador(request.getColaborador());
            pedidoRepository.save(pedido);
            return ResponseEntity.ok("Pedido marcado como entregue.");
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Page<Pedido> buscarPedidos(@RequestParam(required = false) String notaFiscal,
                                      @RequestParam(required = false) String numeroOperacional,
                                      @RequestParam(required = false) String destinatario,
                                      @RequestParam(required = false) String cidadeOrigem,
                                      @RequestParam(required = false) String cidadeDestino,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "id,asc") String[] sort){

        Sort.Direction direction = Sort.Direction.ASC;
        String sortBy = "id";

        if (sort.length == 2){
            sortBy = sort[0];
            direction = sort[1].equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC : Sort.Direction.ASC
;        } else if(sort.length == 1){
            sortBy = sort[0];
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return pedidoService.buscarPedidosComFiltros(
                notaFiscal,
                numeroOperacional,
                destinatario,
                cidadeOrigem,
                cidadeDestino,
                pageable);
    }

}
