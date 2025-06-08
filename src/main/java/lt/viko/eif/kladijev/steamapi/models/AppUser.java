package lt.viko.eif.kladijev.steamapi.models;

import jakarta.persistence.*;

/**
 * Модель для определения типа пользователя использующего приложение.
 */
@Entity
public class AppUser extends BaseEntity
{
    @Column(unique = true)
    private String username;
    private String password;
    private String role;

    @OneToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToOne
    @JoinColumn(name = "player_id")
    private Player player;

    public AppUser(String username, String password, String role)
    {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public AppUser() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", admin=" + admin +
                ", player=" + player +
                ", id=" + id +
                '}';
    }
}
