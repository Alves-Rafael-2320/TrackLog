package track.log.demo.service;

import org.springframework.stereotype.Service;
import track.log.demo.model.AWB;
import track.log.demo.model.Pedido;
import track.log.demo.repository.PedidoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final AWBService awbService;

    public PedidoService(PedidoRepository pedidoRepository, AWBService awbService) {
        this.pedidoRepository = pedidoRepository;
        this.awbService = awbService;
    }

    public Pedido salvarPedido(Pedido pedido){
        if (pedido.getNumeroOperacional()!= null && !pedido.getNumeroOperacional().isEmpty()){
            AWB awb = awbService.findOrCreateByNumeroOperacional(pedido.getNumeroOperacional());
            pedido.setAwb(awb);
        }
        return  pedidoRepository.save(pedido);
    }

    public List<Pedido> listarTodos(){
        return pedidoRepository.findAll();
    }

    public List<Pedido> buscarPorNumeroOperacional(String numeroOperacional){
        return pedidoRepository.findByNumeroOperacional(numeroOperacional);
    }

    public Optional<Pedido> findByNotaFiscal(String notaFiscal){
        return pedidoRepository.findByNotaFiscal(notaFiscal);
    }
}
