package lt.viko.eif.kladijev.steamapi.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Класс модель достижения, содержит: id, achievementName, achievementDescription, dateAchieved.
 */
@Entity
public class Achievement extends BaseEntity
{
    @Column private String achievementName;
    @Column private String achievementDescription;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column private LocalDateTime dateAchieved;

    public Achievement(Long id, String achievementName, String achievementDescription, LocalDateTime dateAchieved)
    {
        this.id = id;
        this.achievementName = achievementName;
        this.achievementDescription = achievementDescription;
        this.dateAchieved = dateAchieved;
    }

    public Achievement(String achievementName, String achievementDescription, LocalDateTime dateAchieved)
    {
        this.achievementName = achievementName;
        this.achievementDescription = achievementDescription;
        this.dateAchieved = dateAchieved;
    }

    public Achievement() {}

    public String getAchievementName() {
        return achievementName;
    }

    public void setAchievementName(String achievementName) {
        this.achievementName = achievementName;
    }

    public String getAchievementDescription() {
        return achievementDescription;
    }

    public void setAchievementDescription(String achievementDescription) {
        this.achievementDescription = achievementDescription;
    }

    public LocalDateTime getDateAchieved() {
        return dateAchieved;
    }

    public void setDateAchieved(LocalDateTime dateAchieved) {
        this.dateAchieved = dateAchieved;
    }

    @Override
    public String toString() {
        return "Achievement{" +
                "achievementName='" + achievementName + '\'' +
                ", achievementDescription='" + achievementDescription + '\'' +
                ", dateAchieved=" + dateAchieved +
                ", id=" + id +
                '}';
    }
}
