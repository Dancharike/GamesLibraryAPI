package lt.viko.eif.kladijev.steamapi.resources;

import lt.viko.eif.kladijev.steamapi.dto.AdminDto;
import lt.viko.eif.kladijev.steamapi.dto.PlayerDto;
import lt.viko.eif.kladijev.steamapi.mappers.AdminMapper;
import lt.viko.eif.kladijev.steamapi.mappers.PlayerMapper;
import lt.viko.eif.kladijev.steamapi.models.*;
import lt.viko.eif.kladijev.steamapi.repositories.AdminRepository;
import lt.viko.eif.kladijev.steamapi.repositories.PlayerRepository;
import lt.viko.eif.kladijev.steamapi.service.CommonMethodsService;
import lt.viko.eif.kladijev.steamapi.utility.NotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Класс контроллер для управления игроками.
 * Есть все CRUD-методы и доступ к связанным сущностям.
 */
@RestController
@RequestMapping("api/admins")
@PreAuthorize("hasRole('ADMIN')")
public class AdminResource
{
    private final AdminRepository adminRepository;
    private final PlayerRepository playerRepository;
    private final CommonMethodsService commonMethodsService;

    public AdminResource(AdminRepository adminRepository, PlayerRepository playerRepository, CommonMethodsService commonMethodsService)
    {
        this.adminRepository = adminRepository;
        this.playerRepository = playerRepository;
        this.commonMethodsService = commonMethodsService;
    }

    /**
     * Метод для получения списка всех игроков с HATEOAS ссылками
     * @return коллекцию DTO игроков, обёрнутую в EntityModel
     */
    @GetMapping("/players")
    public CollectionModel<EntityModel<PlayerDto>> getAllPlayers()
    {
        var players = playerRepository.findAll().stream()
                .map(player -> {
                    PlayerDto dto = PlayerMapper.toDto(player);
                    EntityModel<PlayerDto> model = EntityModel.of(dto);
                    model.add(linkTo(methodOn(AdminResource.class).getPlayerById(player.getId())).withSelfRel());
                    model.add(linkTo(methodOn(AdminResource.class).updatePlayer(player.getId(), null)).withRel("update"));
                    model.add(linkTo(methodOn(AdminResource.class).deletePlayer(player.getId())).withRel("delete"));
                    model.add(linkTo(methodOn(AdminResource.class).getPlayerGames(player.getId())).withRel("games"));
                    model.add(linkTo(methodOn(AdminResource.class).getPlayerItems(player.getId())).withRel("items"));
                    model.add(linkTo(methodOn(AdminResource.class).getPlayerAchievements(player.getId())).withRel("achievements"));

                    return model;
                }).toList();

        return CollectionModel.of(players,
                linkTo(methodOn(AdminResource.class).getAllPlayers()).withSelfRel(),
                linkTo(methodOn(AdminResource.class).createPlayer(null)).withRel("create"));
    }

    /**
     * Метод для получения списка админов.
     * @return коллекцию DTO админов, обёрнутую в EntityModel
     */
    @GetMapping("/list")
    public CollectionModel<EntityModel<AdminDto>> getAllAdmins()
    {
        List<EntityModel<AdminDto>> admins = adminRepository.findAll().stream()
                .map(admin -> {
                    AdminDto dto = AdminMapper.toDto(admin);

                    return EntityModel.of(dto,
                            linkTo(methodOn(AdminResource.class).getAdminById(admin.getId())).withSelfRel(),
                            linkTo(methodOn(AdminResource.class).updateAdmin(admin.getId(), null)).withRel("update"),
                            linkTo(methodOn(AdminResource.class).deleteAdmin(admin.getId())).withRel("delete"));
                })
                .toList();

        return CollectionModel.of(admins,
                linkTo(methodOn(AdminResource.class).getAllAdmins()).withSelfRel(),
                linkTo(methodOn(AdminResource.class).createAdmin(null)).withRel("create"));
    }

