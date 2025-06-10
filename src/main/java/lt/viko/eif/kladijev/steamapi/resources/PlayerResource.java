package lt.viko.eif.kladijev.steamapi.resources;

import lt.viko.eif.kladijev.steamapi.models.*;
import lt.viko.eif.kladijev.steamapi.repositories.*;
import lt.viko.eif.kladijev.steamapi.service.CommonMethodsService;
import lt.viko.eif.kladijev.steamapi.utility.NotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final CommonMethodsService commonMethodsService;

    public PlayerResource(PlayerRepository playerRepository, UserRepository userRepository, CommonMethodsService commonMethodsService)
    {
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
        this.commonMethodsService = commonMethodsService;
    }

    /**
     * Метод для получения игроком своего списка игр.
     * @param userDetails роль пользователя.
     * @return свой список игр.
     */
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

    /**
     * Метод для получения игроком своего списка достижений.
     * @param userDetails роль пользователя.
     * @return свой список достижений.
     */
    @GetMapping("/me/achievements")
    public List<Achievement> getMyAchievements(@AuthenticationPrincipal UserDetails userDetails)
    {
        String username = userDetails.getUsername();

        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Player player = user.getPlayer();
        if(player == null)
        {
            throw new NotFoundException("No Player entity attached to user " + username, 0L);
        }

        return player.getAchievements();
    }

    /**
     * Метод для получения игроком своего списка предметов.
     * @param userDetails роль пользователя.
     * @return свой список предметов.
     */
    @GetMapping("/me/items")
    public List<Item> getMyItems(@AuthenticationPrincipal UserDetails userDetails)
    {
        String username = userDetails.getUsername();

        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Player player = user.getPlayer();
        if(player == null)
        {
            throw new NotFoundException("No Player entity attached to user " + username, 0L);
        }

        return player.getItems();
    }
}
