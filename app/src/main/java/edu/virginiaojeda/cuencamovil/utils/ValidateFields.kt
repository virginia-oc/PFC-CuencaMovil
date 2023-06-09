/**
 * Clase que contiene los métodos que validan los campos del formulario de notificación de reports
 * y el método que crea un string con la fecha y hora actuales
 * @author Virginia Ojeda Corona
 */
package edu.virginiaojeda.cuencamovil.utils

import android.content.Context
import edu.virginiaojeda.cuencamovil.databinding.ReportFragmentBinding
import java.text.SimpleDateFormat
import java.util.*

class ValidateFields(val binding: ReportFragmentBinding, val context: Context) {

    /**
     * Método que obtiene la fecha y la hora actuales y las devuelve en un string con un formato
     * determinado
     */
    fun createDateTime() : String{
        val calendar = Calendar.getInstance()
        val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        return dateTimeFormat.format(calendar.time)
    }
}