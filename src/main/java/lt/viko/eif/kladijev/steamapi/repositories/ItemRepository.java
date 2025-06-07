package lt.viko.eif.kladijev.steamapi.repositories;

import lt.viko.eif.kladijev.steamapi.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {}
