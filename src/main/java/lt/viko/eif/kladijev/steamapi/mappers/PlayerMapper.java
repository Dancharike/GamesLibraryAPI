package lt.viko.eif.kladijev.steamapi.mappers;

import lt.viko.eif.kladijev.steamapi.dto.PlayerDto;
import lt.viko.eif.kladijev.steamapi.models.Player;

/**
 * Класс для преобразования модели Player в DTO.
 * Здесь это используется для изоляции внутренней модели от формата API.
 */
public class PlayerMapper
{
    public static PlayerDto toDto(Player player)
    {
        PlayerDto dto = new PlayerDto();
        dto.id = player.getId();
        dto.nickName = player.getNickName();
        dto.email = player.getEmail();
        dto.level = player.getLevel();
        dto.experience = player.getExperience();
        dto.totalInventoryItems = player.getItems().size();
        dto.totalAchievements = player.getAchievements().size();

        return dto;
    }
}
