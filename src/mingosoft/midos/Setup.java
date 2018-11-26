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
import java.util.Collections;
import java.util.Comparator;
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
    
    // mensaje que se va desplegar al usuario con la información de la memoria restante 
    static String versionMessage = "\nMINGOSOFT ® MIDOS \n" +
                            "© Copyright MINGOSOFT CORPORATION 2018\n" +
                            "Versión 1.0 Memoria libre: %d K Autor: Carlos Castro Brenes - Cédula: 1-1596-0319\n";
    
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
        DisplayVersion();
        DisplayPath();
        
        // se debe repetir hasta que el usuario desee salir mediante el comando EXIT
        do 
        {
            optionEntered = "";

            optionEntered = scanner.nextLine();

            // se verifica si el texto ingresado corresponde a un comando
            CheckCommand(optionEntered);
            
            DisplayPath();
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
        // estamos agregando los espacios antes y después de los comandos, hay que seguir revisando los comando restantes para eso ver el
        // bloc de notas
        commandsList.add(new Command(COMMAND_TYPE.CLS,      "[ \\t]*CLS[ \\t]*", "CL", "Permite limpiar la pantalla de la consola", "Para ejecutar el comando debe ingresar CLS seguido de la tecla enter"));
        commandsList.add(new Command(COMMAND_TYPE.DATE,     "[ \\t]*DATE[ \\t]*", "DA", "Despliega la fecha del sistema", "Para ejecutar el comando debe ingresar DATE seguido de la tecla enter"));
        commandsList.add(new Command(COMMAND_TYPE.TIME,     "[ \\t]*TIME[ \\t]*", "TI", "Despliega la hora del sistema", "Para ejecutar el comando debe ingresar TIME seguido de la tecla enter"));
        commandsList.add(new Command(COMMAND_TYPE.MD,       "[ \\t]*MD[ \\t]+[a-zA-Z]{1,}[a-zA-Z0-9]{0,7}[ \\t]*", "MD" ,"Crea un directorio en la ruta actual", "Para ejecutar el comando debe ingresar MD + espacio + nombre del directorio a crear seguido de la tecla enter, el nombre debe inicar con letras y puede contener números, su longitud es de 8 caracteres como máximo.\nPor ejemplo: MD directorio1"));
        commandsList.add(new Command(COMMAND_TYPE.CD,       "[ \\t]*CD[ \\t]+[a-zA-Z]{1,}[a-zA-Z0-9]{0,7}[ \\t]*|[ \\t]*CD[ \\t]*[.]{2,2}[ \\t]*|[ \\t]*CD[ \\t]*[\\\\]{1,1}[ \\t]*", "CD" ,"Cambia al directorio especificado", "Para ejecutar el comando debe ingresar CD + espacio + nombre del directorio a cambiar seguido de la tecla enter, el nombre debe inicar con letras y puede contener números, su longitud es de 8 caracteres como máximo.\nPor ejemplo: CD directorio1"));
        commandsList.add(new Command(COMMAND_TYPE.VER,      "[ \\t]*VER[ \\t]*", "VE" ,"Despliega la versión y espacio libre del sistema", "Para ejecutar el comando debe ingresar VER seguido de la tecla enter"));
        commandsList.add(new Command(COMMAND_TYPE.DIR,      "[ \\t]*DIR[ \\t]*", "DI" ,"Lista los archivos y directorios que hay en la dirección actual", "Para ejecutar el comando debe ingresar DIR seguido de la tecla enter"));
        commandsList.add(new Command(COMMAND_TYPE.RD,       "[ \\t]*RD[ \\t]+[a-zA-Z]{1,}[a-zA-Z0-9]{0,7}[ \\t]*", "RD" ,"Elimina un directorio con el nombre indicado en la ruta actual", "Para ejecutar el comando debe ingresar RD + espacio + nombre del directorio a eliminar seguido de la tecla enter.\nPor ejemplo: RD directorio1"));
        commandsList.add(new Command(COMMAND_TYPE.PROMPT,   "[ \\t]*PROMPT[ \\t]*|[ \\t]*PROMPT[ \\t]+\\$P[ \\t]*|[ \\t]*PROMPT[ \\t]+\\$G[ \\t]*|[ \\t]*PROMPT[ \\t]+\\$P[ \\t]*\\$G[ \\t]*|[ \\t]*PROMPT[ \\t]+\\$G[ \\t]*\\$P[ \\t]*", "PR" ,"Cambia la apariencia de la línea de comandos", "Para ejecutar el comando debe ingresar PROMPT seguido de la tecla enter"));
        commandsList.add(new Command(COMMAND_TYPE.COPY_CON, "[ \\t]*COPY[ \\t]+CON[ \\t]+[a-zA-Z]{1,}[a-zA-Z0-9]{0,7}[ \\t]*", "CO" ,"Crea un archivo en la ruta actual", "Para ejecutar el comando debe ingresar COPY + espacio + CON + nombre del archivo a crear seguido de la tecla enter, el nombre debe inicar con letras y puede contener números, su longitud es de 8 caracteres como máximo.\nPor ejemplo: COPY CON archivo1"));
        commandsList.add(new Command(COMMAND_TYPE.TYPE,     "[ \\t]*TYPE[ \\t]+[a-zA-Z]{1,}[a-zA-Z0-9]{0,7}[ \\t]*", "TY", "Muestra el contenido del archivo especificado", "Para ejecutar el comando debe ingresar TYPE + espacio + nombre del archivo seguido de la tecla enter, el nombre debe inicar con letras y puede contener números, su longitud es de 8 caracteres como máximo.\nPor ejemplo: TYPE archivo1"));
        commandsList.add(new Command(COMMAND_TYPE.DEL,      "[ \\t]*DEL[ \\t]+[a-zA-Z]{1,}[a-zA-Z0-9]{0,7}[ \\t]*", "DE", "Elimina archivo especificado", "Para ejecutar el comando debe ingresar DEL + espacio + nombre del archivo a eliminar seguido de la tecla enter, el nombre debe inicar con letras y puede contener números, su longitud es de 8 caracteres como máximo.\nPor ejemplo: DEL archivo1"));
        commandsList.add(new Command(COMMAND_TYPE.REN,      "[ \\t]*REN[ \\t]+[a-zA-Z]{1,}[a-zA-Z0-9]{0,7}[ \\t]+[a-zA-Z]{1,}[a-zA-Z0-9]{0,7}[ \\t]*", "RE" , "Cambia el nombre del archivo o directorio especificado", "Para ejecutar el comando debe ingresar REN + espacio + nombre del archivo del que se desea cambiar el nombre + espacio + nuevo nombre seguido de la tecla enter, el nombre debe inicar con letras y puede contener números, su longitud es de 8 caracteres como máximo.\nPor ejemplo: REN archivo1 miarchivo"));
        commandsList.add(new Command(COMMAND_TYPE.TREE,     "[ \\t]*TREE[ \\t]*", "TR", "Despliega la jerarquía de los directorios", "Para ejecutar el comando debe ingresar TREE seguido de la tecla enter"));
        commandsList.add(new Command(COMMAND_TYPE.EXIT,     "[ \\t]*EXIT[ \\t]*", "EX", "Finaliza el programa", "Para ejecutar el comando debe ingresar EXIT seguido de la tecla enter"));
        
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
       
        System.out.print(String.format(versionMessage, TOTAL_STORAGE));
    }
      
    private static void DisplayPath()
    {
        System.out.println();
        switch (commandLineType)
        {
            case PROMPT:
            case PROMPT_PG:
                System.out.print(path + ">");
                break;
                
            case PROMPT_P:
                System.out.print(path);
                break;
                
            case PROMPT_G:
                System.out.print(">");
                break;
           
            case PROMPT_GP:
                System.out.print(">" + path);
                break;
        }
    }
    
    private static void CheckCommand(String commandToSearch)
    {
        boolean isValidCommand = false;
        boolean isMistyped = false;
        
        
        for (Command command : commandsList)
        {
            commandToSearch = commandToSearch.trim();
            
            if (commandToSearch.toUpperCase().matches(command.getAcceptedPattern())) 
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
                        ChangeDirectory(commandToSearch);
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
                        
                    case REN:
                        try{
                            String [] names = commandToSearch.split("\\s+");
                        RenameItem(names[1], names[2]);
                        }catch (Exception e)
                        {
                            throw e;
                        }
                        
                        return;
                        
                    case TREE:
                        DisplayAllFiles();
                        return;
                            
                    case EXIT:
                        Exit();
                        return;
                        
                }
            }
            else if (commandToSearch.trim().toUpperCase().startsWith(command.getAcceptedKeyWord()))
            {
                isMistyped = true;
                System.out.println(command.getHelp());
            }
        }
        
        // !commandToSearch.isEmpty() permite línea vacías
        if (!isValidCommand && !commandToSearch.isEmpty() && !isMistyped) 
        {
            // se verifica si se ingresaron mal los parámetros de un comando para indicarle cómo se debe utilizar
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
    
    private static void SortItemsByName(Directory directory)
    {
        // creamos un comparador de tipo object para ordenar alfabéticamente
        Collections.sort(directory.nextItems, new Comparator<Object>() {
            public int compare(Object firstObject, Object secondObject) {
            
                // como son de tipo object entonces hacemos un casting para poder acceder a las propiedades
                return ((BaseItem)firstObject).getName().compareTo(((BaseItem)secondObject).getName());
            }
         });
        
    }
    
    private static void ListDirectories()
    {
         // se verifica si  hay directorios para listar
        if (currentItem.nextItems.size() > 0)
        {
            // ordenamos los ítems alfabéticamente
            SortItemsByName(currentItem);
            System.out.println("\nDirectorio de " + path);
            
            String nameOfType = "";
            int quantityOfDirectories = 0;
            int quantityOfFiles = 0;
            
            // si es así los listamos
            for (Object item : currentItem.nextItems) 
            {
                BaseItem baseItem = (BaseItem) item;
                
                switch (baseItem.getType())
                {
                    case DIRECTORY:
                        nameOfType = "<DIR>";
                        quantityOfDirectories ++;
                        break;
                        
                    case FILE:
                        nameOfType = "arch";
                        quantityOfFiles ++;
                        break;
                }       
                
                // %1$-12s coloca 12 caracteres del primer string, si es menor de 12 lo rellena con ceros
                // %2$s hace referencia al segundo parámetro de tipo string
                System.out.println(String.format("%1$-12s %2$s", baseItem.getName(), nameOfType));
                
            }
            
            System.out.println(String.format("\n%d archivos", quantityOfFiles));
            System.out.println(String.format("%d directorios", quantityOfDirectories));
            System.out.println(String.format("%d K libres", TOTAL_STORAGE));
            
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
        else if (name.length() > 8)
        {
            System.out.println("El nombre debe tener como máximo 8 caracteres");
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
       
        String navigationType = navigation.toUpperCase().replace("CD", " ").trim();
        
        // verificamos si es una navegación hacia el padre
        if (navigationType.equals(".."))
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
        else if (navigationType.equals("\\"))
        {
            // se quiere navegar al directorio padre
            currentItem = GetRootDirectory(currentItem);
            
            path = "M:\\";
        }
        else
        {
            
            // no hace falta validar si se tiene acceso al índice porque si llegó acá
            // es porque cumplió el regex y si no cae en las opciones anteriores cae en esta
            String nameOfDirectory = navigation.split("\\s+")[1];
        
            Directory item = (Directory) GetItem(nameOfDirectory, ItemType.DIRECTORY);
        
            // verificamos si se encontró el ítem
            if (item != null)
            {

                // verificamos si es un directorio
                if (item.getType() == ItemType.DIRECTORY)
                {
                    // cambiamos de directorio
                    currentItem = item;

                    // actualizamos la ruta actual en pantalla
                    path = String.format("%s%s%s", path , nameOfDirectory, "\\");
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
        else if(commandToSearch.toUpperCase().matches("PROMPT[ \\t]*\\$P"))
        {
            commandLineType = COMMAND_LINE_TYPE.PROMPT_P;
        }
        else if(commandToSearch.toUpperCase().matches("PROMPT[ \\t]*\\$G"))
        {
            commandLineType = COMMAND_LINE_TYPE.PROMPT_G;
        }
        else if(commandToSearch.toUpperCase().matches("PROMPT[ \\t]+\\$P[ \\t]*\\$G"))
        {
            commandLineType = COMMAND_LINE_TYPE.PROMPT_PG;
        }
        else if(commandToSearch.toUpperCase().matches("PROMPT[ \\t]+\\$G[ \\t]*\\$P"))
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
  
    
    private static void RenameItem(String oldName, String newName)
    {
        // verificamos si el nombre no excede la cantida de caracteres
        if (newName.length() > 8)
        {
            System.out.println("El nombre debe tener como máximo 8 caracteres");
        }
        else
        {
            
             // verificamos si existe el archivo o directorio
            if (SearchItem(oldName, ItemType.DEFAULT)) 
            {

                // verificamos si ya existe un directorio con el nuevo nombre
                if (SearchItem(newName, ItemType.DEFAULT))  
                {
                    System.out.println("Ya existe un archivo o directorio con el nuevo nombre");
                }
                else
                {
                    // si lo encuentra vamos a buscarlo
                    int directoryPosition = currentItem.nextItems.indexOf(new Directory(oldName, ItemType.DIRECTORY));
                    int filePosition = currentItem.nextItems.indexOf(new CustomFile(oldName, ItemType.FILE));

                    // -1 indica que no se encontró
                    if (directoryPosition > -1) 
                    {
                        Directory directory = (Directory) currentItem.nextItems.get(directoryPosition);
                        directory.setName(newName);
                        currentItem.nextItems.set(directoryPosition, directory);

                    }
                    else if (filePosition > -1)
                    {
                        CustomFile customFile = (CustomFile) currentItem.nextItems.get(filePosition);
                        customFile.setName(newName);
                        currentItem.nextItems.set(filePosition, customFile);
                    }

                    // se escribe el estado actual de los directorios
                    WriteItems();

                    System.out.println("El nombre se ha cambiado exitosamente");
                }

            }
            else
            {
                System.out.println("No se ha encontrado el archivo o directorio con el nombre especificado");
            }

        }
    }
    
    private static void PrintDirectory(Object directory, String tabulation)
    {
        Directory directoryToIterate = (Directory) directory;
        
        // ordenamos la colección actual
        SortItemsByName(directoryToIterate);
        
        for (Object item : directoryToIterate.nextItems) 
        {
            BaseItem baseItem = (BaseItem) item;
            
            System.out.println(tabulation + baseItem.name);

            // verificamos si el ítem actual es un directorio, si es así debemos ingresar a él e iterar
            if (baseItem.getType() == ItemType.DIRECTORY) 
            {
                PrintDirectory(item, tabulation + " ");
            }
            
        }
    }
    
    private static void DisplayAllFiles()
    {
        // almacena temporalmente el directorio raíz
        Directory rootDirectory = GetRootDirectory(currentItem);
        
         // se verifica si  hay directorios para listar
        if (rootDirectory.nextItems.size() > 0)
        {
            
            System.out.println("Listado de rutas de directorios para el volumen MIDOS\n" +
                                "El número de serie del volumen es: Carlos Castro Brenes 1-1596-0319\n" +
                                "M:\\\n");
            
            PrintDirectory(rootDirectory, " ");
            
        }
        else
        {
           // enc caso contrario indicamos que no se han encontrado directorios
            System.out.println("Aún no hay archivos agregados");
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

            String selectedOption = scanner.next().trim();

            if (selectedOption.equalsIgnoreCase("S")) 
            {
                System.out.println("El programa va finalizar\n¡Te esperamos de vuelta pronto!");
                exit = true;
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
