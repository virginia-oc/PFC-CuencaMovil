/**
 * Clase que contiene los métodos que validan los campos del formulario de notificación de reports
 * @author Virginia Ojeda Corona
 */
package edu.virginiaojeda.cuencamovil.utils

import android.content.Context
import edu.virginiaojeda.cuencamovil.R
import edu.virginiaojeda.cuencamovil.databinding.ReportFragmentBinding
import java.text.SimpleDateFormat
import java.util.*

class ValidateFields(val binding: ReportFragmentBinding, val context: Context) {
    val resources = context.resources

    /**
     * Método que valida la categoría
     */
    fun validateCategory() {
        if (binding.spCategories.selectedItem.toString() ==
            resources.getStringArray(R.array.sp_categories)[0]) {
        }
    }

    /**
     * Método que valida la descripción
     */
    fun validateDescription() {
        //TODO
    }

    /**
     * Método que valida la localización (latitud y longitud)
     */
    fun validateLocation() {
        //TODO
    }

    /**
     * Método que valida la fecha y hora
     */
    fun createDateTime() : String{
        val calendar = Calendar.getInstance()
        val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        return dateTimeFormat.format(calendar.time)
    }
}