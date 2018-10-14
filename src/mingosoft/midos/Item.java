/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mingosoft.midos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author ccastro31
 */
// se implementa serializable para indicarle que 
public class Item implements Serializable
{
    private String name;
    private ItemType type;
    public Item previousItem;
    public ArrayList<Item> nextItems;

    public Item() {
        this.name = "";
        this.type = ItemType.DEFAULT;
        this.previousItem = null;
        this.nextItems = new ArrayList<>();
    }

    public Item(String name, ItemType type) {
        this.name = name;
        this.type = type;
        this.previousItem = null;
        this.nextItems = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.name);
        return hash;
    }

    
    @Override
    public boolean equals(Object obj) {
        
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final Item receivedItem = (Item) obj;
        
        return Objects.equals(this.name, receivedItem.name);
    }

    

    
    
}
