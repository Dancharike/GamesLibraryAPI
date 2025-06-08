package lt.viko.eif.kladijev.steamapi.mappers;

import lt.viko.eif.kladijev.steamapi.dto.AdminDto;
import lt.viko.eif.kladijev.steamapi.models.Admin;

/**
 * Класс для преобразования модели Admin в DTO.
 * Здесь это используется для изоляции внутренней модели от формата API.
 */
public class AdminMapper
{
    public static AdminDto toDto(Admin admin)
    {
        AdminDto dto = new AdminDto();
        dto.id = admin.getId();
        dto.fullName = admin.getFullName();
        dto.email = admin.getEmail();
        dto.level = admin.getLevel();
        dto.experience = admin.getExperience();

        return dto;
    }
}
