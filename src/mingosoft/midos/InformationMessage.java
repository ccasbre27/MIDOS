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
public class InformationMessage 
{
    
    private INFORMATION_CODE code;
    private String number;
    private String description;

    public InformationMessage(INFORMATION_CODE code, String number, String description) {
        this.code = code;
        this.number = number;
        this.description = description;
    }
    

    public INFORMATION_CODE getCode() {
        return code;
    }

    public void setCode(INFORMATION_CODE code) {
        this.code = code;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    
}
