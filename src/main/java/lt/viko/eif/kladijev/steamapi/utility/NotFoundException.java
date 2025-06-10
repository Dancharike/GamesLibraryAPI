package lt.viko.eif.kladijev.steamapi.utility;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение для ситуации, когда сущность не найдена.
 * Это универсальное исключение используемое для Player, Game, Achievement, Item.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException
{
    public NotFoundException(String entity, Long id)
    {
        super(String.format("%s with ID %d not found", entity, id));
    }

    public NotFoundException(String entity, String name)
    {
        super(String.format("%s with name %s not found", entity, name));
    }
}
