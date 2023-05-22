package edu.virginiaojeda.cuencamovil.utils

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import edu.virginiaojeda.cuencamovil.model.Report
import java.io.File
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class ManageDatabase {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionName = "reports"
    lateinit var storage: FirebaseStorage
    var urlFirebase = "gs://cuenca-movil-22b63.appspot.com"
    var pathPhotosFirebaseList = mutableListOf<String>()

    fun addData(dataReport: Report) {
        // Create a new user with a first and last name
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

    fun getURLPhotoList() : MutableList<String>{
        return pathPhotosFirebaseList
    }

    fun getIncidents(){
        db.collection(collectionName)
            .whereEqualTo("incident", true)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    fun getRequests(){
        db.collection(collectionName)
            .whereEqualTo("incident", false)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    fun getReportsByUser(user : String){
        //en id podría poner el email del usuario registrado
        db.collection(collectionName)
            .whereEqualTo("id", "")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    fun getAllReports(){
        db.collection(collectionName)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }
}