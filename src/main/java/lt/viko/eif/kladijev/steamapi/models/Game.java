package lt.viko.eif.kladijev.steamapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс модель игры, содержит: id, gameTitle, gameGenre, gameDescription.
 * Также прикреплены листы на другие классы.
 */
@Entity
public class Game extends BaseEntity
{
    @Column private String gameTitle;
    @Column private String gameGenre;
    @Column private String gameDescription;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private List<Achievement> achievements = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private List<Item> items = new ArrayList<>();

    public Game(Long id, String gameTitle, String gameGenre, String gameDescription, List<Achievement> achievements,List<Item> items)
    {
        this.id = id;
        this.gameTitle = gameTitle;
        this.gameGenre = gameGenre;
        this.gameDescription = gameDescription;
        this.achievements = achievements;
        this.items = items;
    }

    public Game(String gameTitle, String gameGenre, String gameDescription, List<Achievement> achievements,List<Item> items)
    {
        this.gameTitle = gameTitle;
        this.gameGenre = gameGenre;
        this.gameDescription = gameDescription;
        this.achievements = achievements;
    }

    public Game(String gameTitle, String gameGenre, String gameDescription)
    {
        this.gameTitle = gameTitle;
        this.gameGenre = gameGenre;
        this.gameDescription = gameDescription;
    }

    public Game() {}

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getGameGenre() {
        return gameGenre;
    }

    public void setGameGenre(String gameGenre) {
        this.gameGenre = gameGenre;
    }

    public String getGameDescription() {
        return gameDescription;
    }

    public void setGameDescription(String gameDescription) {
        this.gameDescription = gameDescription;
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
        return "Game{" +
                "gameTitle='" + gameTitle + '\'' +
                ", gameGenre='" + gameGenre + '\'' +
                ", gameDescription='" + gameDescription + '\'' +
                ", achievements=" + achievements +
                ", items=" + items +
                ", id=" + id +
                '}';
    }
}
