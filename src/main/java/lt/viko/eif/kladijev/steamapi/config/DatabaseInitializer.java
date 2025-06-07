package lt.viko.eif.kladijev.steamapi.config;

import lt.viko.eif.kladijev.steamapi.models.AppUser;
import lt.viko.eif.kladijev.steamapi.models.Player;
import lt.viko.eif.kladijev.steamapi.repositories.PlayerRepository;
import lt.viko.eif.kladijev.steamapi.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Класс для заполнения базы данных начальными пользователями и игроками.
 */
@Configuration
public class DatabaseInitializer
{
    @Bean
    public CommandLineRunner initialiseUsers(
            UserRepository userRepository,
            PlayerRepository playerRepository,
            BCryptPasswordEncoder encoder)
    {
        return args -> {
            if (userRepository.findAll().isEmpty())
            {
                // Создание игроков
                Player playerOne = new Player("PlayerOne", "player1@example.com", 1, 100);
                Player playerTwo = new Player("PlayerTwo", "player2@example.com", 2, 250);

                playerRepository.save(playerOne);
                playerRepository.save(playerTwo);

                // Привязка к пользователям
                AppUser admin = new AppUser("admin", encoder.encode("password"), "ROLE_ADMIN");
                AppUser userOne = new AppUser("playerOne", encoder.encode("password"), "ROLE_PLAYER");
                AppUser userTwo = new AppUser("playerTwo", encoder.encode("password"), "ROLE_PLAYER");

                userOne.setPlayer(playerOne);
                userTwo.setPlayer(playerTwo);

                userRepository.save(admin);
                userRepository.save(userOne);
                userRepository.save(userTwo);

                System.out.println("Initial users and players created.");
            }
            else
            {
                System.out.println("Users already exist.");
            }
        };
    }
}
