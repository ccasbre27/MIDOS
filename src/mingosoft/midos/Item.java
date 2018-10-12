/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mingosoft.midos;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author ccastro31
 */
// se implementa serializable para indicarle que 
public class Item implements Serializable
{
    private String name;
    private ItemType type;
    ArrayList<Item> items;

    public Item() {
        this.name = "";
        this.type = ItemType.DEFAULT;
        this.items = new ArrayList<>();
    }

    public Item(String name, ItemType type) {
        this.name = name;
        this.type = type;
        this.items = new ArrayList<>();
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


    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    
}
