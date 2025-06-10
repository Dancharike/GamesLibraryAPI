package lt.viko.eif.kladijev.steamapi.resources;

import lt.viko.eif.kladijev.steamapi.dto.PlayerDto;
import lt.viko.eif.kladijev.steamapi.mappers.PlayerMapper;
import lt.viko.eif.kladijev.steamapi.models.*;
import lt.viko.eif.kladijev.steamapi.repositories.*;
import lt.viko.eif.kladijev.steamapi.service.CommonMethodsService;
import lt.viko.eif.kladijev.steamapi.utility.NotFoundException;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
     * Метод для нахождения игроком самого себя.
     * @param userDetails данные игрока.
     * @return коллекцию DTO игроков, обёрнутую в EntityModel.
     */
    @GetMapping("/me")
    public EntityModel<PlayerDto> getMe(@AuthenticationPrincipal UserDetails userDetails)
    {
        String currentUsername = userDetails.getUsername();
        Player player = commonMethodsService.findPlayerByNickname(currentUsername);

        if(player == null)
        {
            throw new NotFoundException("No Player entity attached to user " + currentUsername, 0L);
        }

        return EntityModel.of(PlayerMapper.toDto(player),
            linkTo(methodOn(PlayerResource.class).getMe(null)).withSelfRel(),
            linkTo(methodOn(PlayerResource.class).getMyGames(null)).withRel("my-games"),
            linkTo(methodOn(PlayerResource.class).getMyAchievements(null)).withRel("my-achievements"),
            linkTo(methodOn(PlayerResource.class).getMyItems(null)).withRel("my-items"),
            linkTo(methodOn(PlayerResource.class).updateMe(null, null)).withRel("update-me")
        );
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

    /**
     * Метод для обновления своих данных игроком самостоятельно.
     * @param userDetails данные игрока.
     * @param updatedPlayer новые данные.
     * @return обновлённый игрок.
     */
    @PutMapping("/me/update")
    public EntityModel<PlayerDto> updateMe(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Player updatedPlayer)
    {
        String currentUsername = userDetails.getUsername();
        Player currentUser = commonMethodsService.findPlayerByNickname(currentUsername);

        if(currentUser == null)
        {
            throw new NotFoundException("No Player entity attached to user " + currentUsername, 0L);
        }

        if(updatedPlayer.getNickName() != null)
        {
            currentUser.setNickName(updatedPlayer.getNickName());
        }
        if(updatedPlayer.getEmail() != null)
        {
            currentUser.setEmail(updatedPlayer.getEmail());
        }

        Player savedPlayer = playerRepository.save(currentUser);
        PlayerDto dto = PlayerMapper.toDto(savedPlayer);

        return EntityModel.of(dto,
                linkTo(methodOn(PlayerResource.class).getMyGames(userDetails)).withRel("my-games"),
                linkTo(methodOn(PlayerResource.class).getMyAchievements(userDetails)).withRel("my-achievements"),
                linkTo(methodOn(PlayerResource.class).getMyItems(userDetails)).withRel("my-items"),
                linkTo(methodOn(PlayerResource.class).updateMe(null, null)).withRel("update-me")
        );
    }

    /**
     * Метод для само-удаления игроком себя.
     * @param userDetails данные игрока.
     * @return код 204 No Content.
     */
    @DeleteMapping("/me/delete")
    public ResponseEntity<?> deleteMe(@AuthenticationPrincipal UserDetails userDetails)
    {
        String username = userDetails.getUsername();

        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Player player = user.getPlayer();

        if(player == null)
        {
            throw new NotFoundException("No Player entity attached to user " + username, 0L);
        }

        user.setPlayer(null);
        userRepository.save(user);
        playerRepository.delete(player);
        userRepository.delete(user);

        return ResponseEntity.noContent().build();
    }
}
