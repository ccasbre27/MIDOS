/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mingosoft.midos;

/**
 *
 * @author ccastro31
 */
public class ErrorMessage 
{
    
    private INFORMATION_CODE code;
    private String description;

    public ErrorMessage(INFORMATION_CODE code, String description) {
        this.code = code;
        this.description = description;
    }
    

    public INFORMATION_CODE getCode() {
        return code;
    }

    public void setCode(INFORMATION_CODE code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    
}
