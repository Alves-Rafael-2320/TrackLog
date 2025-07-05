package track.log.demo.specification;

import org.springframework.data.jpa.domain.Specification;
import track.log.demo.model.AWB;

/**Especificações para filtragem de AWB na consulta dinâmica.*/
public class AWBSpecification {

    /**
     * Filtra AWB pelo número operacional, usando LIKE ignorando maiúsculas/minúsculas.
     * @param numero Número operacional para filtro
     * @return Specification para filtro por número operacional ou null se parâmetro inválido
     */
    public static Specification<AWB> comNumeroOperacional(String numero) {
        return (root, query, criteriaBuilder) -> {
            if (numero == null || numero.isBlank()) return null;
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("numeroOperacional")),
                    "%" + numero.toLowerCase() + "%"
            );
        };
    }

    /**
     * Filtra AWB pelo status de recebida.
     * @param recebida Boolean indicando se a AWB foi recebida ou não
     * @return Specification para filtro por recebida ou null se parâmetro for null
     */
    public static Specification<AWB> comRecebida(Boolean recebida) {
        return (root, query, cb) -> {
            if (recebida == null) return null;
            return cb.equal(root.get("recebida"), recebida);
        };
    }
}
