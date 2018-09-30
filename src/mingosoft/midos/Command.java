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
public class Command 
{
    private COMMAND_TYPE commandType;
    private String patternAccepted;
    private String description;

    public Command(COMMAND_TYPE commandType, String patternAccepted, String description) {
        this.commandType = commandType;
        this.patternAccepted = patternAccepted;
        this.description = description;
    }

    public COMMAND_TYPE getCommandType() {
        return commandType;
    }

    public void setCommandType(COMMAND_TYPE commandType) {
        this.commandType = commandType;
    }

    public String getPatternAccepted() {
        return patternAccepted;
    }

    public void setPatternAccepted(String patternAccepted) {
        this.patternAccepted = patternAccepted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
}
