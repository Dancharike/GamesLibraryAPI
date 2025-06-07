package lt.viko.eif.kladijev.steamapi.repositories;

import lt.viko.eif.kladijev.steamapi.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long>
{
    Optional<AppUser> findByUsername(String username);
}