    /**
     * Метод для получения игрока по его специфическому ID.
     * @param id ID игрока.
     * @return DTO игрока с HATEOAS ссылками.
     */
    @GetMapping("/players/{id}")
    public EntityModel<PlayerDto> getPlayerById(@PathVariable Long id)
    {
        Player player = playerRepository.findById(id).orElseThrow(() -> new NotFoundException("Player", id));

        PlayerDto dto = PlayerMapper.toDto(player);
        EntityModel<PlayerDto> model = EntityModel.of(dto);
        model.add(linkTo(methodOn(AdminResource.class).getPlayerById(id)).withSelfRel());
        model.add(linkTo(methodOn(AdminResource.class).updatePlayer(id, null)).withRel("update"));
        model.add(linkTo(methodOn(AdminResource.class).deletePlayer(id)).withRel("delete"));
        model.add(linkTo(methodOn(AdminResource.class).getPlayerGames(id)).withRel("games"));
        model.add(linkTo(methodOn(AdminResource.class).getPlayerItems(id)).withRel("items"));
        model.add(linkTo(methodOn(AdminResource.class).getPlayerAchievements(id)).withRel("achievements"));

        return model;
    }

    /**
     * Метод для получения админа со специфическим ID.
     * @param id ID админа.
     * @return DTO админа с HATEOAS ссылками.
     */
    @GetMapping("/{id}")
    public EntityModel<AdminDto> getAdminById(@PathVariable Long id)
    {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new NotFoundException("Admin", id));

        AdminDto dto = AdminMapper.toDto(admin);

