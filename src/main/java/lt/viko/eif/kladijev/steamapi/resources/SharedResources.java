package lt.viko.eif.kladijev.steamapi.resources;

import lt.viko.eif.kladijev.steamapi.models.Achievement;
import lt.viko.eif.kladijev.steamapi.models.Game;
import lt.viko.eif.kladijev.steamapi.models.Item;
import lt.viko.eif.kladijev.steamapi.models.Player;
import lt.viko.eif.kladijev.steamapi.repositories.AdminRepository;
import lt.viko.eif.kladijev.steamapi.repositories.PlayerRepository;
import lt.viko.eif.kladijev.steamapi.service.CommonMethodsService;
import lt.viko.eif.kladijev.steamapi.utility.NotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Класс контроллер содержащий в себе методы общие для админа и игрока.
 * Сделано ради меньшего дублирования кода методов, что есть для каждого другого контроллера.
 * Есть все CRUD-методы и доступ к связанным сущностям.
 */
@RestController
@RequestMapping("/api/shared")
public class SharedResources
{
    private final AdminRepository adminRepository;
    private final PlayerRepository playerRepository;
    private final CommonMethodsService commonMethodsService;

    public SharedResources(AdminRepository adminRepository,
                           PlayerRepository playerRepository,
                           CommonMethodsService commonMethodsService
    )
    {
        this.adminRepository = adminRepository;
        this.playerRepository = playerRepository;
        this.commonMethodsService = commonMethodsService;
    }

    /**
     * Метод для получения игр игрока по его специфическому имени.
     * @param name имя игрока.
     * @return список игр указанного игрока.
     */
    @GetMapping("/players/name/{name}/games")
    public CollectionModel<EntityModel<Game>> getGamesByPlayerName(@PathVariable String name)
    {
        Player player = commonMethodsService.findPlayerByNickname(name);

        if (player == null)
        {
            throw new NotFoundException("Player", name);
        }

        List<EntityModel<Game>> games = player.getGames().stream()
                .map(game -> EntityModel.of(game,
                        linkTo(methodOn(GameResource.class).getGameById(game.getId())).withSelfRel(),
                        linkTo(methodOn(GameResource.class).updateGame(game.getId(), null)).withRel("update"),
                        linkTo(methodOn(GameResource.class).deleteGame(game.getId())).withRel("delete")))
                .toList();

        //return player.getGames();
        return CollectionModel.of(games, linkTo(methodOn(SharedResources.class).getGamesByPlayerName(name)).withSelfRel());
    }

    /**
     * Метод для получения достижений игрока по его специфическому имени.
     * @param name имя игрока.
     * @return список достижений указанного игрока.
     */
    @GetMapping("/players/name/{name}/achievements")
    public CollectionModel<EntityModel<Achievement>> getAchievementByPlayerName(@PathVariable String name)
    {
        Player player = commonMethodsService.findPlayerByNickname(name);

        if (player == null)
        {
            throw new NotFoundException("Player", name);
        }

        List<EntityModel<Achievement>> achievements = player.getAchievements().stream()
                .map(a -> EntityModel.of(a,
                        linkTo(methodOn(AchievementResource.class).getAchievementById(a.getId())).withSelfRel(),
                        linkTo(methodOn(AchievementResource.class).updateAchievement(a.getId(), null)).withRel("update"),
                        linkTo(methodOn(AchievementResource.class).deleteAchievement(a.getId())).withRel("delete")))
                .toList();

        //return player.getAchievements();
        return CollectionModel.of(achievements, linkTo(methodOn(SharedResources.class).getAchievementByPlayerName(name)).withSelfRel());
    }

    /**
     * Метод для получения предметов игрока по его специфическому имени.
     * @param name имя игрока.
     * @return список предметов указанного игрока.
     */
    @GetMapping("/players/name/{name}/items")
    public CollectionModel<EntityModel<Item>> getItemsByPlayerName(@PathVariable String name)
    {
        Player player = commonMethodsService.findPlayerByNickname(name);

        if (player == null)
        {
            throw new NotFoundException("Player", name);
        }


        List<EntityModel<Item>> items = player.getItems().stream()
                .map(item -> EntityModel.of(item,
                        linkTo(methodOn(ItemResource.class).getItemById(item.getId())).withSelfRel(),
                        linkTo(methodOn(ItemResource.class).updateItem(item.getId(), null)).withRel("update"),
                        linkTo(methodOn(ItemResource.class).deleteItem(item.getId())).withRel("delete")))
                .toList();

        //return player.getItems();
        return CollectionModel.of(items, linkTo(methodOn(SharedResources.class).getItemsByPlayerName(name)).withSelfRel());
    }
}
