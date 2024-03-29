/**
 * Fragment que contiene los métodos para mostrar la pantalla de inicio.
 * @author Virginia Ojeda Corona
 */
package edu.virginiaojeda.cuencamovil.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.virginiaojeda.cuencamovil.MainActivity
import edu.virginiaojeda.cuencamovil.databinding.HomeFragmentBinding

class HomeFragment : Fragment(){
    lateinit var binding: HomeFragmentBinding
    private lateinit var contextFrag :Context
    val TAG = "HomeFragment"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contextFrag = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    /**
     * Crea los eventos de click para cada botón de la pantalla de inicio
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNotifyIncident.setOnClickListener(){
            (activity as MainActivity).showReportFragment(true)
        }

        binding.btnMakeRequest.setOnClickListener(){
            (activity as MainActivity).showReportFragment(false)
        }

        binding.btnAllReports.setOnClickListener(){
            (activity as MainActivity).showReportsListFragment()
        }

        binding.btnMyReports.setOnClickListener(){
            (activity as MainActivity).showTemporalFragment()
        }

        binding.btnAssociations.setOnClickListener(){
            (activity as MainActivity).showTemporalFragment()
        }

        binding.btnSocialMedia.setOnClickListener(){
            (activity as MainActivity).showTemporalFragment()
        }
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
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
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach")
        super.onDetach()
    }
}