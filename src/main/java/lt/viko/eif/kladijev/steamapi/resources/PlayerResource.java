package lt.viko.eif.kladijev.steamapi.resources;

import lt.viko.eif.kladijev.steamapi.dto.PlayerDto;
import lt.viko.eif.kladijev.steamapi.dto.RegisterRequest;
import lt.viko.eif.kladijev.steamapi.mappers.PlayerMapper;
import lt.viko.eif.kladijev.steamapi.models.*;
import lt.viko.eif.kladijev.steamapi.repositories.*;
import lt.viko.eif.kladijev.steamapi.service.CommonMethodsService;
import lt.viko.eif.kladijev.steamapi.utility.NotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    public CollectionModel<EntityModel<Game>> getMyGames(@AuthenticationPrincipal UserDetails userDetails)
    {
        String username = userDetails.getUsername();

        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Player player = user.getPlayer();
        if(player == null)
        {
            throw new NotFoundException("No Player entity attached to user " + username, 0L);
        }

        List<EntityModel<Game>> games = player.getGames().stream()
                .map(game -> EntityModel.of(game,
                        linkTo(methodOn(GameResource.class).getGameById(game.getId())).withSelfRel(),
                        linkTo(methodOn(GameResource.class).updateGame(game.getId(), null)).withRel("update"),
                        linkTo(methodOn(GameResource.class).deleteGame(game.getId())).withRel("delete")))
                .toList();

        //return player.getGames();
        return CollectionModel.of(games, linkTo(methodOn(PlayerResource.class).getMyGames(userDetails)).withSelfRel());
    }

    /**
     * Метод для получения игроком своего списка достижений.
     * @param userDetails роль пользователя.
     * @return свой список достижений.
     */
    @GetMapping("/me/achievements")
    public CollectionModel<EntityModel<Achievement>> getMyAchievements(@AuthenticationPrincipal UserDetails userDetails)
    {
        String username = userDetails.getUsername();

        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Player player = user.getPlayer();
        if(player == null)
        {
            throw new NotFoundException("No Player entity attached to user " + username, 0L);
        }

        List<EntityModel<Achievement>> ach = player.getAchievements().stream()
                .map(a -> EntityModel.of(a,
                        linkTo(methodOn(AchievementResource.class).getAchievementById(a.getId())).withSelfRel(),
                        linkTo(methodOn(AchievementResource.class).updateAchievement(a.getId(), null)).withRel("update"),
                        linkTo(methodOn(AchievementResource.class).deleteAchievement(a.getId())).withRel("delete")))
                .toList();

        //return player.getAchievements();
        return CollectionModel.of(ach, linkTo(methodOn(PlayerResource.class).getMyAchievements(userDetails)).withSelfRel());
    }

    /**
     * Метод для получения игроком своего списка предметов.
     * @param userDetails роль пользователя.
     * @return свой список предметов.
     */
    @GetMapping("/me/items")
    public CollectionModel<EntityModel<Item>> getMyItems(@AuthenticationPrincipal UserDetails userDetails)
    {
        String username = userDetails.getUsername();

        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Player player = user.getPlayer();
        if(player == null)
        {
            throw new NotFoundException("No Player entity attached to user " + username, 0L);
        }

        List<EntityModel<Item>> items = player.getItems().stream()
                .map(item -> EntityModel.of(item,
                        linkTo(methodOn(ItemResource.class).getItemById(item.getId())).withSelfRel(),
                        linkTo(methodOn(ItemResource.class).updateItem(item.getId(), null)).withRel("update"),
                        linkTo(methodOn(ItemResource.class).deleteItem(item.getId())).withRel("delete")))
                .toList();

        //return player.getItems();
        return CollectionModel.of(items, linkTo(methodOn(PlayerResource.class).getMyItems(userDetails)).withSelfRel());
    }

    /**
     * Метод был реализован, но не получилось грамотно его встроить в тесты из-за нагромождения говно-кода!
     * Метод для регистрации нового игрока.
     * @param request запрос ввода.
     * @return созданного игрока.
     */
    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> registerPlayer(@RequestBody RegisterRequest request)
    {
        if(userRepository.existsByUsername(request.username))
        {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }

        AppUser user = new AppUser();
        user.setUsername(request.username);
        user.setPassword(new BCryptPasswordEncoder().encode(request.password));
        user.setRole(UserRole.ROLE_PLAYER);

        Player player = new Player();
        player.setNickName(request.username);
        player.setEmail(request.email);
        user.setPlayer(player);

        userRepository.save(user);
        playerRepository.save(player);

        return ResponseEntity.ok("Player registered successfully");
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
                linkTo(methodOn(PlayerResource.class).getMe(userDetails)).withSelfRel(),
                linkTo(methodOn(PlayerResource.class).getMyGames(userDetails)).withRel("my-games"),
                linkTo(methodOn(PlayerResource.class).getMyAchievements(userDetails)).withRel("my-achievements"),
                linkTo(methodOn(PlayerResource.class).getMyItems(userDetails)).withRel("my-items"),
                linkTo(methodOn(PlayerResource.class).updateMe(null, null)).withRel("update-me"),
                linkTo(methodOn(PlayerResource.class).deleteMe(userDetails)).withRel("delete-me"));
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
