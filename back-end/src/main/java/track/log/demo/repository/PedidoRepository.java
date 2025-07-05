package track.log.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import track.log.demo.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long>, JpaSpecificationExecutor<Pedido> {
    Optional<Pedido> findByNotaFiscal(String notaFiscal);
    Optional<Pedido> findByCteAndNotaFiscal(String cte, String notaFiscal);
}
