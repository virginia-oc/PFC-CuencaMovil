/**
 * Clase que contiene la actividad principal de la aplicación. Gestiona todos los fragment que
 * se muestran en la aplicación
 * @author Virginia Ojeda Corona
 */
package edu.virginiaojeda.cuencamovil

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import edu.virginiaojeda.cuencamovil.databinding.ActivityMainBinding
import edu.virginiaojeda.cuencamovil.fragments.HomeFragment
import edu.virginiaojeda.cuencamovil.fragments.ReportFragment
import edu.virginiaojeda.cuencamovil.fragments.ReportsListFragment
import edu.virginiaojeda.cuencamovil.fragments.TemporalFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var activity = this

    private val myBackPressed = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            var currentFragment =
                supportFragmentManager.findFragmentById(binding.fragmentContainer.id)
            var stackFragments = supportFragmentManager.backStackEntryCount

            if (binding.myDrawerLayout.isOpen) {
                binding.myDrawerLayout.close()
            } else if (currentFragment is HomeFragment) {
                showExitConfirmDialog()
            } else if (stackFragments != 0) {
                supportFragmentManager.popBackStack()
            } else {
                showExitConfirmDialog()
            }
        }
    }

    /**
     * Realiza la configuración inicial de la actividad, infla el diseño, configura la barra
     * de acción y el panel lateral y crea y muestra el fragment de inicio
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, myBackPressed)

        //Cargamos la barra de acción:
        setSupportActionBar(binding.myToolBar)

        // Se añade el botón “hamburgesa” a la toolbar y se vincula con el DrawerLayout.
        val toggle = ActionBarDrawerToggle(
            this,
            binding.myDrawerLayout,
            binding.myToolBar,
            R.string.txt_open,
            R.string.txt_close
        )
        binding.myDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        createHomeFragment()
        buildMenuOptions()
    }

    /**
     * Crea el primer fragment que aparece en la main activity. Como es el primer fragment se
     * utiliza add y no replace
     * @see HomeFragment
     */
    private fun createHomeFragment(){
        supportFragmentManager.beginTransaction()
            .add(
                binding.fragmentContainer.id,
                HomeFragment()
            )
            .commit()
    }

    /**
     * Construye las opciones de menú en el panel lateral de navegación y establece la lógica
     * para cada opción de menú
     */
    private fun buildMenuOptions(){
        binding.myNavigationView.setNavigationItemSelectedListener { menuItem ->
            binding.myDrawerLayout.close()

            when (menuItem.itemId){
                (R.id.item01) -> {
                    showHomeFragment()
                    true
                }
                (R.id.item11) -> {
                    showTemporalFragment()
                    true
                }
                (R.id.item12) -> {
                    showTemporalFragment()
                    true
                }
                (R.id.item21) -> {
                    showTemporalFragment()
                    true
                }
                (R.id.item31) -> {
                    showTemporalFragment()
                    true
                }
                (R.id.item32) -> {
                    showTemporalFragment()
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Muestra el fragment de inicio en la actividad principal. Además agrega la transicción a la
     * pila de retroceso para habilitar la navegación hacia atrás
     * @see HomeFragment
     */
    fun showHomeFragment(){
        val transaction = supportFragmentManager.beginTransaction().apply {
            replace(
                binding.fragmentContainer.id,
                HomeFragment()
            )
            // Permite la vuelta "atrás".
            addToBackStack(null)
        }
        transaction.commit()
    }

    /**
     * Muestra el fragment de notificar report en la actividad principal. Además agrega la
     * transicción a la pila de retroceso para habilitar la navegación hacia atrás
     * @see ReportFragment
     */
    fun showReportFragment(isIncident : Boolean){
        val transaction = supportFragmentManager.beginTransaction().apply {
            replace(
                binding.fragmentContainer.id,
                ReportFragment(activity, isIncident)
            )
            // Permite la vuelta "atrás".
            addToBackStack(null)
        }
        transaction.commit()
    }

    /**
     * Muestra el fragment del listado de reports en la actividad principal. Además agrega la
     * transicción a la pila de retroceso para habilitar la navegación hacia atrás
     * @see ReportsListFragment
     */
    fun showReportsListFragment(){
        val transaction = supportFragmentManager.beginTransaction().apply {
            replace(
                binding.fragmentContainer.id,
                ReportsListFragment()
            )
            // Permite la vuelta "atrás".
            addToBackStack(null)
        }
        transaction.commit()
    }

    /**
     * Muestra un fragment temporal que notifica al usuario que la funcionalidad a la que está
     * intentando acceder está en construcción
     * @see TemporalFragment
     */
    fun showTemporalFragment(){
        val transaction = supportFragmentManager.beginTransaction().apply {
            replace(
                binding.fragmentContainer.id,
                TemporalFragment()
            )
            // Permite la vuelta "atrás".
            addToBackStack(null)
        }
        transaction.commit()
    }

    /**
     * Muestra un alert dialog pidiendo confirmación para salir de la aplicación
     */
    private fun showExitConfirmDialog() {
        val alertDialog = AlertDialog.Builder(this).apply {
            setTitle(R.string.title_dialogExitApp)
            setMessage(R.string.message_dialogExitApp)
            setPositiveButton(R.string.option1_dialogExitApp) { dialog, _ ->
                // Acciones a realizar cuando se presiona el botón "Aceptar"
                dialog.dismiss() // Cerrar el diálogo
                finish()
            }
            setNegativeButton(R.string.option2_dialogExitApp){ dialog, _ ->
                dialog.dismiss()
            }
        }.create()
        alertDialog.show()
    }
}