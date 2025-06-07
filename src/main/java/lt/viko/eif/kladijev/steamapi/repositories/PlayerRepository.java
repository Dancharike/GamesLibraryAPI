package lt.viko.eif.kladijev.steamapi.repositories;

import lt.viko.eif.kladijev.steamapi.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {}
