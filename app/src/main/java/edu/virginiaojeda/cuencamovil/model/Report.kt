package edu.virginiaojeda.cuencamovil.model

import com.google.type.DateTime
import edu.virginiaojeda.cuencamovil.R

enum class Status {
    SENT,
    ACCEPTED,
    REJECTED,
    FINISHED
}

class Report(
    var id : Int,
    var date: DateTime,
    var location : FloatArray,
    var category : String,
    var description : String,
    var incident : Boolean,
    var photo : ByteArray,
    var status : Status
) {
    constructor(id: Int,
                date: DateTime,
                location: FloatArray,
                category: String,
                description: String,
                incident: Boolean) : this(id, date, location, category, description, incident,
                                        ByteArray(10) { 0 }, Status.SENT)
}