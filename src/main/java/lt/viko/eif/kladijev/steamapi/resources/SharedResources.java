package lt.viko.eif.kladijev.steamapi.resources;

import lt.viko.eif.kladijev.steamapi.models.Achievement;
import lt.viko.eif.kladijev.steamapi.models.Game;
import lt.viko.eif.kladijev.steamapi.models.Item;
import lt.viko.eif.kladijev.steamapi.models.Player;
import lt.viko.eif.kladijev.steamapi.repositories.AdminRepository;
import lt.viko.eif.kladijev.steamapi.repositories.PlayerRepository;
import lt.viko.eif.kladijev.steamapi.service.CommonMethodsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
