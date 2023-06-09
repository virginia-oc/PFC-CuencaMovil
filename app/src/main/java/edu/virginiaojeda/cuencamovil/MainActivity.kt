/**
 * Clase que contiene la actividad principal de la aplicación. Gestiona todos los fragment que
 * se muestran en la aplicación
 * @author Virginia Ojeda Corona
 */
package edu.virginiaojeda.cuencamovil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import edu.virginiaojeda.cuencamovil.databinding.ActivityMainBinding
import edu.virginiaojeda.cuencamovil.fragments.TemporalFragment
import edu.virginiaojeda.cuencamovil.fragments.HomeFragment
import edu.virginiaojeda.cuencamovil.fragments.ReportFragment
import edu.virginiaojeda.cuencamovil.fragments.ReportsListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var activity = this

    private val myBackPressed = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            var stackFragments = supportFragmentManager.backStackEntryCount
            if (binding.myDrawerLayout.isOpen) {
                binding.myDrawerLayout.close()
            } else if (stackFragments != 0){
                supportFragmentManager.popBackStack()
            }else
                finish()
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
            ).commit()
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
                    showFAQFragment()
                    true
                }
                (R.id.item12) -> {
                    showAppProblemsFragment()
                    true
                }
                (R.id.item21) -> {
                    showShareFragment()
                    true
                }
                (R.id.item31) -> {
                    showPrivacyPoliciesFragment()
                    true
                }
                (R.id.item32) -> {
                    showConditionsFragment()
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
    private fun showHomeFragment(){
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
     * Muestra el fragment de FAQ en la actividad principal. Además agrega la transicción a la
     * pila de retroceso para habilitar la navegación hacia atrás
     * @see TemporalFragment
     */
    private fun showFAQFragment(){
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
     * Muestra el fragment de AppProblems en la actividad principal. Además agrega la transicción a la
     * pila de retroceso para habilitar la navegación hacia atrás
     * @see
     */
    private fun showAppProblemsFragment(){
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
     * Muestra el fragment de Share en la actividad principal. Además agrega la transicción a la
     * pila de retroceso para habilitar la navegación hacia atrás
     * @see
     */
    private fun showShareFragment(){
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
     * Muestra el fragment de PrivacyPolicies en la actividad principal. Además agrega la
     * transicción a la pila de retroceso para habilitar la navegación hacia atrás
     * @see
     */
    private fun showPrivacyPoliciesFragment(){
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
     * Muestra el fragment de Conditions en la actividad principal. Además agrega la transicción a la
     * pila de retroceso para habilitar la navegación hacia atrás
     * @see
     */
    private fun showConditionsFragment(){
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
}