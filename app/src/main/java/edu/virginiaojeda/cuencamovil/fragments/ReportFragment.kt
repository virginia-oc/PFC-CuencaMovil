package edu.virginiaojeda.cuencamovil.fragments

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import androidx.exifinterface.media.ExifInterface
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import edu.virginiaojeda.cuencamovil.MainActivity
import edu.virginiaojeda.cuencamovil.R
import edu.virginiaojeda.cuencamovil.databinding.IncidentFragmentBinding
import edu.virginiaojeda.cuencamovil.utils.ManageDatabase
import edu.virginiaojeda.cuencamovil.utils.ManageFiles
import edu.virginiaojeda.cuencamovil.utils.ValidateFields
import java.io.File
import java.io.IOException
import java.util.*

class ReportFragment (activity: Activity, isIncident : Boolean): Fragment(), OnMapReadyCallback {
    lateinit var binding : IncidentFragmentBinding
    private lateinit var contextFrag :Context
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var mMap: GoogleMap
    private var activity = activity
    private lateinit var currentLatLng : LatLng
    private val isIncident = isIncident

    private var photoFile: File? = null
    private var photoFileList = mutableListOf<File>()
    private val images: MutableList<File> = ArrayList()
    private val permissionIdCamera = 2

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
            configMap()
        } else {
            Log.d("PERMISOS", "Explicación de la necesidad del permiso.")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contextFrag = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = IncidentFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter.createFromResource(
            contextFrag,
            R.array.sp_categories,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spCategories.adapter = adapter
        binding.spCategories.setSelection(0)

        //Iniciar el mapa:
        val mapFragment = SupportMapFragment.newInstance()

        childFragmentManager.beginTransaction()
            .replace(R.id.map_container, mapFragment)
            .commit()

        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        //Iniciar la cámara:
        var resultTakePicture =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    images.add(photoFile!!)
                    photoFileList.add(photoFile!!)
                    photoFile = null

                    val imageView = ImageView(contextFrag)
                    val bitmap = BitmapFactory.decodeFile(images[images.size - 1].path)
                    imageView.setImageBitmap(bitmap)
                    imageView.rotation = 90.0f
                    imageView.layoutParams = LinearLayout.LayoutParams(
                        resources.getString(R.string.image_width).toInt(),
                        resources.getString(R.string.image_height).toInt()
                    ).apply {
//                            rightMargin =
                    }
                    if (isImagePortrait(images[images.size - 1]))
                        imageView.rotation = 90.0f
                    binding.imagesLinearLayout.addView(imageView)
                } else {
                    Toast.makeText(
                        contextFrag,
                        R.string.info_picture_canceled,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        binding.btnCamera.setOnClickListener(){
            startCamera(resultTakePicture)
        }
    }


    private fun isImagePortrait(image : File) : Boolean{
        try {
            val exif =
                ExifInterface(image.absolutePath)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL)
            return orientation == ExifInterface.ORIENTATION_ROTATE_90 ||
                    orientation == ExifInterface.ORIENTATION_ROTATE_270
        } catch (e: IOException) {
            // Manejar la excepción si ocurre algún error al leer el archivo de imagen
            e.printStackTrace()
        }
        return false
    }

    override fun onStart() {
        super.onStart()
        binding.spCategories.onItemSelectedListener =
            object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0){
                        binding.spCategories.setSelection(position)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

                override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    TODO("Not yet implemented")
                }
            }

        binding.btnSendReport.setOnClickListener() {
            val manageDatabase = ManageDatabase()
            manageDatabase.savePhotoToFirebaseStorage(photoFileList)

            val validateFields = ValidateFields(binding, contextFrag)
            validateFields.validateCategory()
            validateFields.validateDescription()
            validateFields.validateLocation()
            val dateTime = validateFields.createDateTime()

//            val dataReport = Report(
//                0,
//                dateTime,
//                currentLatLng.latitude,
//                currentLatLng.longitude,
//                binding.spCategories.selectedItem.toString(),
//                binding.etDescription.text.toString(),
//                isIncident,
//                Gestionar aqui lo de las fotos,
//                Gestionar aqui los status
//            )
//            val manageDatabase = ManageDatabase()
//            manageDatabase.addData(dataReport)
        }
    }

    //Funciones de la camara:
    private fun startCamera(resultTakePicture : ActivityResultLauncher<Intent>){
        if (checkPermissionsCamera()){
            photoFile = ManageFiles().createImageFile(contextFrag)
            val fileProvider =
                FileProvider.getUriForFile( // En base al provider creado en el Manifest.
                    contextFrag,
                    "edu.virginiaojeda.cuencamovil",
                    photoFile!!
                )

            //Se crea el intent y se le pasa el contenedor del fichero a recuperar
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(
                    MediaStore.EXTRA_OUTPUT, fileProvider
                )
                Log.e("", fileProvider.toString())
            }
            resultTakePicture.launch(intent)
        }else {
            requestPermissionsCamera()
        }
    }

    /**
     * Comprueba si están concedidos o no los permisos para el uso de la cámara del dispositivo
     * @return True si los permisos están concedidos, false en caso contrario
     */
    private fun checkPermissionsCamera() : Boolean{
        if (ActivityCompat.checkSelfPermission(
                contextFrag,
                CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    /**
     * Método que solicita al usuario conceder los permisos de cámara a la aplicación
     */
    private fun requestPermissionsCamera() {
        ActivityCompat.requestPermissions(
            (activity as MainActivity),
            arrayOf(
                CAMERA
            ),
            permissionIdCamera
        )
    }


    //Funciones del mapa:
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Se habilitan los botones del zoom.
        mMap.uiSettings.isZoomControlsEnabled = true
        // Se habilita la brújula, solo aparecerá cuando se gire el mapa.
        mMap.uiSettings.isCompassEnabled = true
        // Se habilita el botón para centrar la ubicación actual (por defecto es true).
        mMap.uiSettings.isMyLocationButtonEnabled = true

        configMap()
    }

    /// Comprueba el estado del permiso.
    private fun isPermissionGranted() = ContextCompat.checkSelfPermission(
        contextFrag, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    /// Comprueba el permiso de ubicación y recoloca el mapa según la ubicación.
    @Suppress("MissingPermission")
    private fun configMap() {
        when {
            (isPermissionGranted()) -> {
                // Se añade la marca en la ubicación real.
                mMap.isMyLocationEnabled = true
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        lastLocation = location
                        currentLatLng = LatLng(location.latitude, location.longitude)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                    }
                }
            }
            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
}