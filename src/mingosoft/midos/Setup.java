/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mingosoft.midos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import javax.print.attribute.standard.DateTimeAtCompleted;

/**
 *
 * @author ccastro31
 */
public class Setup 
{
    // mensaje que se va desplegar al usuario con la información de la memoria restante 
    final static String versionMessage = "MINGOSOFT ® MIDOS \n" +
                            "© Copyright MINGOSOFT CORPORATION 2018\n" +
                            "Versión 1.0 Memoria libre: {0} K Autor: Carlos - Cédula\n" +
                            "M:\\ _";
    
    // almacena los mensajes de error
    static ArrayList <ErrorMessage> errorMessagesList = new ArrayList<ErrorMessage>();
    
    // almacena los comandos permitidos
    static ArrayList <Command> commandsList = new ArrayList<Command>();
    
    public static void main (String [] args) throws IOException
    {
        String optionEntered = "";
        INFORMATION_CODE informationCode = INFORMATION_CODE.DEFAULT;
        Scanner scanner = new Scanner(System.in);
        
        // se cargan los mensajes de error
        LoadErrorMessages();
        
        // se cargan los comandos
        LoadCommands();
        
        // se debe repetir hasta que el usuario desee salir mediante el comando EXIT
        
        System.out.print(versionMessage);
        optionEntered = scanner.next();
        
        
        // se verifica si el texto ingresado corresponde a un comando
        informationCode = CheckCommand(optionEntered);
        
        
        
    }
    
    private static void LoadErrorMessages()
    {
        errorMessagesList.add(new ErrorMessage(INFORMATION_CODE.COMMAND_NOT_FOUND, "Comando inválido"));
        errorMessagesList.add(new ErrorMessage(INFORMATION_CODE.NOT_VALID_OPTION, "Opción inválida"));
        errorMessagesList.add(new ErrorMessage(INFORMATION_CODE.DUPLICATE_DIRECTORY, "Ya existe un directorio con ese nombre: nombre del directorio"));
    }
    
    private static void LoadCommands()
    {
        commandsList.add(new Command(COMMAND_TYPE.CLS, "^CLS|cls$", "Permite limpiar la pantalla de la consola"));
        commandsList.add(new Command(COMMAND_TYPE.DATE, "^DATE|date", "Despliega la fecha del sistema"));
        
    }
    
    private static INFORMATION_CODE CheckCommand(String commandToSearch)
    {
        
        for (Command command : commandsList)
        {
            if (commandToSearch.matches(command.getPatternAccepted())) 
            {
                // comando encontrado
                switch(command.getCommandType())
                {
                    case CLS:
                        // se simula un limpiado de pantalla con 10 líneas en blanco
                        ClearScreen();
                        break;
                        
                    case DATE:
                        DisplayDate();
                        break;
                        
                    case MD:
                        break;
                        
                    case TIME:
                        break;
                        
                    case VER:
                        break;
                        
                    case EXIT:
                        break;
                }
            }
        }
        
        // comando inválido
        return INFORMATION_CODE.NOT_VALID_OPTION;
    }
    
    private static void ClearScreen()
    {
        for (int i = 0; i < 10; i++) {
            System.out.println();
        }
        
        System.out.print("\\M:");
    }
    
    
    private static void DisplayDate()
    {
        Date currentDate = Calendar.getInstance().getTime();

        System.out.println(new SimpleDateFormat("dd/MM/yyyy").format(currentDate));
    }
}
