package edu.virginiaojeda.cuencamovil.model

data class ReportFirebase(
    var dateTime: String? = null,
    var latitude : Double? = null,
    var longitude: Double? = null,
    var category : String? = null,
    var description : String? = null,
    @field:JvmField // use this annotation if your Boolean field is prefixed with 'is'
    var isIncident : Boolean? = null,
    var photoURLs : MutableList<String>? = null,
    var status : String ? = null,
) {
    constructor(dateTime: String,
                latitude: Double,
                longitude: Double,
                category: String,
                description: String,
                isIncident: Boolean)
            : this(dateTime, latitude, longitude, category,
        description, isIncident, mutableListOf(), "Enviado")

    constructor(dateTime: String,
                latitude: Double,
                longitude: Double,
                category: String,
                description: String,
                isIncident: Boolean,
                photoURLs: MutableList<String>)
            : this(dateTime, latitude, longitude, category,
        description, isIncident, photoURLs, "Enviado")

    override fun toString(): String {
        return this.category + " --- " + this.description
    }

}