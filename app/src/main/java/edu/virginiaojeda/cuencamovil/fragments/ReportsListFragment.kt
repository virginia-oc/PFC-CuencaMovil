package edu.virginiaojeda.cuencamovil.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
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
    //Esto soluciona el problema de asincronía que hay a la hora de recuperar todos los
    //reportes de Firebase (Firebase tiene un funcionamiento asíncrono y la vista de la
    // recycler view se visualizaba antes de guardarse todos los reportes en la mutableList)
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    lateinit var binding : ReportslistFragmentBinding
    private lateinit var contextFrag : Context
    private var databaseManager = DatabaseManager()
    private lateinit var reportsRVAdapter : ReportsRVAdapter
    val TAG = "ReportListFragment"

    companion object{
        private lateinit var reportsList : MutableList<Report>
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
        binding = ReportslistFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launch{
            reportsList = databaseManager.getAllReports()
            reportsRVAdapter = ReportsRVAdapter(reportsList)
            Log.e("CANTIDAD", reportsList.size.toString())
            setUpRecyclerView()
        }

    }


    /**
     * Inicializa el recycled view
     * @param myRVAdapter
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

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
        job.cancel()
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach")
        super.onDetach()
    }
}