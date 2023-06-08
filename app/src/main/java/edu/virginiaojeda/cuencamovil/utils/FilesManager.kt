/**
 * Clase que contiene los métodos para gestionar los archivos de imagen tomados en la cámara
 * @author Virginia Ojeda Corona
 */
package edu.virginiaojeda.cuencamovil.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FilesManager {
    /**
     * Crea un archivo temporal con nombre único en el directorio de almacenamiento de imágenes.
     * Devuelve el objeto File que representa el archivo creado. Dicho archivo se utilizará más
     * tarde para almacenar una imagen
     * @param context
     * @return Objeto de tipo File del archivo temporal creado
     */
    fun createImageFile(context: Context) : File {
        //Se crea un timeStamp para el nombre del fichero:
        val timeStamp = SimpleDateFormat("yyyyMMdd"). format(Date())

        //Se obtiene el directorio según el path creado utilizando el provider
        val directoryStorage = context.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES
        )
        return File.createTempFile("IMG_$timeStamp", ".jpg", directoryStorage)
    }
}