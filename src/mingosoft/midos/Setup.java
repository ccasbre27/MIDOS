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
    static int freeStorage = 0;
    
    // almacena los mensajes de error
    static ArrayList <ErrorMessage> errorMessagesList = new ArrayList<ErrorMessage>();
    
    // almacena los comandos permitidos
    static ArrayList <Command> commandsList = new ArrayList<Command>();
    
    static boolean exit = false;
    
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
        do 
        {
            optionEntered = "";
            
            DisplayVersion();
       
            optionEntered = scanner.next();
        
        
            // se verifica si el texto ingresado corresponde a un comando
            informationCode = CheckCommand(optionEntered);
        
        } 
        while (exit);
        
    
        
        
        
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
        commandsList.add(new Command(COMMAND_TYPE.DATE, "^DATE|date$", "Despliega la fecha del sistema"));
        commandsList.add(new Command(COMMAND_TYPE.TIME, "^TIME|time$", "Despliega la hora del sistema"));
        commandsList.add(new Command(COMMAND_TYPE.VER, "^VER|ver$", "Despliega la versión y espacio libre del sistema"));
        commandsList.add(new Command(COMMAND_TYPE.EXIT, "^EXIT|exit$", "Finaliza el programa"));
        
        
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
                        
                    case TIME:
                        DisplayTime();
                        break;
                        
                    case MD:
                        break;
                
                    case VER:
                        DisplayVersion();
                        break;
                        
                    case EXIT:
                        Exit();
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
        Date currentTime = Calendar.getInstance().getTime();

        System.out.println(new SimpleDateFormat("dd/MM/yyyy").format(currentTime));
    }
    
    private static void DisplayTime()
    {
        Date currentTime = Calendar.getInstance().getTime();

        System.out.println(new SimpleDateFormat("HH:mm:ss").format(currentTime));
    }
    
    private static void DisplayVersion()
    {
        // mensaje que se va desplegar al usuario con la información de la memoria restante 
        final String versionMessage = "MINGOSOFT ® MIDOS \n" +
                            "© Copyright MINGOSOFT CORPORATION 2018\n" +
                            "Versión 1.0 Memoria libre: %d K Autor: Carlos - Cédula\n" +
                            "M:\\ _";
    
         System.out.print(String.format(versionMessage, freeStorage));
    }
    
    private static void Exit()
    {
        Scanner scanner = new Scanner(System.in);
        boolean stopAskingCorrectOption = false;
        
        do 
        {
             // se verifica si desea salir del sistema
            System.out.print("¿Está seguro que desea salir de MIDOS (S/N) ?");

            String selectedOption = scanner.next();

            if (selectedOption.equalsIgnoreCase("S")) 
            {
                System.out.println("El programa va finalizar\n¡Te esperamos de vuelta pronto!");
                exit = false;
                stopAskingCorrectOption = false;
            }
            else
            {
                if (!selectedOption.equalsIgnoreCase("N")) 
                {
                    System.out.println("Verifique la opción ingresada, sólo se permite (S para si/N para no)");
                    stopAskingCorrectOption = true;
                } 
            }
        } 
        while (stopAskingCorrectOption);

    }
}
