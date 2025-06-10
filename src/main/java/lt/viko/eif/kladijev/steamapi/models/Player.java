package lt.viko.eif.kladijev.steamapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс модель для игрока, содержит: id, nickname, email, level, experience поля.
 * Дополнительно прикреплены листы на другие модели.
 * Также класс имеет методы для подсчитывания общего кол-ва достижений и предметов игрока.
 */
@Entity
public class Player extends BaseEntity
{
    @Column private String nickName;
    @Column private String email;
    @Column private Integer level;
    @Column private Integer experience;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private List<Game> games = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private List<Achievement> achievements = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private List<Item> items = new ArrayList<>();

    /**
     * Метод для вычисления общего кол-ва достижений для игрока.
     * @return Общее кол-во достижений.
     */
    public int getTotalAchievements()
    {
        if(achievements == null)
        {
            return 0;
        }
        return achievements.size();
    }

    /**
     * Метод для вычисления общего кол-ва предметов для игрока.
     * @return Общее кол-во предметов.
     */
    public int getTotalInventoryItems()
    {
        if(items == null)
        {
            return 0;
        }
        return items.size();
    }

    public Player(Long id, String nickName, String email, Integer level, Integer experience, List<Game> games, List<Achievement> achievements, List<Item> items)
    {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.level = level;
        this.experience = experience;
        this.games = games;
        this.achievements = achievements;
        this.items = items;
    }

    public Player(String nickName, String email, Integer level, Integer experience, List<Game> games, List<Achievement> achievements, List<Item> items)
    {
        this.nickName = nickName;
        this.email = email;
        this.level = level;
        this.experience = experience;
        this.games = games;
        this.achievements = achievements;
    }

    public Player(String nickName, String email, Integer level, Integer experience)
    {
        this.nickName = nickName;
        this.email = email;
        this.level = level;
        this.experience = experience;
    }

    public Player() {}

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Player{" +
                "nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", level=" + level +
                ", experience=" + experience +
                ", games=" + games +
                ", achievements=" + achievements +
                ", items=" + items +
                ", id=" + id +
                '}';
    }
}