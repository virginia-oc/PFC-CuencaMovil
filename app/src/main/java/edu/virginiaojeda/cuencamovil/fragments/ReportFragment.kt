/**
 * Fragment que contiene los métodos que crean los eventos de cada botón de la pantalla de reporte
 * y los métodos para garantizar los permisos de cámara y mapas.
 * @author Virginia Ojeda Corona
 */
package edu.virginiaojeda.cuencamovil.fragments

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
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
import androidx.exifinterface.media.ExifInterface
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
import edu.virginiaojeda.cuencamovil.databinding.ReportFragmentBinding
import edu.virginiaojeda.cuencamovil.model.ReportFirebase
import edu.virginiaojeda.cuencamovil.utils.DatabaseManager
import edu.virginiaojeda.cuencamovil.utils.FilesManager
import edu.virginiaojeda.cuencamovil.utils.ValidateFields
import java.io.File
import java.io.IOException
import java.util.*

class ReportFragment (activity: Activity, isIncident : Boolean): Fragment(), OnMapReadyCallback {
    lateinit var binding : ReportFragmentBinding
    private lateinit var contextFrag :Context
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var mMap: GoogleMap
    private var activity = activity
    private lateinit var currentLatLng : LatLng
    private val isIncident = isIncident

    private var photoFile: File? = null
    private val photoFileList: MutableList<File> = ArrayList()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ReportFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    /**
     * Crea los métodos de click de los elementos de la pantalla de notificar un reporte, muestra
     * el fragment del mapa en su contenedor e inicia el intent para la cámara
     * @param view
     * @param savedInstanceState
     */
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
                    photoFileList.add(photoFile!!)
                    photoFile = null

                    val imageView = ImageView(contextFrag)
                    val bitmap = BitmapFactory.decodeFile(photoFileList[photoFileList.size - 1].path)
                    imageView.setImageBitmap(bitmap)
                    imageView.rotation = 90.0f
                    imageView.layoutParams = LinearLayout.LayoutParams(
                        resources.getString(R.string.image_width).toInt(),
                        resources.getString(R.string.image_height).toInt()
                    )
                    if (isImagePortrait(photoFileList[photoFileList.size - 1]))
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

    /**
     * Método que dado un elemento imagen de tipo File devuelve 'true' si la imagen está tomada
     * en vertical y 'false' si es horizontal
     * @param image Variable de tipo File que contiene la foto de la cual queremos saber su
     * orientación
     * @return Valor booleano que indica si la imagen está en vertical (true) u horizontal (false)
     */
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

    /**
     * Llama a los métodos que se encargan de configurar los listeners de eventos para el spinner
     * de categorías y para el botón de enviar reporte.
     */
    override fun onStart() {
        super.onStart()
        setSpinnerCategoriesListener()
        setButtonSendReportListener()
    }

    /**
     * Método que crea y rellena el Spinner para elegir las diferentes categorías
     */
    private fun setSpinnerCategoriesListener(){
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
    }

    /**
     * Método que crea el evento para el botón de enviar reporte, llama a las funciones que
     * validan los datos del formulario y llama al método que guarda el reporte en la base de datos
     * @see DatabaseManager
     * @see ValidateFields
     */
    private fun setButtonSendReportListener(){
        binding.btnSendReport.setOnClickListener() {
            val databaseManager = DatabaseManager()
            databaseManager.savePhotoToFirebaseStorage(photoFileList)

            val validateFields = ValidateFields(binding, contextFrag)
            validateFields.validateCategory()
            validateFields.validateDescription()
            validateFields.validateLocation()
            val dateTime = validateFields.createDateTime()

            val dataReport = ReportFirebase(
                dateTime,
                currentLatLng.latitude,
                currentLatLng.longitude,
                binding.spCategories.selectedItem.toString(),
                binding.etDescription.text.toString(),
                isIncident,
                databaseManager.getURLPhotoList()
            )
            databaseManager.addData(dataReport)
        }
    }

    /**
     * Inicia la cámara para capturar una imagen, verifica los permisos necesarios y gestiona
     * el resultado de la actividad de la cámara
     * @param resultTakePicture Objeto de tipo ActivityResultLauncher utilizado para manejar el
     * resultado de la cámara
     */
    private fun startCamera(resultTakePicture : ActivityResultLauncher<Intent>){
        if (checkPermissionsCamera()){
            photoFile = FilesManager().createImageFile(contextFrag)
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
     * @see MainActivity
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

    /**
     * Método de devolución de llamada invocado cuando el mapa de Google está listo para ser
     * utilizado. Configura la interfaz de usuario del mapa (botones, brújula, etc.)
     * @param googleMap Objeto de tipo GoogleMap que representa el mapa que se mostrará en la
     * interfaz de usuario
     */
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

    /**
     * Verifica si se ha concedido el permiso de acceso a la ubicación en el contexto especificado
     * @return Valor booleano. Devuelve 'true' si el permiso ha sido concedido, y 'false' en caso
     * contrario
     * @see android.Manifest
     */
    private fun isPermissionGranted() = ContextCompat.checkSelfPermission(
        contextFrag, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    /**
     * Método encargado de configurar el mapa y realizar acciones relaciondas con la ubicación.
     * Si se ha concedido el permiso de ubicación, se habilita la función de mostrar la ubicación
     * actual en el mapa. Si el permiso no ha sido concedido, se solicita al usuario que otorgue
     * el permiso
     * @see android.Manifest
     */
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