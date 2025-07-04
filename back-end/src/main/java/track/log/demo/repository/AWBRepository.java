package track.log.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import track.log.demo.model.AWB;

import java.util.Optional;

public interface AWBRepository extends JpaRepository<AWB, Long> {
    Optional<AWB> findByNumeroOperacional(String numeroOperacional);
}
