package lt.viko.eif.kladijev.steamapi.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

/**
 * Класс модель для админа.
 */
@Entity
public class Admin extends BaseEntity
{
    @Column private String nickName;
    @Column private String email;
    @Column private Integer level;
    @Column private Integer experience;

    public Admin(String nickName, String email, Integer level, Integer experience)
    {
        this.nickName = nickName;
        this.email = email;
        this.level = level;
        this.experience = experience;
    }

    public Admin() {}

    public String getFullName() {
        return nickName;
    }

    public void setFullName(String fullName) {
        this.nickName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", level=" + level +
                ", experience=" + experience +
                ", id=" + id +
                '}';
    }
}
