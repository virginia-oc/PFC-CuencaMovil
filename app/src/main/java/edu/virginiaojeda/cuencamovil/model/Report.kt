package edu.virginiaojeda.cuencamovil.model

import androidx.core.graphics.component1
import edu.virginiaojeda.cuencamovil.R

data class Report(
    var id : String,
    var dateTime: String ? = null,
    var latitude : Double? = null,
    var longitude: Double? = null,
    var category : String? = null,
    var description : String? = null,
    var isIncident : Boolean? = null,
    var photoURLs : MutableList<String>? = null,
    var status : String? = null,
) {
    constructor(id: String,
                dateTime: String,
                latitude: Double,
                longitude: Double,
                category: String,
                description: String,
                isIncident: Boolean)
            : this(id, dateTime, latitude, longitude, category,
                     description, isIncident, mutableListOf(), "Enviado")

    constructor(id: String,
                dateTime: String,
                latitude: Double,
                longitude: Double,
                category: String,
                description: String,
                isIncident: Boolean,
                photoURLs: MutableList<String>)
            : this(id, dateTime, latitude, longitude, category,
                    description, isIncident, photoURLs, "Enviado")

    override fun toString(): String {
        return this.id + " - " + this.dateTime + " - " + this.latitude + " - " + this.longitude + " - " + this.category + " - " + this.description + " - " + this.isIncident + " - " + this.status
    }

}