        return EntityModel.of(dto,
                linkTo(methodOn(AdminResource.class).getAdminById(id)).withSelfRel(),
                linkTo(methodOn(AdminResource.class).getAllAdmins()).withRel("all-admins"),
                linkTo(methodOn(AdminResource.class).updateAdmin(id, null)).withRel("update"),
                linkTo(methodOn(AdminResource.class).deleteAdmin(id)).withRel("delete"));

    }

    /**
     * Метод для получения списка игр игрока со специфическим ID.
     * @param id ID игрока.
     * @return список игр.
     */
    @GetMapping("/players/{id}/games")
    public CollectionModel<EntityModel<Game>> getPlayerGames(@PathVariable Long id)
    {
        Player player = playerRepository.findById(id).orElseThrow(() -> new NotFoundException("Player", id));

        List<EntityModel<Game>> games = player.getGames().stream()
                .map(game -> EntityModel.of(game,
                        linkTo(methodOn(GameResource.class).getGameById(game.getId())).withSelfRel(),
                        linkTo(methodOn(GameResource.class).deleteGame(game.getId())).withRel("delete")))
                .toList();

        return CollectionModel.of(games, linkTo(methodOn(AdminResource.class).getPlayerGames(id)).withSelfRel());
    }

    /**
     * Метод для получения списка достижений игрока со специфическим ID.
     * @param id ID игрока.
     * @return список достижений.
     */
    @GetMapping("/players/{id}/achievements")
    public CollectionModel<EntityModel<Achievement>> getPlayerAchievements(@PathVariable Long id)
    {
        Player player = playerRepository.findById(id).orElseThrow(() -> new NotFoundException("Player", id));

        List<EntityModel<Achievement>> achievements = player.getAchievements().stream()
                .map(a -> EntityModel.of(a,
                        linkTo(methodOn(AchievementResource.class).getAchievementById(a.getId())).withSelfRel(),
                        linkTo(methodOn(AchievementResource.class).deleteAchievement(a.getId())).withRel("delete")))
                .toList();

        return CollectionModel.of(achievements, linkTo(methodOn(AdminResource.class).getPlayerAchievements(id)).withSelfRel());
    }

    /**
     * Метод для получения списка предметов игрока со специфическим ID.
     * @param id ID игрока.
     * @return список предметов.
     */
    @GetMapping("/players/{id}/items")
    public CollectionModel<EntityModel<Item>> getPlayerItems(@PathVariable Long id)
    {
        Player player = playerRepository.findById(id).orElseThrow(() -> new NotFoundException("Player", id));

        List<EntityModel<Item>> items = player.getItems().stream()
                .map(item -> EntityModel.of(item,
                        linkTo(methodOn(ItemResource.class).getItemById(item.getId())).withSelfRel(),
                        linkTo(methodOn(ItemResource.class).deleteItem(item.getId())).withRel("delete")))
                .toList();

        return CollectionModel.of(items, linkTo(methodOn(AdminResource.class).getPlayerItems(id)).withSelfRel());
    }

    /**
     * Метод для получения игр игрока по его специфическому имени.
     * @param name имя игрока.
     * @return список игр указанного игрока.
     */
    @GetMapping("/players/name/{name}/games")
    public List<Game> getGamesByPlayerName(@PathVariable String name)
    {
        Player player = commonMethodsService.findPlayerByNickname(name);
        return player.getGames();
    }

    /**
     * Метод для получения достижений игрока по его специфическому имени.
     * @param name имя игрока.
     * @return список достижений указанного игрока.
     */
    @GetMapping("/players/name/{name}/achievements")
    public List<Achievement> getAchievementByPlayerName(@PathVariable String name)
    {
        Player player = commonMethodsService.findPlayerByNickname(name);
        return player.getAchievements();
    }

    /**
     * Метод для получения предметов игрока по его специфическому имени.
     * @param name имя игрока.
     * @return список предметов указанного игрока.
     */
    @GetMapping("/players/name/{name}/items")
    public List<Item> getItemsByPlayerName(@PathVariable String name)
    {
        Player player = commonMethodsService.findPlayerByNickname(name);
        return player.getItems();
    }

    /**
     * Метод для создания нового игрока.
     * @param player объект игрока.
     * @return созданный игрок с HATEOAS ссылками.
     */
    @PostMapping("/create-players")
    public EntityModel<PlayerDto> createPlayer(@RequestBody Player player)
    {
        Player savedPlayer = playerRepository.save(player);
        PlayerDto dto = PlayerMapper.toDto(savedPlayer);

        return EntityModel.of(dto,
                linkTo(methodOn(AdminResource.class).getPlayerById(savedPlayer.getId())).withSelfRel(),
                linkTo(methodOn(AdminResource.class).getAllPlayers()).withRel("all-players"));
    }

    /**
     * Метод для создания нового админа.
     * @param admin объект админа.
     * @return созданный админ с HATEOAS ссылками.
     */
    @PostMapping("/create-admins")
    public EntityModel<AdminDto> createAdmin(@RequestBody Admin admin)
    {
        Admin savedAdmin = adminRepository.save(admin);
        AdminDto dto = AdminMapper.toDto(savedAdmin);

        return EntityModel.of(dto,
                linkTo(methodOn(AdminResource.class).getAdminById(savedAdmin.getId())).withSelfRel(),
                linkTo(methodOn(AdminResource.class).getAllAdmins()).withRel("all-admins"));
    }

    /**
     * Метод для обновления информации об игроке со специфическим ID.
     * @param id ID игрока.
     * @param updatedPlayer новые данные игрока.
     * @return обновлённый игрок.
     */
    @PutMapping("/players/{id}/update")
    public EntityModel<PlayerDto> updatePlayer(@PathVariable Long id, @RequestBody Player updatedPlayer)
    {
        Player player = playerRepository.findById(id).orElseThrow(() -> new NotFoundException("Player", id));

        player.setNickName(updatedPlayer.getNickName());
        player.setEmail(updatedPlayer.getEmail());
        player.setLevel(updatedPlayer.getLevel());
        player.setExperience(updatedPlayer.getExperience());

        Player saved = playerRepository.save(player);
        PlayerDto dto = PlayerMapper.toDto(saved);

        return EntityModel.of(dto, linkTo(methodOn(AdminResource.class).getPlayerById(saved.getId())).withSelfRel());
    }

    /**
     * Метод для обновления данных админа.
     * @param id ID админа.
     * @param updatedAdmin новые данные админа.
     * @return обновлённый админ.
     */
    @PutMapping("/{id}/update")
    public EntityModel<AdminDto> updateAdmin(@PathVariable Long id, @RequestBody Admin updatedAdmin)
    {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new NotFoundException("Admin", id));

        admin.setNickName(updatedAdmin.getNickName());
        admin.setEmail(updatedAdmin.getEmail());
        admin.setLevel(updatedAdmin.getLevel());
        admin.setExperience(updatedAdmin.getExperience());

        Admin saved = adminRepository.save(admin);
        AdminDto dto = AdminMapper.toDto(saved);

        return EntityModel.of(dto, linkTo(methodOn(AdminResource.class).getAdminById(saved.getId())).withSelfRel());
    }

    /**
     * Метод для удаления игрока.
     * @param id ID игрока.
     * @return код 204 No Content.
     */
    @DeleteMapping("/players/{id}/delete")
    public ResponseEntity<?> deletePlayer(@PathVariable Long id)
    {
        playerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Метод для удаления админа.
     * @param id ID админа.
     * @return код 204 No Content.
     */
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id)
    {
        adminRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
