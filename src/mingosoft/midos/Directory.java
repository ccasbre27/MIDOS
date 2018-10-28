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
public class Directory extends BaseItem implements Serializable
{
    public Directory previousItem;
    public ArrayList<Object> nextItems;
    
    public Directory() {
        this.previousItem = null;
        this.nextItems = new ArrayList<>();
    }

    public Directory(String name, ItemType type) {
        super.name = name;
        super.type = type;
        this.previousItem = null;
        this.nextItems = new ArrayList<>();
    }
    
    @Override
    public boolean equals(Object obj) {
        
        return super.equals(obj);
    }
    
}
