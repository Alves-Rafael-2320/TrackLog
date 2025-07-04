package track.log.demo.service;

import track.log.demo.model.AWB;
import track.log.demo.model.Pedido;
import track.log.demo.repository.PedidoRepository;
import track.log.demo.specification.PedidoSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**Serviço responsável por operações CRUD e buscas com filtros para a entidade Pedido.*/
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final AWBService awbService;

    public PedidoService(PedidoRepository pedidoRepository, AWBService awbService) {
        this.pedidoRepository = pedidoRepository;
        this.awbService = awbService;
    }

    /**Salva um pedido no banco, associando a uma AWB existente ou criando uma nova com base no número operacional.*/
    public Pedido salvarPedido(Pedido pedido){
        if (pedido.getNumeroOperacional() != null && !pedido.getNumeroOperacional().isEmpty()){
            AWB awb = awbService.findOrCreateByNumeroOperacional(pedido.getNumeroOperacional());
            pedido.setAwb(awb);
        }
        return pedidoRepository.save(pedido);
    }

    /**Busca um pedido pelo número da nota fiscal.*/
    public Optional<Pedido> findByNotaFiscal(String notaFiscal){
        return pedidoRepository.findByNotaFiscal(notaFiscal);
    }

    /**Busca pedidos aplicando filtros opcionais com paginação.*/
    public Page<Pedido> buscarPedidosComFiltros(
            String notaFiscal,
            String numeroOperacional,
            String destinatario,
            String cidadeOrigem,
            String cidadeDestino,
            Pageable pageable
    ) {
        Specification<Pedido> spec = Specification
                .where(PedidoSpecification.comNotaFiscal(notaFiscal))
                .and(PedidoSpecification.comNumeroOperacional(numeroOperacional))
                .and(PedidoSpecification.comDestinatario(destinatario))
                .and(PedidoSpecification.comCidadeOrigem(cidadeOrigem))
                .and(PedidoSpecification.comCidadeDestino(cidadeDestino));

        return pedidoRepository.findAll(spec, pageable);
    }
}
