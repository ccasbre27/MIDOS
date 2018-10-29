/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mingosoft.midos;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author ccastro31
 */
public class BaseItem implements Serializable
{
    protected String name;
    protected ItemType type;

    public BaseItem() {
        this.name = "";
        this.type = ItemType.DEFAULT;
    }
    
    public BaseItem(String name) {
        this.name = name;
    }

    public BaseItem(String name, ItemType type) {
        this.name = name;
        this.type = type;
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
        
        final BaseItem receivedItem = (BaseItem) obj;
        
        return Objects.equals(this.name.toUpperCase(), receivedItem.name.toUpperCase()) && Objects.equals(this.type, receivedItem.type);
    }

}

