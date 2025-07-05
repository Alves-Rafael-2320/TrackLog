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

    /**
     * Altera o status de um pedido para entregue, registra o horário atual
     * e o nome do colaborador responsável.
     *
     * @param notaFiscal número da nota fiscal usada para localizar o pedido
     * @param request contém o nome do colaborador
     * @return 200 OK se encontrado e atualizado, 404 Not Found se não encontrado
     */
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



    /**
     * Realiza a busca de pedidos com filtros opcionais e suporte à paginação e ordenação.
     *
     * @param notaFiscal filtro por nota fiscal
     * @param numeroOperacional filtro por número operacional
     * @param destinatario filtro por destinatário
     * @param cidadeOrigem filtro por cidade de origem
     * @param cidadeDestino filtro por cidade de destino
     * @param page número da página (default 0)
     * @param size quantidade de elementos por página (default 10)
     * @param sort array com campo e direção (ex: id,asc)
     * @return página de resultados filtrados
     */
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
