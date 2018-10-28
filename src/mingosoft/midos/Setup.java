/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mingosoft.midos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    static ArrayList <InformationMessage> informationMessagesList = new ArrayList<InformationMessage>();
    
    // almacena los comandos permitidos
    static ArrayList <Command> commandsList = new ArrayList<Command>();
    
    // indica el directorio actual
    static Directory currentItem = new Directory();
    
    static String path = "M:\\";
    
    static boolean exit = false;
    
    static COMMAND_LINE_TYPE commandLineType = COMMAND_LINE_TYPE.PROMPT;
    
    static Scanner scanner = null;
    
    public static void main (String [] args) throws IOException
    {
        String optionEntered = "";
        INFORMATION_CODE informationCode = INFORMATION_CODE.DEFAULT;
        scanner = new Scanner(System.in);
        
        // se cargan los mensajes de error
        LoadErrorMessages();
        
        // se cargan los comandos
        LoadCommands();
       
        LoadStorage();
        LoadItems();
        
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
        informationMessagesList.add(new InformationMessage(INFORMATION_CODE.COMMAND_NOT_FOUND, "001", "Comando inválido"));
        informationMessagesList.add(new InformationMessage(INFORMATION_CODE.NOT_VALID_OPTION, "002", "Opción inválida"));
        informationMessagesList.add(new InformationMessage(INFORMATION_CODE.DUPLICATE_DIRECTORY, "003", "Ya existe un directorio con ese nombre: nombre del directorio"));
        informationMessagesList.add(new InformationMessage(INFORMATION_CODE.EMPTY_DIRECTORY, "004", "La carpeta se encuentra vacía"));
    }
    
    private static void LoadCommands()
    {
        commandsList.add(new Command(COMMAND_TYPE.CLS, "^CLS$", "Permite limpiar la pantalla de la consola"));
        commandsList.add(new Command(COMMAND_TYPE.DATE, "^DATE$", "Despliega la fecha del sistema"));
        commandsList.add(new Command(COMMAND_TYPE.TIME, "^TIME$", "Despliega la hora del sistema"));
        commandsList.add(new Command(COMMAND_TYPE.MD, "MD [a-zA-Z]{1,}[a-zA-Z0-9]{0,7}", "Crea un directorio en la ruta actual"));
        commandsList.add(new Command(COMMAND_TYPE.CD, "CD [a-zA-Z]{1,}[a-zA-Z0-9]{0,7}|CD [.]{2,2}|CD [\\\\]{1,1}", "Cambia al directorio especificado"));
        commandsList.add(new Command(COMMAND_TYPE.VER, "^VER$", "Despliega la versión y espacio libre del sistema"));
        commandsList.add(new Command(COMMAND_TYPE.DIR, "^DIR$", "Lista los archivos y directorios que hay en la dirección actual"));
        commandsList.add(new Command(COMMAND_TYPE.RD, "RD [a-zA-Z]{1,}[a-zA-Z0-9]{0,7}", "Elimina un archivo o directorio con el nombre indicado en la ruta actual"));
        commandsList.add(new Command(COMMAND_TYPE.PROMPT, "PROMPT|PROMPT \\$P|PROMPT \\$G|PROMPT \\$P \\$G|PROMPT \\$G \\$P", "Cambia la apariencia de la línea de comandos"));
        commandsList.add(new Command(COMMAND_TYPE.COPY_CON, "COPY CON [a-zA-Z]{1,}[a-zA-Z0-9]{0,7}", "Crea un archivo en la ruta actual"));
        commandsList.add(new Command(COMMAND_TYPE.TYPE, "TYPE [a-zA-Z]{1,}[a-zA-Z0-9]{0,7}", "Muestra el contenido del archivo especificado"));
        commandsList.add(new Command(COMMAND_TYPE.DEL, "DEL [a-zA-Z]{1,}[a-zA-Z0-9]{0,7}", "Elimina archivo especificado"));
        commandsList.add(new Command(COMMAND_TYPE.EXIT, "^EXIT$", "Finaliza el programa"));
        
    }
    
    private static String GetErrorMessage(INFORMATION_CODE informationCode)
    {
        for (InformationMessage errorMessage : informationMessagesList) 
        {
            if (errorMessage.getCode() == informationCode) 
            {
                return errorMessage.getDescription();
            }
        }
        
        return "";
    }
    
      private static void DisplayVersion()
    {
        // mensaje que se va desplegar al usuario con la información de la memoria restante 
        String versionMessage = "\nMINGOSOFT ® MIDOS \n" +
                            "© Copyright MINGOSOFT CORPORATION 2018\n" +
                            "Versión 1.0 Memoria libre: %d K Autor: Carlos Castro Brenes - Cédula: 1-1596-0319\n";
        
        switch (commandLineType)
        {
            case PROMPT:
            case PROMPT_PG:
                versionMessage += path + ">";
                break;
                
            case PROMPT_P:
                versionMessage += path;
                break;
                
            case PROMPT_G:
                versionMessage += ">";
                break;
           
            case PROMPT_GP:
                versionMessage += ">" + path;
                break;
        }
                            
        System.out.print(String.format(versionMessage, TOTAL_STORAGE));
    }
    
    private static void CheckCommand(String commandToSearch)
    {
        boolean isValidCommand = false;
       
        
        for (Command command : commandsList)
        {
            
            if (commandToSearch.toUpperCase().matches(command.getPatternAccepted())) 
            {
                isValidCommand = true;
                
                // comando encontrado
                switch(command.getCommandType())
                {
                    case CLS:
                        // se simula un limpiado de pantalla con 10 líneas en blanco
                        ClearScreen();
                        return;
                        
                    case DATE:
                        DisplayDate();
                        return;
                        
                    case TIME:
                        DisplayTime();
                        return;
                        
                    case MD:
                        CreateItem(commandToSearch.split("\\s+")[1], ItemType.DIRECTORY);
                        return;
                
                    case VER:
                        DisplayVersion();
                        return;
                        
                    case DIR:
                        ListDirectories();
                        return;
                        
                    case RD:
                        RemoveDirectory(commandToSearch.split("\\s+")[1]);
                        return;
                        
                    case CD:
                        ChangeDirectory(commandToSearch.split("\\s+")[1]);
                        return;
                        
                    case PROMPT:
                        ChangeCommandLineType(commandToSearch);
                        return;
                        
                    case COPY_CON:
                        CreateItem(commandToSearch.split("\\s+")[2], ItemType.FILE);
                        return;
                        
                    case TYPE:
                        ShowContent(commandToSearch.split("\\s+")[1]);
                        return;
                        
                    case DEL:
                        DeleteFile(commandToSearch.split("\\s+")[1]);
                        return;
                            
                    case EXIT:
                        Exit();
                        return;
                        
                }
            }
            
        }
        
        if (!isValidCommand) 
        {
            System.out.println(GetErrorMessage(INFORMATION_CODE.COMMAND_NOT_FOUND));
        }
       
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
            
            TOTAL_STORAGE = 256;
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
    
    private static void LoadItems()
    {
        try 
        {
            
            // Se indica el archivo con el que se desea trabajar
            FileInputStream file = new FileInputStream(DIRECTORIES_FILE);
            ObjectInputStream objectInputStream = new ObjectInputStream(file);
            currentItem = (Directory) objectInputStream.readObject();
            objectInputStream.close();
     
 
        } 
        catch (IOException e) 
        {
            System.out.println("Ha ocurrido un error al cargar los directorios creados");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("El archivo de almacenamiento de memoria contiene información en un formato no reconocido");
        }
    }
    
    private static void WriteItems()
    {
        try 
        {
            // se indica el archivo con el que se va trabajar y que trunque los datos para que siempre escriba la cantidad de espacio disponible
            // al inicio del archivo
            FileOutputStream file = new FileOutputStream(DIRECTORIES_FILE, false);
            ObjectOutputStream objectOutput = new ObjectOutputStream(file);
         
            // se escribe el directorio en el archivo
            // obtenemos el directorio root porque si escribimos el directorio actual y hemos navegado vamos a dejar de lado los ítems superiores
            objectOutput.writeObject(GetRootDirectory(currentItem));
            
            objectOutput.close();

        } 
        catch (IOException e) 
        {
            System.out.print("Ha ocurrido un error al salvar el almacenamiento disponible");
        }
    }
    
    private static void ListDirectories()
    {
         // se verifica si  hay directorios para listar
        if (currentItem.nextItems.size() > 0)
        {
            // si es así los listamos
            for (Object item : currentItem.nextItems) 
            {
                BaseItem baseItem = (BaseItem) item;
                System.out.println("\t" + baseItem.getName());

            }
        }
        else
        {
           // enc caso contrario indicamos que no se han encontrado directorios
            System.out.println("Aún no hay archivos agregados");
        }
        
    }
    
    private static Object GetItem(String name, ItemType itemType)
    {
        for (Object item : currentItem.nextItems)
        {
            switch (itemType) 
            {
                case DIRECTORY:
                    if (item.equals(new Directory(name, itemType)))
                    {
                        return (Directory) item;
                    }
                    break;
                    
                case FILE:
                    if (item.equals(new CustomFile(name, itemType)))
                    {
                        return (CustomFile) item;
                    }
                    break;
            }
            
        }
        
        return null;
    }
    
    private static boolean SearchItem(String name, ItemType itemType)
    {
        switch (itemType)
        {
            case DIRECTORY:
                return currentItem.nextItems.contains(new Directory (name, ItemType.DIRECTORY));
                
            case FILE:
                return currentItem.nextItems.contains(new CustomFile (name, ItemType.FILE));
                
            case DEFAULT:
                return currentItem.nextItems.contains(new Directory (name, ItemType.DIRECTORY)) || currentItem.nextItems.contains(new CustomFile (name, ItemType.FILE));
                
        }
        
        return false;
        
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
    
    private static void CreateItem(String name, ItemType itemType)
    {
        
        // se verifica la cantidad de hijos actuales
        if (currentItem.nextItems.size() >= 8) 
        {
            System.out.println("Ya se ha alcanzado la cantidad máxima de archivos y directorios (8 como máximo entre los dos), elimine alguno si desea crear otro");
        }
        else
        {
            
        
            switch (itemType)
            {
                case DIRECTORY:

                    // se verifica si hay espacio disponible para crear el directorio, cada uno ocupa 8k
                    if (TOTAL_STORAGE >= 8) 
                    {

                        if(SearchItem(name, ItemType.DIRECTORY))
                        {
                            System.out.println("Ya existe un archivo o directorio con ese nombre");
                        }
                        else
                        {
                            // cantidad de bytes que ocupan los directorios
                            TOTAL_STORAGE -= 8;

                            // se agrega el directorio
                            Directory newItem = new Directory(name, ItemType.DIRECTORY);

                            // se indica que el directorio anterior es en el que estamos ubicados
                            newItem.previousItem = currentItem;

                            // agregamos a los ítems siguientes el directorio que acabamos de agregar
                            currentItem.nextItems.add(newItem);

                            WriteItems();
                            WriteStorage();
                            System.out.println("El directorio se ha creado exitosamente");
                        }    
                    }
                    else    
                    {
                        System.out.println("No hay espacio disponible para crear el directorio");
                    }
                    break;

                case FILE:
                    // se verifica si hay espacio disponible para crear el archivo, cada uno ocupa 4k
                    if (TOTAL_STORAGE >= 4) 
                    {
                        if(SearchItem(name, ItemType.FILE))
                        {
                            System.out.println("Ya existe un archivo o directorio con ese nombre");
                        }
                        else
                        {
                            String content = "";

                            // cantidad de bytes que ocupan los archivos
                            TOTAL_STORAGE -= 4;

                            // se agrega el archivo
                            CustomFile customFile = new CustomFile(name, ItemType.FILE);

                            // solicitamos el contenido del archivo
                            System.out.println("Ingrese el texto del archivo, para indicar el fin debe ingresar ^Z");
                            content = scanner.nextLine().trim();

                            // verificamos si se ha indicado el fin del contenido, de lo contrario le indicamos que debe volver a ingresarlo
                            while (!content.endsWith("^Z") && !content.endsWith("^z"))
                            {
                                System.out.println("No indicó el fin del contenido (^Z), ingrese  nuevament el texto del archivo, para indicar el fin debe ingresar ^Z");
                                content = scanner.nextLine();
                            }

                            customFile.setContent(content.substring(0, content.length() - 3));

                            // agregamos a los ítems siguientes el directorio que acabamos de agregar
                            currentItem.nextItems.add(customFile);

                            WriteItems();
                            WriteStorage();
                            System.out.println("El archivo se ha creado exitosamente");
                        }
                    }
                    else    
                    {
                        System.out.println("No hay espacio disponible para crear el archivo");
                    }
                    break;
                    
                default: 
                    break;

            }
            
        }   
    }
    
    private static void ChangeDirectory(String navigation)
    {
        
        // verificamos si es una navegación hacia el padre
        if (navigation.equals(".."))
        {
            // verificamos que el padre no sea nulo porque el directorio lo tiene nulo
            if (currentItem.previousItem != null)
            {
      
                // buscamos la posición en la que está el nombre del directorio
                int lastBackslashPosition = path.indexOf(currentItem.getName());
                
                // actualizamos el path
                path = path.substring(0, lastBackslashPosition);
               
                // si no es nulo navegamos hacia él
                currentItem = currentItem.previousItem;
                    
            }
            else
            {
                System.out.println("Se encuentra en el directorio raíz, no se puede navegar hacia el directorio padre");
            }
        }
        else if (navigation.equals("\\"))
        {
            // se quiere navegar al directorio padre
            currentItem = GetRootDirectory(currentItem);
            
            path = "M:\\";
        }
        else
        {
            Directory item = (Directory) GetItem(navigation, ItemType.DIRECTORY);
        
            // verificamos si se encontró el ítem
            if (item != null)
            {

                // verificamos si es un directorio
                if (item.getType() == ItemType.DIRECTORY)
                {
                    // cambiamos de directorio
                    currentItem = item;

                    // actualizamos la ruta actual en pantalla
                    path = String.format("%s%s%s", path , navigation, "\\");
                }
                else
                {
                    System.out.println("El nombre indicado no es un directorio");
                }

            }
            else
            {
                // en caso contrario indicamos que no se ha encontrado el directorio
                System.out.println("No se ha encontrado el directorio indicado");
            }
        }
        
    }
    
    public static Directory GetRootDirectory(Directory item)
    {
        if (item.previousItem == null)
        {
            return item;
        }
        else
        {
            return GetRootDirectory(item.previousItem);
        }
    }
    
    private static void RemoveDirectory(String name)
    {
        
        Directory directory = (Directory) GetItem(name, ItemType.DIRECTORY);
        
        // revisamos que el directorio exista
        if (directory != null)
        {
            // verificamos que el directorio esté vacío
            if (directory.nextItems.size() > 0) 
            {
                System.out.println("El directorio no se encuentra vacío, debe eliminar primero el contenido de éste");
            }
            else
            {
                currentItem.nextItems.remove(directory);
            
                // se escribe el estado actual de los directorios
                WriteItems();

                // se suma el espacio eliminado
                TOTAL_STORAGE += 8;

                // se actualiza el espacio en memoria
                WriteStorage();

                System.out.println("El directorio se ha eliminado exitosamente");
            }
           
        }
        else
        {
            System.out.println("No se ha encontrado el archivo o directorio indicado");
        }
    }
    
    private static void ChangeCommandLineType(String commandToSearch)
    {
        if(commandToSearch.toUpperCase().matches("PROMPT"))
        {
            commandLineType = COMMAND_LINE_TYPE.PROMPT;
        }
        else if(commandToSearch.toUpperCase().matches("PROMPT \\$P"))
        {
            commandLineType = COMMAND_LINE_TYPE.PROMPT_P;
        }
        else if(commandToSearch.toUpperCase().matches("PROMPT \\$G"))
        {
            commandLineType = COMMAND_LINE_TYPE.PROMPT_G;
        }
        else if(commandToSearch.toUpperCase().matches("PROMPT \\$P \\$G"))
        {
            commandLineType = COMMAND_LINE_TYPE.PROMPT_PG;
        }
        else if(commandToSearch.toUpperCase().matches("PROMPT \\$G \\$P"))
        {
            commandLineType = COMMAND_LINE_TYPE.PROMPT_GP;
        }
    }
    
    /**
    * Muestra el contenido del archivo indicado
    *
    * @param  name : nombre del archivo a buscar
    */
    private static void ShowContent(String name)
    {
        // obtenemos la referencia al archivo 
        CustomFile customFile = (CustomFile) GetItem(name, ItemType.FILE);
        
        // revisamos que el directorio exista
        if (customFile != null)
        {
            System.out.println(customFile.getContent());
        }
        else
        {
            System.out.println("No se ha encontrado un archivo con ese nombre");
        }
    }
    
    
    private static void DeleteFile(String name)
    {
        CustomFile customFile = (CustomFile) GetItem(name, ItemType.FILE);
        
        // revisamos que el directorio exista
        if (customFile != null)
        {
            currentItem.nextItems.remove(customFile);

            // se escribe el estado actual de los directorios
            WriteItems();

            // se suma el espacio eliminado, 4 para archivos
            TOTAL_STORAGE += 4;

            // se actualiza el espacio en memoria
            WriteStorage();

            System.out.println("El archivo se ha eliminado exitosamente");
            
        }
        else
        {
            System.out.println("No se ha encontrado el archivo indicado");
        }
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
