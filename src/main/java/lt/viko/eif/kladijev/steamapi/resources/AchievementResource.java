package lt.viko.eif.kladijev.steamapi.resources;

import lt.viko.eif.kladijev.steamapi.dto.AchievementDto;
import lt.viko.eif.kladijev.steamapi.mappers.AchievementMapper;
import lt.viko.eif.kladijev.steamapi.models.Achievement;
import lt.viko.eif.kladijev.steamapi.repositories.AchievementRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Класс контроллер для управления достижениями.
 * Присутствуют CRUD-методы и HATEOAS ссылки.
 */
@RestController
@RequestMapping("api/achievements")
public class AchievementResource
{
    private final AchievementRepository achievementRepository;

    public AchievementResource(AchievementRepository achievementRepository)
    {
        this.achievementRepository = achievementRepository;
    }

    /**
     * Метод для получения списка всех достижений.
     * @return коллекция достижений с HATEOAS.
     */
    @GetMapping
    public CollectionModel<EntityModel<AchievementDto>> getAllAchievements()
    {
        var achievements = achievementRepository.findAll().stream()
                .map(ach -> {
                    AchievementDto dto = AchievementMapper.toDto(ach);

                    var model = EntityModel.of(dto);
                    model.add(linkTo(methodOn(AchievementResource.class).getAchievementById(ach.getId())).withSelfRel());
                    model.add(linkTo(methodOn(AchievementResource.class).deleteAchievement(ach.getId())).withRel("delete"));

                    return model;
                }).toList();

        return CollectionModel.of(achievements, linkTo(methodOn(AchievementResource.class).getAllAchievements()).withSelfRel());
    }

    /**
     * Метод для получения достижение со специфическим ID.
     * @param id ID достижения.
     * @return DTO достижение с HATEOAS ссылками.
     */
    @GetMapping("/{id}")
    public EntityModel<AchievementDto> getAchievementById(@PathVariable Long id)
    {
        Achievement ach = achievementRepository.findById(id).orElseThrow(() -> new RuntimeException("Achievement not found"));
        AchievementDto dto = AchievementMapper.toDto(ach);

        var model = EntityModel.of(dto);
        model.add(linkTo(methodOn(AchievementResource.class).getAchievementById(id)).withSelfRel());
        model.add(linkTo(methodOn(AchievementResource.class).deleteAchievement(id)).withRel("delete"));

        return model;
    }

    /**
     * Метод для создания достижения.
     * @param achievement объект достижения.
     * @return созданное достижение.
     */
    @PostMapping
    public EntityModel<AchievementDto> createAchievement(@RequestBody Achievement achievement)
    {
        Achievement saved = achievementRepository.save(achievement);
        AchievementDto dto = AchievementMapper.toDto(saved);

        return EntityModel.of(dto, linkTo(methodOn(AchievementResource.class).getAchievementById(saved.getId())).withSelfRel());
    }

    /**
     * Метод для обновления информации о достижении со специфическим ID.
     * @param id ID достижения.
     * @param updated новые данные.
     * @return обновлённое достижение.
     */
    @PutMapping("/{id}")
    public EntityModel<AchievementDto> updateAchievement(@PathVariable Long id, @RequestBody Achievement updated)
    {
        Achievement ach = achievementRepository.findById(id).orElseThrow(() -> new RuntimeException("Achievement not found"));

        ach.setAchievementName(updated.getAchievementName());
        ach.setAchievementDescription(updated.getAchievementDescription());
        ach.setDateAchieved(updated.getDateAchieved());

        Achievement saved = achievementRepository.save(ach);
        AchievementDto dto = AchievementMapper.toDto(saved);

        return EntityModel.of(dto, linkTo(methodOn(AchievementResource.class).getAchievementById(saved.getId())).withSelfRel());
    }

    /**
     * Метод для удаления достижения со специфическим ID.
     * @param id ID достижения.
     * @return код 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAchievement(@PathVariable Long id)
    {
        achievementRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
