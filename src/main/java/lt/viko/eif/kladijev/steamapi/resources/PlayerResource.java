package lt.viko.eif.kladijev.steamapi.resources;

import lt.viko.eif.kladijev.steamapi.dto.*;
import lt.viko.eif.kladijev.steamapi.models.*;
import lt.viko.eif.kladijev.steamapi.repositories.*;
import lt.viko.eif.kladijev.steamapi.mappers.PlayerMapper;
import lt.viko.eif.kladijev.steamapi.utility.NotFoundException;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Класс контроллер для управления игроками.
 * Есть все CRUD-методы и доступ к связанным сущностям.
 */
@RestController
@RequestMapping("api/players")
public class PlayerResource
{
    private final PlayerRepository playerRepository;

    public PlayerResource(PlayerRepository playerRepository)
    {
        this.playerRepository = playerRepository;
    }

    /**
     * Метод для получения списка всех игроков с HATEOAS ссылками
     * @return коллекцию DTO игроков, обёрнутую в EntityModel
     */
    @GetMapping
    public CollectionModel<EntityModel<PlayerDto>> getAllPlayers()
    {
        var players = playerRepository.findAll().stream()
                .map(player -> {
                    PlayerDto dto = PlayerMapper.toDto(player);
                    EntityModel<PlayerDto> model = EntityModel.of(dto);
                    model.add(linkTo(methodOn(PlayerResource.class).getPlayerById(player.getId())).withSelfRel());
                    model.add(linkTo(methodOn(PlayerResource.class).deletePlayer(player.getId())).withRel("delete"));
                    model.add(linkTo(methodOn(PlayerResource.class).getPlayerGames(player.getId())).withRel("games"));
                    model.add(linkTo(methodOn(PlayerResource.class).getPlayerItems(player.getId())).withRel("items"));
                    model.add(linkTo(methodOn(PlayerResource.class).getPlayerAchievements(player.getId())).withRel("achievements"));

                    return model;
                }).toList();

        return CollectionModel.of(players, linkTo(methodOn(PlayerResource.class).getAllPlayers()).withSelfRel());
    }

    /**
     * Метод для получения игрока по его специфическому ID.
     * @param id ID игрока.
     * @return DTO игрока с HATEOAS ссылками.
     */
    @GetMapping("/{id}")
    public EntityModel<PlayerDto> getPlayerById(@PathVariable Long id)
    {
        Player player = playerRepository.findById(id).orElseThrow(() -> new NotFoundException("Player", id));

        PlayerDto dto = PlayerMapper.toDto(player);
        EntityModel<PlayerDto> model = EntityModel.of(dto);
        model.add(linkTo(methodOn(PlayerResource.class).getPlayerById(id)).withSelfRel());
        model.add(linkTo(methodOn(PlayerResource.class).deletePlayer(id)).withRel("delete"));
        model.add(linkTo(methodOn(PlayerResource.class).getPlayerGames(id)).withRel("games"));
        model.add(linkTo(methodOn(PlayerResource.class).getPlayerItems(id)).withRel("items"));
        model.add(linkTo(methodOn(PlayerResource.class).getPlayerAchievements(id)).withRel("achievements"));

        return model;
    }

    /**
     * Метод для получения списка игр игрока со специфическим ID.
     * @param id ID игрока.
     * @return список игр.
     */
    @GetMapping("/{id}/games")
    public List<?> getPlayerGames(@PathVariable Long id)
    {
        return playerRepository.findById(id)
                .map(Player::getGames)
                .orElseThrow(() -> new RuntimeException("Player not found"));
    }

    /**
     * Метод для получения списка достижений игрока со специфическим ID.
     * @param id ID игрока.
     * @return список достижений.
     */
    @GetMapping("/{id}/achievements")
    public List<?> getPlayerAchievements(@PathVariable Long id)
    {
        return playerRepository.findById(id)
                .map(Player::getAchievements)
                .orElseThrow(() -> new RuntimeException("Player not found"));
    }

    /**
     * Метод для получения списка предметов игрока со специфическим ID.
     * @param id ID игрока.
     * @return список предметов.
     */
    @GetMapping("/{id}/items")
    public List<?> getPlayerItems(@PathVariable Long id)
    {
        return playerRepository.findById(id)
                .map(Player::getItems)
                .orElseThrow(() -> new RuntimeException("Player not found"));
    }

    /**
     * Метод для создания нового игрока.
     * @param player объект игрока.
     * @return созданный игрок с HATEOAS ссылками.
     */
    @PostMapping
    public EntityModel<PlayerDto> createPlayer(@RequestBody Player player)
    {
        Player savedPlayer = playerRepository.save(player);
        PlayerDto dto = PlayerMapper.toDto(savedPlayer);

        return EntityModel.of(dto,
                linkTo(methodOn(PlayerResource.class).getPlayerById(savedPlayer.getId())).withSelfRel(),
                linkTo(methodOn(PlayerResource.class).getAllPlayers()).withRel("players"));
    }

    /**
     * Метод для обновления информации об игроке со специфическим ID.
     * @param id ID игрока.
     * @param updatedPlayer новые данные игрока.
     * @return обновлённый игрок.
     */
    @PutMapping("/{id}")
    public EntityModel<PlayerDto> updatePlayer(@PathVariable Long id, @RequestBody Player updatedPlayer)
    {
        Player player = playerRepository.findById(id).orElseThrow(() -> new RuntimeException("Player not found"));

        player.setNickName(updatedPlayer.getNickName());
        player.setEmail(updatedPlayer.getEmail());
        player.setLevel(updatedPlayer.getLevel());
        player.setExperience(updatedPlayer.getExperience());

        Player saved = playerRepository.save(player);
        PlayerDto dto = PlayerMapper.toDto(saved);

        return EntityModel.of(dto, linkTo(methodOn(PlayerResource.class).getPlayerById(saved.getId())).withSelfRel());
    }

    /**
     * Метод для удаления игрока.
     * @param id ID игрока.
     * @return код 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable Long id)
    {
        playerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
