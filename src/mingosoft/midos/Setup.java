/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mingosoft.midos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author ccastro31
 */
public class Setup 
{
    
    static int TOTAL_STORAGE = 0;
    
 
    static final String DIRECTORIES_FILE = String.format(".%s%s%s%s%s%s%sMIDOSTRE.txt", File.separator,"src", File.separator, "mingosoft", File.separator, "midos", File.separator);
    static final String STORAGE_FILE = String.format(".%s%s%s%s%s%s%sMIDOSFRE.txt", File.separator,"src", File.separator, "mingosoft", File.separator, "midos", File.separator);
    
    
    // almacena los mensajes de error
    static ArrayList <ErrorMessage> errorMessagesList = new ArrayList<ErrorMessage>();
    
    // almacena los comandos permitidos
    static ArrayList <Command> commandsList = new ArrayList<Command>();
    static ArrayList <String> directoriesList = new ArrayList<String>();
    
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
       
        LoadStorage();
        LoadDirectories();

        // se debe repetir hasta que el usuario desee salir mediante el comando EXIT
        do 
        {
            optionEntered = "";

            DisplayVersion();

            optionEntered = scanner.nextLine();


            // se verifica si el texto ingresado corresponde a un comando
            CheckCommand(optionEntered);

        } 
        while (!exit);
        
    }
    
    private static void LoadErrorMessages()
    {
        errorMessagesList.add(new ErrorMessage(INFORMATION_CODE.COMMAND_NOT_FOUND, "Comando inválido"));
        errorMessagesList.add(new ErrorMessage(INFORMATION_CODE.NOT_VALID_OPTION, "Opción inválida"));
        errorMessagesList.add(new ErrorMessage(INFORMATION_CODE.DUPLICATE_DIRECTORY, "Ya existe un directorio con ese nombre: nombre del directorio"));
    }
    
    private static void LoadCommands()
    {
        commandsList.add(new Command(COMMAND_TYPE.CLS, "^CLS$|^cls$", "Permite limpiar la pantalla de la consola"));
        commandsList.add(new Command(COMMAND_TYPE.DATE, "^DATE$|^date$", "Despliega la fecha del sistema"));
        commandsList.add(new Command(COMMAND_TYPE.TIME, "^TIME$|^time$", "Despliega la hora del sistema"));
        commandsList.add(new Command(COMMAND_TYPE.MD, "MD [a-zA-Z]{1,}[a-zA-Z0-9]{0,7}|md [a-zA-Z]{1,}[a-zA-Z0-9]{0,7}", "Crea un directorio en la raíz donde se encuentra"));
        commandsList.add(new Command(COMMAND_TYPE.VER, "^VER$|^ver$", "Despliega la versión y espacio libre del sistema"));
        commandsList.add(new Command(COMMAND_TYPE.EXIT, "^EXIT$|^exit$", "Finaliza el programa"));
        
    }
    
    private static String GetErrorMessage(INFORMATION_CODE informationCode)
    {
        for (ErrorMessage errorMessage : errorMessagesList) 
        {
            if (errorMessage.getCode() == informationCode) 
            {
                return errorMessage.getDescription();
            }
        }
        
        return "";
    }
    
    private static void LoadStorage()
    {
        try 
        {
            
            // Se indica el archivo con el que se desea trabajar
            File file = new File (STORAGE_FILE);

            FileReader fileReader = new FileReader (file);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // permite almacenar los datos que se van leyendo
            String readedData;

            while((readedData = bufferedReader.readLine()) != null)
            {
                TOTAL_STORAGE = Integer.parseInt(readedData);
            }
                    
            fileReader.close();
 
        } 
        catch (IOException e) 
        {
            System.out.println("Ha ocurrido un error al cargar el almacenamiento disponible");
        }
        catch (NumberFormatException e)
        {
            System.out.println("El archivo de almacenamiento de memoria contiene información en un formato no reconocido");
        }
        
    }
    
    private static void WriteStorage()
    {
        try 
        {
            // se indica el archivo con el que se va trabajar y que trunque los datos para que siempre escriba la cantidad de espacio disponible
            // al inicio del archivo
            FileWriter fileWriter = new FileWriter(STORAGE_FILE, false);

            PrintWriter	printWriter = new PrintWriter(fileWriter);

            // se escribe la cantidad restante de espacio
            printWriter.print(TOTAL_STORAGE);
            
            fileWriter.close();
            
        } 
        catch (IOException e) 
        {
            System.out.println("Ha ocurrido un error al salvar el almacenamiento disponible");
        }
    }
    
    private static void LoadDirectories()
    {
        try 
        {
            
            // Se indica el archivo con el que se desea trabajar
            File file = new File (DIRECTORIES_FILE);

            FileReader fileReader = new FileReader (file);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // permite almacenar los datos que se van leyendo
            String readedData;

            while((readedData = bufferedReader.readLine()) != null)
            {
                directoriesList.add(readedData.trim());
            }
                    
            fileReader.close();
 
        } 
        catch (IOException e) 
        {
            System.out.println("Ha ocurrido un error al cargar el almacenamiento disponible");
        }
        catch (NumberFormatException e)
        {
            System.out.println("El archivo de almacenamiento de memoria contiene información en un formato no reconocido");
        }
    }
    
    private static void WriteDirectory(String name)
    {
        try 
        {
            // se indica el archivo con el que se va trabajar y que trunque los datos para que siempre escriba la cantidad de espacio disponible
            // al inicio del archivo
            FileWriter fileWriter = new FileWriter(DIRECTORIES_FILE, true);

            PrintWriter	printWriter = new PrintWriter(fileWriter);

            // se escribe el directorio en el archivo
            printWriter.println(name);
            
            fileWriter.close();
            
        } 
        catch (IOException e) 
        {
            System.out.print("Ha ocurrido un error al salvar el almacenamiento disponible");
        }
    }
    
    private static void ListDirectories()
    {
        for (String currentFolder : directoriesList) 
        {
            System.out.println(currentFolder);
        }
    }
    
    private static boolean SearchDirectory(String name)
    {
        for (String currentFolder : directoriesList) 
        {
            // se verifica si el nombre hace match
            if (name.equalsIgnoreCase(currentFolder)) 
            {
                return true;
            }
        }
        
        return false;
    }
    
    private static void CheckCommand(String commandToSearch)
    {
        boolean isValidCommand = false;
        
        for (Command command : commandsList)
        {
            if (commandToSearch.matches(command.getPatternAccepted())) 
            {
                isValidCommand = true;
                
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
                        MakeDirectory(commandToSearch.split("\\s+")[1]);
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
        
        if (!isValidCommand) 
        {
            System.out.println(GetErrorMessage(INFORMATION_CODE.COMMAND_NOT_FOUND));
        }
       
    }
    
    private static void ClearScreen()
    {
        for (int i = 0; i < 10; i++) 
        {
            System.out.println();
        }
        
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
    
    private static void MakeDirectory(String name)
    {
        // se verifica si hay espacio disponible para crear el directorio, cada uno ocupa 8k
        if (TOTAL_STORAGE >= 8) 
        {
            
            if(SearchDirectory(name))
            {
                System.out.println("Ya existe un directorio con ese nombre");
            }
            else
            {
                
                TOTAL_STORAGE -= 8;
                WriteDirectory(name);
                directoriesList.add(name.trim());
                WriteStorage();
                System.out.println("El directorio se ha creado exitosamente");
            }    
        }
        else    
        {
            System.out.println("No hay espacio disponible para crear el directorio");
        }
    }
    
    private static void DisplayVersion()
    {
        // mensaje que se va desplegar al usuario con la información de la memoria restante 
        final String versionMessage = "\nMINGOSOFT ® MIDOS \n" +
                            "© Copyright MINGOSOFT CORPORATION 2018\n" +
                            "Versión 1.0 Memoria libre: %d K Autor: Carlos Castro Brenes - Cédula: 1-1596-0319\n" +
                            "M:\\ _";
    
         System.out.print(String.format(versionMessage, TOTAL_STORAGE));
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
