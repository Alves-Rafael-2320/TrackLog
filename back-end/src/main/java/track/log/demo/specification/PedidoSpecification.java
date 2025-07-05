package track.log.demo.specification;

import org.springframework.data.jpa.domain.Specification;
import track.log.demo.model.Pedido;
/** Filtros avançados e ordenação de requests.*/
public class PedidoSpecification {

    public static Specification<Pedido> comNotaFiscal(String notaFiscal) {
        return (root, query, cb) ->
                notaFiscal == null ? null : cb.like(cb.lower(root.get("notaFiscal")),
                        "%" + notaFiscal.toLowerCase() + "%");
    }

    public static Specification<Pedido> comNumeroOperacional(String numeroOperacional) {
        return (root, query, cb) ->
                numeroOperacional == null ? null : cb.like(cb.lower(root.get("numeroOperacional")),
                        "%" + numeroOperacional.toLowerCase() + "%");
    }

    public static Specification<Pedido> comDestinatario(String destinatario) {
        return (root, query, cb) ->
                destinatario == null ? null : cb.like(cb.lower(root.get("destinatario")),
                        "%" + destinatario.toLowerCase() + "%");
    }

    public static Specification<Pedido> comCidadeOrigem(String cidadeOrigem) {
        return (root, query, cb) ->
                cidadeOrigem == null ? null : cb.like(cb.lower(root.get("cidadeOrigem")),
                        "%" + cidadeOrigem.toLowerCase() + "%");
    }

    public static Specification<Pedido> comCidadeDestino(String cidadeDestino) {
        return (root, query, cb) ->
                cidadeDestino == null ? null : cb.like(cb.lower(root.get("cidadeDestino")),
                        "%" + cidadeDestino.toLowerCase() + "%");
    }
}
