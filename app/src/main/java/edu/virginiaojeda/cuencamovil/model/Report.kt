package edu.virginiaojeda.cuencamovil.model

enum class Status {
    Enviado,
    Aceptado,
    Rechazado,
    Finalizado
}

class Report(
    var id : String,
    var dateTime: String,
    var latitude : Double,
    var longitude: Double,
    var category : String,
    var description : String,
    var isIncident : Boolean,
    var photoURLs : MutableList<String>,
    var status : Status
) {
    constructor(id: String,
                dateTime: String,
                latitude: Double,
                longitude: Double,
                category: String,
                description: String,
                isIncident: Boolean) : this(id, dateTime, latitude, longitude, category,
                                            description, isIncident, mutableListOf(), Status.Enviado)

    constructor(id: String,
                dateTime: String,
                latitude: Double,
                longitude: Double,
                category: String,
                description: String,
                isIncident: Boolean,
                photoURLs: MutableList<String>) : this(id, dateTime, latitude, longitude, category,
                                                    description, isIncident, photoURLs, Status.Enviado)
}