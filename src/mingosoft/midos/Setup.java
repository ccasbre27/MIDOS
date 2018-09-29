/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mingosoft.midos;

import java.io.IOException;
import java.util.ArrayList;

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
    
    static ArrayList <ErrorMessage> errorMessagesList = new ArrayList<ErrorMessage>();
    
    public static void main (String [] args) throws IOException
    {
        // se cargan los mensajes de error
        LoadErrorMessages();
        
        // se debe repetir hasta que el usuario desee salir mediante el comando EXIT
        
        System.out.print(versionMessage);
        System.in.read();
    }
    
    private static void LoadErrorMessages()
    {
        errorMessagesList.add(new ErrorMessage(1, "Comando inválido"));
        errorMessagesList.add(new ErrorMessage(2, "Opción inválida"));
        errorMessagesList.add(new ErrorMessage(3, "Ya existe un directorio con ese nombre: nombre del directorio"));
    }
    
}
