package track.log.demo.controller;

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

    /** Retorna todos os pedidos
     *  Teste com Postman 200 OK.*/
    @GetMapping
    public List<Pedido> listarTodosPedidos(){
        return pedidoRepository.findAll();
    }

    /** Lista todos pedidos que compartilham uma mesma AWB
     *  Teste com Postman 200 OK.*/
    @GetMapping("/findByAWB/{numeroOperacional}")
    public List<Pedido> findByAWB(@PathVariable String numeroOperacional){
        return pedidoRepository.findByNumeroOperacional(numeroOperacional);
    }

    /** Retorna um pedido
     *  Teste com Postman 200 OK.*/
    @GetMapping("/findByPedido/{notaFiscal}")
    public Optional<Pedido> findByPedido(@PathVariable String notaFiscal){
        return pedidoService.findByNotaFiscal(notaFiscal);
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

}
