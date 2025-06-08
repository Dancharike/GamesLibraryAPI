package lt.viko.eif.kladijev.steamapi.resources;

import lt.viko.eif.kladijev.steamapi.dto.AchievementDto;
import lt.viko.eif.kladijev.steamapi.dto.GameDto;
import lt.viko.eif.kladijev.steamapi.dto.ItemDto;
import lt.viko.eif.kladijev.steamapi.mappers.AchievementMapper;
import lt.viko.eif.kladijev.steamapi.mappers.GameMapper;
import lt.viko.eif.kladijev.steamapi.mappers.ItemMapper;
import lt.viko.eif.kladijev.steamapi.models.Game;
import lt.viko.eif.kladijev.steamapi.repositories.GameRepository;
import lt.viko.eif.kladijev.steamapi.utility.NotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Класс контроллер для управления играми.
 * Присутствуют CRUD-методы и HATEOAS ссылки.
 */
@RestController
@RequestMapping("api/games")
public class GameResource
{
    private final GameRepository gameRepository;

    public GameResource(GameRepository gameRepository)
    {
        this.gameRepository = gameRepository;
    }

    /**
     * Метод для получения списка всех игр.
     * @return коллекция DTO игр со ссылками.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CollectionModel<EntityModel<GameDto>> getAllGames()
    {
        var games = gameRepository.findAll().stream()
                .map(game -> {
                    GameDto dto = GameMapper.toDto(game);
                    Long gameId = game.getId();

                    var model = EntityModel.of(dto);
                    model.add(linkTo(methodOn(GameResource.class).getGameById(gameId)).withSelfRel());
                    model.add(linkTo(methodOn(GameResource.class).updateGame(gameId, null)).withRel("update"));
                    model.add(linkTo(methodOn(GameResource.class).deleteGame(gameId)).withRel("delete"));
                    model.add(linkTo(methodOn(GameResource.class).getGameAchievements(gameId)).withRel("achievements"));
                    model.add(linkTo(methodOn(GameResource.class).getGameItems(gameId)).withRel("items"));

                    return model;
                }).toList();

        return CollectionModel.of(games,
                linkTo(methodOn(GameResource.class).getAllGames()).withSelfRel(),
                linkTo(methodOn(GameResource.class).createGame(null)).withRel("create"));
    }

    /**
     * Метод для получения игры со специфическим ID.
     * @param id ID игры.
     * @return DTO игры с HATEOAS ссылками.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public EntityModel<GameDto> getGameById(@PathVariable Long id)
    {
        Game game = gameRepository.findById(id).orElseThrow(() -> new NotFoundException("Game", id));
        GameDto dto = GameMapper.toDto(game);

        var model = EntityModel.of(dto);
        model.add(linkTo(methodOn(GameResource.class).getGameById(id)).withSelfRel());
        model.add(linkTo(methodOn(GameResource.class).updateGame(id, null)).withRel("update"));
        model.add(linkTo(methodOn(GameResource.class).deleteGame(id)).withRel("delete"));
        model.add(linkTo(methodOn(GameResource.class).getGameAchievements(id)).withRel("achievements"));
        model.add(linkTo(methodOn(GameResource.class).getGameItems(id)).withRel("items"));
        model.add(linkTo(methodOn(GameResource.class).getAllGames()).withRel("all-games"));

        return model;
    }

    /**
     * Метод для получения достижений, что привязаны к игре.
     * @param id ID игры.
     * @return список DTO достижений.
     */
    @GetMapping("/{id}/achievements")
    public List<AchievementDto> getGameAchievements(@PathVariable Long id)
    {
        Game game = gameRepository.findById(id).orElseThrow(() -> new RuntimeException("Game not found"));

        return game.getAchievements().stream()
                .map(AchievementMapper::toDto)
                .toList();
    }

    /**
     * Метод для получения предметов, что привязаны к игре.
     * @param id ID предмета.
     * @return список DTO предметов.
     */
    @GetMapping("/{id}/items")
    public List<ItemDto> getGameItems(@PathVariable Long id)
    {
        Game game = gameRepository.findById(id).orElseThrow(() -> new RuntimeException("Game not found"));

        return game.getItems().stream()
                .map(ItemMapper::toDto)
                .toList();
    }

    /**
     * Метод для создания новой игры.
     * @param game объект игры.
     * @return созданная игра со ссылками.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public EntityModel<GameDto> createGame(@RequestBody Game game)
    {
        Game saved = gameRepository.save(game);
        GameDto dto = GameMapper.toDto(saved);

        return EntityModel.of(dto, linkTo(methodOn(GameResource.class).getGameById(saved.getId())).withSelfRel());
    }

    /**
     * Метод для обновления информации об игре.
     * @param id ID игры.
     * @param updated новые данные
     * @return обновлённая игра со ссылками.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public EntityModel<GameDto> updateGame(@PathVariable Long id, @RequestBody Game updated)
    {
        Game existing = gameRepository.findById(id).orElseThrow(() -> new RuntimeException("Game not found"));

        existing.setGameTitle(updated.getGameTitle());
        existing.setGameGenre(updated.getGameGenre());
        existing.setGameDescription(updated.getGameDescription());

        Game saved = gameRepository.save(existing);
        GameDto dto = GameMapper.toDto(saved);

        return EntityModel.of(dto, linkTo(methodOn(GameResource.class).getGameById(saved.getId())).withSelfRel());
    }

    /**
     * Метод для удаления игры со специфическим ID.
     * @param id ID игры.
     * @return код 204 No Content.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteGame(@PathVariable Long id)
    {
        gameRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
