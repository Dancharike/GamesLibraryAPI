package lt.viko.eif.kladijev.steamapi.service;

import lt.viko.eif.kladijev.steamapi.models.Player;
import lt.viko.eif.kladijev.steamapi.repositories.PlayerRepository;
import lt.viko.eif.kladijev.steamapi.utility.NotFoundException;
import org.springframework.stereotype.Service;

/**
 * Класс сервис предназначенный для общих вспомогательных или часто используемых методов разных контроллеров.
 */
@Service
public class CommonMethodsService
{
    private final PlayerRepository playerRepository;

    public CommonMethodsService(PlayerRepository playerRepository)
    {
        this.playerRepository = playerRepository;
    }

    public Player findPlayerByNickname(String nickname)
    {
        return playerRepository.findAll().stream()
                .filter(p -> p.getNickName().equalsIgnoreCase(nickname))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Player", 0L));
    }

    public Player findPlayerById(Long id)
    {
        return playerRepository.findById(id).orElseThrow(() -> new NotFoundException("Player", id));
    }
}
