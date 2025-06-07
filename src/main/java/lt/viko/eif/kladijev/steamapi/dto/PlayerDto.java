package lt.viko.eif.kladijev.steamapi.dto;

/**
 * DTO класс, что поможет с грамотным выводом данных Player в API.
 */
public class PlayerDto
{
    public Long id;
    public String nickName;
    public String email;
    public int level;
    public int experience;
    public int totalInventoryItems;
    public int totalAchievements;
}
