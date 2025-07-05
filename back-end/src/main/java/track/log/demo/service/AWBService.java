package track.log.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import track.log.demo.model.AWB;
import track.log.demo.repository.AWBRepository;
import track.log.demo.specification.AWBSpecification;

import java.util.Optional;

@Service
public class AWBService {

    private final AWBRepository awbRepository;

    public AWBService(AWBRepository awbRepository){
        this.awbRepository = awbRepository;
    }

    /** Usando o numeroOperacional procura por AWB na base de dados
     * , se não for encontrada, então ela será criada*/
    public AWB findOrCreateByNumeroOperacional(String numeroOperacional) {
        return awbRepository.findByNumeroOperacional(numeroOperacional)
                .orElseGet(() -> {
                    AWB awb = new AWB(numeroOperacional);
                    awb.setRecebida(false); // padrão
                    awb.setDataDeRecebimento(null); // Valor atribuido através de registrarRecebimento
                    awb.setColaborador(null); // Valor atribuido através de registrarRecebimento
                    return awbRepository.save(awb);
                });
    }

    public Optional<AWB> findByNumeroOperacional(String numeroOperacional){
        return awbRepository.findByNumeroOperacional(numeroOperacional);
    }

    public AWB save(AWB awb){
        return  awbRepository.save(awb);
    }

    public Page<AWB> buscarAWBComFiltros(String numeroOperacional, Boolean recebida, Pageable pageable) {
        Specification<AWB> spec = Specification
                .where(AWBSpecification.comNumeroOperacional(numeroOperacional))
                .and(AWBSpecification.comRecebida(recebida));

        return awbRepository.findAll(spec, pageable);
    }
}
