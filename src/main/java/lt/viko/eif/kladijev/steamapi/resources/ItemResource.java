package lt.viko.eif.kladijev.steamapi.resources;

import lt.viko.eif.kladijev.steamapi.dto.ItemDto;
import lt.viko.eif.kladijev.steamapi.mappers.ItemMapper;
import lt.viko.eif.kladijev.steamapi.models.Item;
import lt.viko.eif.kladijev.steamapi.repositories.ItemRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Класс контроллер для управления предметами.
 * Присутствуют CRUD-методы и HATEOAS ссылки.
 */
@RestController
@RequestMapping("api/items")
public class ItemResource
{
    private final ItemRepository itemRepository;

    public ItemResource(ItemRepository itemRepository)
    {
        this.itemRepository = itemRepository;
    }

    /**
     * Метод для получения всех предметов.
     * @return коллекция предметов с HATEOAS.
     */
    @GetMapping
    public CollectionModel<EntityModel<ItemDto>> getAllItems()
    {
        var items = itemRepository.findAll().stream()
                .map(item -> {
                    ItemDto dto = ItemMapper.toDto(item);

                    var model = EntityModel.of(dto);
                    model.add(linkTo(methodOn(ItemResource.class).getItemById(item.getId())).withSelfRel());
                    model.add(linkTo(methodOn(ItemResource.class).deleteItem(item.getId())).withRel("delete"));

                    return model;
                }).toList();

        return CollectionModel.of(items, linkTo(methodOn(ItemResource.class).getAllItems()).withSelfRel());
    }

    /**
     * Метод для получения предмета со специфическим ID.
     * @param id ID предмета.
     * @return DTO предмета с HATEOAS ссылками.
     */
    @GetMapping("/{id}")
    public EntityModel<ItemDto> getItemById(@PathVariable Long id)
    {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        ItemDto dto = ItemMapper.toDto(item);

        var model = EntityModel.of(dto);
        model.add(linkTo(methodOn(ItemResource.class).getItemById(id)).withSelfRel());
        model.add(linkTo(methodOn(ItemResource.class).deleteItem(id)).withRel("delete"));

        return model;
    }

    /**
     * Метод для создания предмета.
     * @param item объект предмета.
     * @return созданный предмет.
     */
    @PostMapping
    public EntityModel<ItemDto> createItem(@RequestBody Item item)
    {
        Item saved = itemRepository.save(item);
        ItemDto dto = ItemMapper.toDto(saved);

        return EntityModel.of(dto, linkTo(methodOn(ItemResource.class).getItemById(saved.getId())).withSelfRel());
    }

    /**
     * Метод для обновления данных о предмете со специфическим ID.
     * @param id ID предмета.
     * @param updated новые данные.
     * @return обновлённый предмет.
     */
    @PutMapping("/{id}")
    public EntityModel<ItemDto> updateItem(@PathVariable Long id, @RequestBody Item updated)
    {
        Item existing = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));

        existing.setItemName(updated.getItemName());
        existing.setItemDescription(updated.getItemDescription());
        existing.setCost(updated.getCost());

        Item saved = itemRepository.save(existing);
        ItemDto dto = ItemMapper.toDto(saved);

        return EntityModel.of(dto, linkTo(methodOn(ItemResource.class).getItemById(saved.getId())).withSelfRel());
    }

    /**
     * Метод для удаления предмета со специфическим ID.
     * @param id ID предмета.
     * @return код 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id)
    {
        itemRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
