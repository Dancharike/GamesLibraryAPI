package lt.viko.eif.kladijev.steamapi.repositories;

import lt.viko.eif.kladijev.steamapi.models.Admin;
import lt.viko.eif.kladijev.steamapi.models.AppUser;
import lt.viko.eif.kladijev.steamapi.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long>
{
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByAdmin(Admin admin);
    Optional<AppUser> findByPlayer(Player player);
    boolean existsByUsername(String username);
}
