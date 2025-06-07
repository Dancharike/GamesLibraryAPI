package lt.viko.eif.kladijev.steamapi.mappers;

import lt.viko.eif.kladijev.steamapi.dto.AchievementDto;
import lt.viko.eif.kladijev.steamapi.models.Achievement;

/**
 * Класс для преобразования модели Achievement в DTO.
 * Здесь это используется для изоляции внутренней модели от формата API.
 */
public class AchievementMapper
{
    public static AchievementDto toDto(Achievement achievement)
    {
        AchievementDto dto = new AchievementDto();
        dto.id = achievement.getId();
        dto.achievementName = achievement.getAchievementName();
        dto.achievementDescription = achievement.getAchievementDescription();
        dto.dateAchieved = achievement.getDateAchieved().toLocalDate();

        return dto;
    }
}
