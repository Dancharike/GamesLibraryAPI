package lt.viko.eif.kladijev.steamapi.mappers;

import lt.viko.eif.kladijev.steamapi.dto.ItemDto;
import lt.viko.eif.kladijev.steamapi.models.Item;

/**
 * Класс для преобразования модели Item в DTO.
 * Здесь это используется для изоляции внутренней модели от формата API.
 */
public class ItemMapper
{
    public static ItemDto toDto(Item item)
    {
        ItemDto dto = new ItemDto();
        dto.id = item.getId();
        dto.itemName = item.getItemName();
        dto.itemDescription = item.getItemDescription();
        dto.cost = item.getCost();

        return dto;
    }
}
