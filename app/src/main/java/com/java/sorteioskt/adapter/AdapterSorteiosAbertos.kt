package com.java.sorteioskt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.java.sorteioskt.R
import com.java.sorteioskt.anim.MyBounce
import com.java.sorteioskt.model.Sorteios
import java.util.*
import kotlin.collections.ArrayList

class AdapterSorteiosAbertos(listaSorteios: List<Sorteios>, var context: Context) : RecyclerView.Adapter<AdapterSorteiosAbertos.MyViewHolder>(), Filterable {

    var listaSorteios: List<Sorteios>
    var listaSorteiosFiltros: List<Sorteios>
    private lateinit var mlistener: AdapterSorteiosListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLista: View = LayoutInflater.from(parent.context).inflate(R.layout.adapter_sorteios_salvos, parent, false)
        return MyViewHolder(itemLista,mlistener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val sorteios: Sorteios = listaSorteiosFiltros.get(position)
        holder.tipo.setText(sorteios.tipo)
        holder.dataInicio.setText(sorteios.dataInicio)
        holder.dataFim.setText(sorteios.dataFim)

        val idSorteios: String = sorteios.idSorteio
        val finalId: String = idSorteios.substring(idSorteios.length - 5)
        holder.cod.text = finalId

        if (sorteios.tipo.equals("Dinheiro")) {
            holder.fundoCor.setBackgroundResource(R.drawable.border_custom_degrade_dinheiro)
        }else if(sorteios.tipo.equals("Palavras")){
            holder.fundoCor.setBackgroundResource(R.drawable.border_custom_degrade_palavras)
        }else if(sorteios.tipo.equals("Músicas")){
            holder.fundoCor.setBackgroundResource(R.drawable.border_custom_degrade_musicas)
        }else if(sorteios.tipo.equals("Vídeos")){
            holder.fundoCor.setBackgroundResource(R.drawable.border_custom_degrade_videos)
        }
    }

    override fun getItemCount(): Int {
        return listaSorteiosFiltros.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(textoPesuisado: CharSequence): FilterResults {
                val textoTipoSorteio: String = textoPesuisado.toString()
                if (textoTipoSorteio.isEmpty()) {
                    listaSorteiosFiltros = listaSorteios
                } else {
                    val listaFiltrada: ArrayList<Sorteios> = ArrayList<Sorteios>()
                    for (sorteios: Sorteios in listaSorteios) {
                        if (sorteios.tipo.toLowerCase().contains(textoTipoSorteio.lowercase(Locale.getDefault()))) {
                            listaFiltrada.add(sorteios)
                        }
                    }
                    listaSorteiosFiltros = listaFiltrada
                }
                val filterResults: FilterResults = FilterResults()
                filterResults.values = listaSorteiosFiltros
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                listaSorteiosFiltros = filterResults.values as ArrayList<Sorteios>
                notifyDataSetChanged()
            }
        }
    }

    val responsavelFilter: Filter
        get() = object : Filter() {
            override fun performFiltering(textoPesuisado: CharSequence): FilterResults {
                val textoIdSorteio: String = textoPesuisado.toString()
                if (textoIdSorteio.isEmpty()) {
                    listaSorteiosFiltros = listaSorteios
                } else {
                    val listaFiltrada: ArrayList<Sorteios> = ArrayList<Sorteios>()
                    for (sorteios: Sorteios in listaSorteios) {
                        if (sorteios.idSorteio.toLowerCase().contains(textoIdSorteio.lowercase(Locale.getDefault()))) {
                            listaFiltrada.add(sorteios)
                        }
                    }
                    listaSorteiosFiltros = listaFiltrada
                }
                val filterResults: FilterResults = FilterResults()
                filterResults.values = listaSorteiosFiltros
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                listaSorteiosFiltros = filterResults.values as ArrayList<Sorteios>
                notifyDataSetChanged()
            }
        }

    val statusFilter: Filter
        get() {
            return object : Filter() {
                override fun performFiltering(textoPesuisado: CharSequence): FilterResults {
                    val textoDataInicio: String = textoPesuisado.toString()
                    if (textoDataInicio.isEmpty()) {
                        listaSorteiosFiltros = listaSorteios
                    } else {
                        val listaFiltrada: ArrayList<Sorteios> = ArrayList<Sorteios>()
                        for (sorteios: Sorteios in listaSorteios) {
                            if (sorteios.dataInicio.toLowerCase().contains(textoDataInicio.lowercase(Locale.getDefault()))) {
                                listaFiltrada.add(sorteios)
                            }
                        }
                        listaSorteiosFiltros = listaFiltrada
                    }
                    val filterResults: FilterResults = FilterResults()
                    filterResults.values = listaSorteiosFiltros
                    return filterResults
                }

                override fun publishResults(
                    charSequence: CharSequence,
                    filterResults: FilterResults
                ) {
                    listaSorteiosFiltros = filterResults.values as ArrayList<Sorteios>
                    notifyDataSetChanged()
                }
            }
        }

    val dataFilter: Filter
        get() {
            return object : Filter() {
                override fun performFiltering(textoPesuisado: CharSequence): FilterResults {
                    val textoDataFim: String = textoPesuisado.toString()
                    if (textoDataFim.isEmpty()) {
                        listaSorteiosFiltros = listaSorteios
                    } else {
                        val listaFiltrada: ArrayList<Sorteios> = ArrayList<Sorteios>()
                        for (sorteios: Sorteios in listaSorteios) {
                            if (sorteios.dataFim.toLowerCase().contains(textoDataFim.lowercase(Locale.getDefault()))) {
                                listaFiltrada.add(sorteios)
                            }
                        }
                        listaSorteiosFiltros = listaFiltrada
                    }
                    val filterResults: FilterResults = FilterResults()
                    filterResults.values = listaSorteiosFiltros
                    return filterResults
                }

                override fun publishResults(
                    charSequence: CharSequence,
                    filterResults: FilterResults
                ) {
                    listaSorteiosFiltros = filterResults.values as ArrayList<Sorteios>
                    notifyDataSetChanged()
                }
            }
        }

    interface AdapterSorteiosListener {
        fun onClickCurto(position: Int)
        fun onClickLongo(position: Int)
    }

    fun setOnItemClickListener(listener: AdapterSorteiosListener){
        mlistener = listener
    }

    inner class MyViewHolder(itemView: View, listener: AdapterSorteiosListener) : RecyclerView.ViewHolder(itemView) {
        var tipo: TextView
        var dataInicio: TextView
        var dataFim: TextView
        var cod: TextView
        var fundoCor: TextView

        init {
            tipo = itemView.findViewById(R.id.txtTipoSorteio)
            dataInicio = itemView.findViewById(R.id.txtDataInicio)
            dataFim = itemView.findViewById(R.id.txtDataFim)
            cod = itemView.findViewById(R.id.txtCodSorteio)
            fundoCor = itemView.findViewById(R.id.textFundoCorSorteios)

            itemView.setOnClickListener {
                MyBounce.animationBounce(it,context)
                MyBounce.vibrar(context)
                listener.onClickCurto(adapterPosition)
            }

            itemView.setOnLongClickListener{
                MyBounce.animationBounce(it,context)
                MyBounce.vibrar(context)
                listener.onClickLongo(adapterPosition)
                true
            }
        }
    }

    init {
        this.listaSorteios = listaSorteios
        listaSorteiosFiltros = listaSorteios
    }

}

