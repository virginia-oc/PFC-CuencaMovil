package edu.virginiaojeda.cuencamovil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import edu.virginiaojeda.cuencamovil.databinding.ActivityMainBinding
import edu.virginiaojeda.cuencamovil.fragments.FAQFragment
import edu.virginiaojeda.cuencamovil.fragments.HomeFragment
import edu.virginiaojeda.cuencamovil.fragments.IncidentFragment
import edu.virginiaojeda.cuencamovil.fragments.RequestFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
     * Este método crea el primer fragment que aparece en la main activity. Como es el
     * primer fragment se utiliza add y no replace
     */
    private fun createHomeFragment(){
        supportFragmentManager.beginTransaction()
            .add(
                binding.fragmentContainer.id,
                HomeFragment()
            ).commit()
    }

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

    private fun showFAQFragment(){
        val transaction = supportFragmentManager.beginTransaction().apply {
            replace(
                binding.fragmentContainer.id,
                FAQFragment()
            )
            // Permite la vuelta "atrás".
            addToBackStack(null)
        }
        transaction.commit()
    }

    private fun showAppProblemsFragment(){

    }

    private fun showShareFragment(){

    }

    private fun showPrivacyPoliciesFragment(){

    }

    private fun showConditionsFragment(){

    }

    fun showNotifyIncidentFragment(){
        val transaction = supportFragmentManager.beginTransaction().apply {
            replace(
                binding.fragmentContainer.id,
                IncidentFragment()
            )
            // Permite la vuelta "atrás".
            addToBackStack(null)
        }
        transaction.commit()
    }

    fun showMakeRequestFragment(){
        val transaction = supportFragmentManager.beginTransaction().apply {
            replace(
                binding.fragmentContainer.id,
                RequestFragment()
            )
            // Permite la vuelta "atrás".
            addToBackStack(null)
        }
        transaction.commit()
    }

    fun showMapFragment(){
        val transaction = supportFragmentManager.beginTransaction().apply {
            replace(
                binding.fragmentContainer.id,
                RequestFragment()
            )
            // Permite la vuelta "atrás".
            addToBackStack(null)
        }
        transaction.commit()
    }
}