package track.log.demo.specification;

import org.springframework.data.jpa.domain.Specification;
import track.log.demo.model.AWB;

public class AWBSpecification {
    public static Specification<AWB> comNumeroOperacional(String numero) {
        return (root, query, criteriaBuilder) -> {
            if (numero == null || numero.isBlank()) return null;
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("numeroOperacional")),
                    "%" + numero.toLowerCase() + "%");
        };
    }

    public static Specification<AWB> comRecebida(Boolean recebida) {
        return (root, query, cb) -> {
            if (recebida == null) return null;
            return cb.equal(root.get("recebida"), recebida);
        };
    }
}
