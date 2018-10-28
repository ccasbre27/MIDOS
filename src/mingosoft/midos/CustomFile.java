/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mingosoft.midos;

import java.io.Serializable;

/**
 *
 * @author ccastro31
 */
public class CustomFile extends BaseItem implements Serializable {
    
    private String content;

    public CustomFile() {
        this.content = "";
    }

    public CustomFile(String name, ItemType type) {
        this.content = "";
        super.name = name;
        super.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    
    
    @Override
    public boolean equals(Object obj) {
        
        return super.equals(obj);
    }
    
}
