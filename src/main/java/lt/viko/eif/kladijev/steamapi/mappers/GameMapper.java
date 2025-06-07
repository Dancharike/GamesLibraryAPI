package lt.viko.eif.kladijev.steamapi.mappers;

import lt.viko.eif.kladijev.steamapi.dto.GameDto;
import lt.viko.eif.kladijev.steamapi.models.Game;

/**
 * Класс для преобразования модели Game в DTO.
 * Здесь это используется для изоляции внутренней модели от формата API.
 */
public class GameMapper
{
    public static GameDto toDto(Game game)
    {
        GameDto dto = new GameDto();
        dto.id = game.getId();
        dto.gameTitle = game.getGameTitle();
        dto.gameGenre = game.getGameGenre();
        dto.gameDescription = game.getGameDescription();

        return dto;
    }
}
