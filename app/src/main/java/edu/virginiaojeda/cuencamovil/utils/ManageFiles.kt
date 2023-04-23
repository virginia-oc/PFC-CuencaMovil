package edu.virginiaojeda.cuencamovil.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ManageFiles {
    /**
     * Este método devuelve el directorio donde se guardará la imagen
     * @param context
     * @return Objeto de tipo File, que contiene el directorio de almacenamiento de la imagen
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