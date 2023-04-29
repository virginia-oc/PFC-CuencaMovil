package edu.virginiaojeda.cuencamovil.model

enum class Status {
    SENT,
    ACCEPTED,
    REJECTED,
    FINISHED
}

class Report(
    var id : Int,
    var dateTime: String,
    var latitude : Double,
    var longitude: Double,
    var category : String,
    var description : String,
    var isIncident : Boolean,
    var photo : ByteArray,
    var status : Status
) {
    constructor(id: Int,
                dateTime: String,
                latitude: Double,
                longitude: Double,
                category: String,
                description: String,
                incident: Boolean) : this(id, dateTime, latitude, longitude, category, description, incident,
                                        ByteArray(10) { 0 }, Status.SENT)
}