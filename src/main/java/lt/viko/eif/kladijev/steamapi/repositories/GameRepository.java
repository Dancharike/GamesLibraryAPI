package lt.viko.eif.kladijev.steamapi.repositories;

import lt.viko.eif.kladijev.steamapi.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {}
