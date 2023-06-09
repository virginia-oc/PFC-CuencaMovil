/**
 * Fragment que contiene los métodos que muestran y configuran la recycler view que contiene
 * la lista de todos los reportes
 * @author Virginia Ojeda Corona
 */
package edu.virginiaojeda.cuencamovil.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import edu.virginiaojeda.cuencamovil.adapters.ReportsRVAdapter
import edu.virginiaojeda.cuencamovil.databinding.ReportslistFragmentBinding
import edu.virginiaojeda.cuencamovil.model.Report
import edu.virginiaojeda.cuencamovil.utils.DatabaseManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class ReportsListFragment() : Fragment(), CoroutineScope {
    /**
     * Variable que soluciona el problema de asincronía que hay a la hora de recuperar todos los
     * reportes de Firebase.
     */
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    lateinit var binding : ReportslistFragmentBinding
    private lateinit var contextFrag : Context
    private var databaseManager = DatabaseManager()
    private lateinit var reportsRVAdapter : ReportsRVAdapter

    companion object{
        private lateinit var reportsList : MutableList<Report>
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
        binding = ReportslistFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    /**
     * Ejecuta una corutina para realizar operaciones asíncronas, en este caso obtener todos los
     * reportes de Firebase, configura el adaptador de la recyclerView y muestra la lista en la
     * interfaz de usuario
     * @param view
     * @param savedInstanceState
     * @see ReportsRVAdapter
     * @see databaseManager
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launch{
            reportsList = databaseManager.getAllReports()
            reportsRVAdapter = ReportsRVAdapter(reportsList)
            //Log.e("CANTIDAD", reportsList.size.toString())
            setUpRecyclerView()
        }
    }

    /**
     * Configura el recyclerView en la interfaz de usuario
     */
    private fun setUpRecyclerView() {
        // Esta opción a TRUE significa que el RV tendrá
        // hijos del mismo tamaño, optimiza su creación.
        binding.rvReportsList.setHasFixedSize(true)
        // Se indica el contexto para RV en forma de lista.
        binding.rvReportsList.layoutManager = LinearLayoutManager(contextFrag)
        // Se asigna el adapter al RV.
        binding.rvReportsList.adapter = reportsRVAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}