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
    private String acceptedPattern;
    private String acceptedKeyWord;
    private String description;
    private String help;

    public Command(COMMAND_TYPE commandType, String patternAccepted, String acceptedKeyWord ,String description, String help) {
        this.commandType = commandType;
        this.acceptedPattern = patternAccepted;
        this.acceptedKeyWord = acceptedKeyWord;
        this.description = description;
        this.help = help;
    }

    public COMMAND_TYPE getCommandType() {
        return commandType;
    }

    public void setCommandType(COMMAND_TYPE commandType) {
        this.commandType = commandType;
    }

    public String getAcceptedPattern() {
        return acceptedPattern;
    }

    public void setAcceptedPattern(String patternAccepted) {
        this.acceptedPattern = patternAccepted;
    }

    public String getAcceptedKeyWord() {
        return acceptedKeyWord;
    }

    public void setAcceptedKeyWord(String acceptedKeyWord) {
        this.acceptedKeyWord = acceptedKeyWord;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }
    
}
