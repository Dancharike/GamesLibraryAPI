package lt.viko.eif.kladijev.steamapi.config;

import lt.viko.eif.kladijev.steamapi.models.*;
import lt.viko.eif.kladijev.steamapi.repositories.AdminRepository;
import lt.viko.eif.kladijev.steamapi.repositories.PlayerRepository;
import lt.viko.eif.kladijev.steamapi.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс для заполнения базы данных начальными пользователями и игроками.
 */
@Configuration
public class DatabaseInitializer
{
    @Bean
    public CommandLineRunner initialiseUsers(UserRepository userRepository, AdminRepository adminRepository, PlayerRepository playerRepository, BCryptPasswordEncoder encoder)
    {
        return args -> {
            if (userRepository.findAll().isEmpty())
            {
                // создание админа
                Admin adminOne = new Admin("AdminOne", "admin1@example.com", 100, 5000);
                Admin adminTwo = new Admin("AdminTwo", "admin2@example.com", 101, 6000);
                Admin adminThree = new Admin("AdminThree", "admin3@example.com", 102, 7000);

                adminRepository.saveAll(List.of(adminOne, adminTwo, adminThree));

                // создание игроков
                Player playerOne = new Player("PlayerOne", "player1@example.com", 1, 100);
                Player playerTwo = new Player("PlayerTwo", "player2@example.com", 2, 250);
                Player playerThree = new Player("PlayerThree", "player3@example.com", 3, 500);

                // создание игр
                Game game1 = new Game("Half-Life", "First-person shooter", "Cool Game");
                Game game2 = new Game("Stardew Valley", "Farming simulator", "Not a cool game");
                Game game3 = new Game("The Witcher 3", "RPG", "Also cool game");

                // создание достижений
                Achievement ach1 = new Achievement("Headshot Master", "Get 100 headshots", LocalDateTime.now().minusDays(10));
                Achievement ach2 = new Achievement("Crop King", "Harvest 100 crops", LocalDateTime.now().minusDays(5));
                Achievement ach3 = new Achievement("Monster Slayer", "Kill 50 monsters", LocalDateTime.now().minusDays(3));
                Achievement ach4 = new Achievement("Explorer", "Visit 10 locations", LocalDateTime.now().minusDays(7));

                // создание предметов
                Item item1 = new Item("Gravity Gun", "Experimental weapon", 199.99f);
                Item item2 = new Item("Golden Hoe", "Legendary farming tool", 49.99f);
                Item item3 = new Item("Silver Sword", "Witcher weapon", 149.99f);
                Item item4 = new Item("Teleportation Rune", "Instant travel", 89.99f);

                // привязка к играм
                game1.getAchievements().add(ach1);
                game1.getItems().add(item1);

                game2.getAchievements().add(ach2);
                game2.getItems().add(item2);

                game3.getAchievements().add(ach3);
                game3.getAchievements().add(ach4);
                game3.getItems().add(item3);
                game3.getItems().add(item4);

                // привязка к игрокам
                playerOne.getGames().add(game1);
                playerOne.getAchievements().add(ach1); // соответствует игре
                playerOne.getItems().add(item1);       // соответствует игре

                playerTwo.getGames().add(game2);
                playerTwo.getAchievements().add(ach2); // соответствует
                playerTwo.getItems().add(item2);       // соответствует

                playerThree.getGames().add(game3);
                playerThree.getAchievements().add(ach3); // соответствует
                playerThree.getAchievements().add(ach4); // тоже
                playerThree.getItems().add(item3);       // соответствует
                playerThree.getItems().add(item4);       // тоже

                // сохранение игроков
                playerRepository.saveAll(List.of(playerOne, playerTwo, playerThree));

                // создание пользователей
                AppUser user1 = new AppUser("admin1", encoder.encode("password"), "ROLE_ADMIN");
                AppUser user2 = new AppUser("admin2", encoder.encode("password"), "ROLE_ADMIN");
                AppUser user3 = new AppUser("admin3", encoder.encode("password"), "ROLE_ADMIN");

                AppUser user4 = new AppUser("playerOne", encoder.encode("password"), "ROLE_PLAYER");
                AppUser user5 = new AppUser("playerTwo", encoder.encode("password"), "ROLE_PLAYER");
                AppUser user6 = new AppUser("playerThree", encoder.encode("password"), "ROLE_PLAYER");

                user1.setAdmin(adminOne);
                user2.setAdmin(adminTwo);
                user3.setAdmin(adminThree);

                user4.setPlayer(playerOne);
                user5.setPlayer(playerTwo);
                user6.setPlayer(playerThree);

                userRepository.saveAll(List.of(user1, user2, user3, user4, user5, user6));

                System.out.println("Initial users and players created.");
            }
            else
            {
                System.out.println("Users already exist.");
            }
        };
    }
}
