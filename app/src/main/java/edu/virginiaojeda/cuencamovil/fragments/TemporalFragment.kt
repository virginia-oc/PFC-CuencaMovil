/**
 * Fragment que muestra un mensaje de "en construcción" en las funcionalidades del menú que aún
 * están por implementar
 * @author Virginia Ojeda Corona
 */
package edu.virginiaojeda.cuencamovil.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.virginiaojeda.cuencamovil.databinding.TemporalFragmentBinding

class TemporalFragment : Fragment() {
    lateinit var binding : TemporalFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TemporalFragmentBinding.inflate(layoutInflater)
        return binding.root
    }
}