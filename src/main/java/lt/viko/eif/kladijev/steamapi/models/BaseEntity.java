package lt.viko.eif.kladijev.steamapi.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * Модель, что создаёт абстрактное поле ID для каждой другой модели, что наследует её.
 */
@MappedSuperclass
public class BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
