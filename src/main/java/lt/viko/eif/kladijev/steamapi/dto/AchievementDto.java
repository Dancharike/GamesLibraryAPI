package lt.viko.eif.kladijev.steamapi.dto;

import java.time.LocalDate;

/**
 * DTO класс, что поможет с грамотным выводом данных Achievement в API.
 */
public class AchievementDto
{
    public Long id;
    public String achievementName;
    public String achievementDescription;
    public LocalDate dateAchieved;
}
