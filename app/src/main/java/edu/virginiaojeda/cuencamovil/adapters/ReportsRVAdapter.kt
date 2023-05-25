package edu.virginiaojeda.cuencamovil.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.virginiaojeda.cuencamovil.databinding.ItemRvReportBinding
import edu.virginiaojeda.cuencamovil.model.Report

class ReportsRVAdapter(val reportList : MutableList<Report>?) :
        RecyclerView.Adapter<ReportsRVAdapter.ViewHolder>() {


    /**
     * Método encargado de inflar la vista
     * @param parent
     * @param viewType
     * @return Objeto de tipo ViewHolder que contiene cada item de la RecyclerView inflado
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemRvReportBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    /**
     * Esta clase se encarga de inflar cada uno de los items que contiene la RecyclerView
     * @param view
     * * @return Objeto de tipo ViewHolder que contien el item de la RecyclerView inflado
     */
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemRvReportBinding.bind(view)

        fun bind(report: Report){
            binding.tvCategory.text = report.category
            binding.tvDatetime.text = report.dateTime
            binding.tvDescription.text = report.description
        }
    }

    /**
     * Método encargado de ir llenando los ViewHolder de la RecyclerView que van
     * apareciendo en la pantalla a medida que se hace scroll
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = reportList?.get(position)
        holder.bind(item!!)
    }

    /**
     * Devuelve el tamaño de la fuente de datos.
     * @return Entero que contiene la cantidad de elementos de la lista que se muestra
     * en el recycler view
     */
    override fun getItemCount(): Int {
        return reportList!!.size
    }
}