/**
 * Clase que contiene los métodos para gestionar todas las acciones de CRUD contra la base
 * de datos de Firebase
 * @author Virginia Ojeda Corona
 */
package edu.virginiaojeda.cuencamovil.utils

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import edu.virginiaojeda.cuencamovil.model.Report
import edu.virginiaojeda.cuencamovil.model.ReportFirebase
import kotlinx.coroutines.tasks.await
import java.io.File

class DatabaseManager {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionName = "reports"
    lateinit var storage: FirebaseStorage
    var urlFirebase = "gs://cuenca-movil-22b63.appspot.com"
    var pathPhotosFirebaseList = mutableListOf<String>()

    /**
     * Crea un hashMap con los datos del reporte, lo agrega como un nuevo documento a la colección
     * 'reports' de la base de datos y maneja los casos de éxito y fallos
     * @param dataReport Objeto de tipo ReportFirebase que se quiere añadir a la base de datos
     */
    fun addData(dataReport: ReportFirebase) {
        val report = hashMapOf(
            "category" to dataReport.category,
            "dateTime" to dataReport.dateTime,
            "description" to dataReport.description,
            "isIncident" to dataReport.isIncident,
            "latitude" to dataReport.latitude,
            "longitude" to dataReport.longitude,
            "status" to dataReport.status,
            "photoURLs" to dataReport.photoURLs,
        )

        // Add a new document with a generated ID
        db.collection(collectionName)
            .add(report)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    /**
     * Itera sobre una lista de archivos de fotos y los almacena en Firebase. Registra los listener
     * que escuchan si el guardado a Firebase se ha realizado con éxito o no
     * @param photoFileList Lista que almacena las fotos que se quieren guardar en la base
     * de datos
     */
    fun savePhotoToFirebaseStorage(photoFileList: MutableList<File>){
        // Create a storage reference from our app
        storage = Firebase.storage(urlFirebase)
        val storageRef = storage.reference

        for (photoFile in photoFileList){
            var file = Uri.fromFile(photoFile)
            val reportRef = storageRef.child("images/${file.lastPathSegment}")
            val uploadTask = reportRef.putFile(file)

            pathPhotosFirebaseList.add("images/${file.lastPathSegment}")

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
            }
        }
    }

    /**
     * Devuelve la lista de URLs de las fotos almacenadas en Firebase
     * @return Objeto de tipo MutableList que contiene objetos de tipo String, que contienen las
     * URLs de las fotos guardadas en Firebase
     */
    fun getURLPhotoList() : MutableList<String>{
        return pathPhotosFirebaseList
    }

    /**
     * Obtiene todos los reports de la base de datos cuyo estado es 'Aceptado'. Registra un listener
     * para escuchar si la consulta se ha realizado con éxito o no
     * @return Objeto de tipo MutableList con los reports devueltos
     */
    suspend fun getAllReports() : MutableList<Report>{
        var reportList : MutableList<Report> = mutableListOf()
        db.collection(collectionName).whereEqualTo("status", "Aceptado")
            .get()
            .apply {
                addOnSuccessListener { result ->
                for (document in result) {
//                    Log.d(TAG, "${document.id} => ${document.data}")
                    val reportFirebase = document.toObject<ReportFirebase>()
                    val report = Report(
                        document.id,
                        reportFirebase.dateTime,
                        reportFirebase.latitude,
                        reportFirebase.longitude,
                        reportFirebase.category,
                        reportFirebase.description,
                        reportFirebase.isIncident,
                        reportFirebase.photoURLs,
                        reportFirebase.status
                    )
                    reportList.add(report)
                    Log.e("database", report.toString())
//                    Log.e("database", reportFirebase.toString())
                }
            }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }.await()
        Log.e("database", reportList.size.toString())
        return reportList
    }
}