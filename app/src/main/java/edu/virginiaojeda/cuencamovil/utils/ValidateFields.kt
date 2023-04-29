package edu.virginiaojeda.cuencamovil.utils

import android.content.Context
import edu.virginiaojeda.cuencamovil.R
import edu.virginiaojeda.cuencamovil.databinding.IncidentFragmentBinding
import java.text.SimpleDateFormat
import java.util.*

class ValidateFields(val binding: IncidentFragmentBinding, val context: Context) {
    val resources = context.resources

    fun validateCategory() {
        if (binding.spCategories.selectedItem.toString() ==
            resources.getStringArray(R.array.sp_categories)[0]) {

        }
    }

    fun validateDescription() {
        //TODO
    }

    fun validateLocation() {
        //TODO
    }

    fun createDateTime() : String{
        val calendar = Calendar.getInstance()
        val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        return dateTimeFormat.format(calendar.time)
    }
}