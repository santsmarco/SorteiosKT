package com.java.sorteioskt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.java.sorteioskt.R
import com.java.sorteioskt.anim.MyBounce
import com.java.sorteioskt.model.Participantes
import java.util.*
import kotlin.collections.ArrayList

class AdapterParticipantes(listaParticipantes: List<Participantes>, var context: Context) : RecyclerView.Adapter<AdapterParticipantes.MyViewHolder>(), Filterable {

    var listaParticipantes: List<Participantes>
    var listaParticipantesFiltros: List<Participantes>
    private lateinit var mlistener: AdapterParticipantesListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLista: View = LayoutInflater.from(parent.context).inflate(R.layout.adapter_participantes, parent, false)
        return MyViewHolder(itemLista,mlistener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val participantes: Participantes = listaParticipantesFiltros.get(position)
        holder.nomeParticipante.setText(participantes.nome)
        holder.cpfParticipante.setText(participantes.cpf)
        holder.enderecoParticpante.setText(participantes.endereco)
        holder.telParticipante.setText(participantes.telefone)

        val idParticipantes: String = participantes.idSorteio
        val finalId: String = idParticipantes.substring(idParticipantes.length - 5)
        holder.idParticipante.text = finalId
    }

    override fun getItemCount(): Int {
        return listaParticipantesFiltros.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(textoPesuisado: CharSequence): FilterResults {
                val txtNome: String = textoPesuisado.toString()
                if (txtNome.isEmpty()) {
                    listaParticipantesFiltros = listaParticipantes
                } else {
                    val listaFiltrada: ArrayList<Participantes> = ArrayList<Participantes>()
                    for (participantes: Participantes in listaParticipantes) {
                        if (participantes.nome.toLowerCase().contains(txtNome.lowercase(Locale.getDefault()))) {
                            listaFiltrada.add(participantes)
                        }
                    }
                    listaParticipantesFiltros = listaFiltrada
                }
                val filterResults: FilterResults = FilterResults()
                filterResults.values = listaParticipantesFiltros
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                listaParticipantesFiltros = filterResults.values as ArrayList<Participantes>
                notifyDataSetChanged()
            }
        }
    }

    val responsavelFilter: Filter
        get() = object : Filter() {
            override fun performFiltering(textoPesuisado: CharSequence): FilterResults {
                val txtCpf: String = textoPesuisado.toString()
                if (txtCpf.isEmpty()) {
                    listaParticipantesFiltros = listaParticipantes
                } else {
                    val listaFiltrada: ArrayList<Participantes> = ArrayList<Participantes>()
                    for (participantes: Participantes in listaParticipantes) {
                        if (participantes.cpf.toLowerCase().contains(txtCpf.lowercase(Locale.getDefault()))) {
                            listaFiltrada.add(participantes)
                        }
                    }
                    listaParticipantesFiltros = listaFiltrada
                }
                val filterResults: FilterResults = FilterResults()
                filterResults.values = listaParticipantesFiltros
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                listaParticipantesFiltros = filterResults.values as ArrayList<Participantes>
                notifyDataSetChanged()
            }
        }


    interface AdapterParticipantesListener {
        fun onClickCurto(position: Int)
        fun onClickLongo(position: Int)
    }

    fun setOnItemClickListener(listener: AdapterParticipantesListener){
        mlistener = listener
    }


    inner class MyViewHolder(itemView: View, listener: AdapterParticipantesListener) : RecyclerView.ViewHolder(itemView) {
        var nomeParticipante: TextView
        var cpfParticipante: TextView
        var enderecoParticpante: TextView
        var telParticipante: TextView
        var idParticipante: TextView
        var fundoCor: TextView

        init {
            nomeParticipante = itemView.findViewById(R.id.txtNomeParticipante)
            cpfParticipante = itemView.findViewById(R.id.txtCpfParticipante)
            enderecoParticpante = itemView.findViewById(R.id.txtEnderecoParticipante)
            telParticipante = itemView.findViewById(R.id.txtTelefoneParticipante)
            idParticipante = itemView.findViewById(R.id.textViewCodigoParticipante)
            fundoCor = itemView.findViewById(R.id.textFundoCorParticipantes)

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
        this.listaParticipantes = listaParticipantes
        listaParticipantesFiltros = listaParticipantes
    }

}

