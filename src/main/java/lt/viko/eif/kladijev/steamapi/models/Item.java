package lt.viko.eif.kladijev.steamapi.models;

import jakarta.persistence.*;

/**
 * Класс модели предмета, содержит: id, itemName, itemDescription, cost.
 */
@Entity
public class Item extends BaseEntity
{
    @Column private String itemName;
    @Column private String itemDescription;
    @Column private Float cost;

    public Item(Long id, String itemName, String itemDescription, Float cost)
    {
        this.id = id;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.cost = cost;
    }

    public Item(String itemName, String itemDescription, Float cost)
    {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.cost = cost;
    }

    public Item() {}

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemName='" + itemName + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", cost=" + cost +
                ", id=" + id +
                '}';
    }
}
