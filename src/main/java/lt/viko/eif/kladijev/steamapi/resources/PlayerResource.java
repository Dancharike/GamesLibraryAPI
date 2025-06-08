package lt.viko.eif.kladijev.steamapi.resources;

import lt.viko.eif.kladijev.steamapi.dto.*;
import lt.viko.eif.kladijev.steamapi.models.*;
import lt.viko.eif.kladijev.steamapi.repositories.*;
import lt.viko.eif.kladijev.steamapi.mappers.PlayerMapper;
import lt.viko.eif.kladijev.steamapi.utility.NotFoundException;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Класс контроллер для управления игроками.
 * Есть все CRUD-методы и доступ к связанным сущностям.
 */
@RestController
@RequestMapping("api/players")
@PreAuthorize("hasRole('PLAYER')")
public class PlayerResource
{
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;

    public PlayerResource(PlayerRepository playerRepository, UserRepository userRepository)
    {
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/me/games")
    public List<Game> getMyGames(@AuthenticationPrincipal UserDetails userDetails)
    {
        String username = userDetails.getUsername();

        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Player player = user.getPlayer();
        if(player == null)
        {
            throw new NotFoundException("No Player entity attached to user " + username, 0L);
        }

        return player.getGames();
    }
}
