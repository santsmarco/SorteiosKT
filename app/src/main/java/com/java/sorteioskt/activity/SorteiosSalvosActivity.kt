package com.java.sorteioskt.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.java.sorteioskt.adapter.AdapterSorteiosAbertos
import com.java.sorteioskt.databinding.ActivitySorteiosSalvosBinding
import com.java.sorteioskt.model.Sorteios

class SorteiosSalvosActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySorteiosSalvosBinding
    private var adapterSorteiosAbertos: AdapterSorteiosAbertos? = null
    private var listaSorteios: ArrayList<Sorteios> = ArrayList<Sorteios>()
    private var recyclerSorteios: RecyclerView? = null
    private var sorteioRef: DatabaseReference? = null
    private lateinit var firebaseDatabaseReference: DatabaseReference
    private var valueEventListenerSorteios: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySorteiosSalvosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Sorteios Salvos"
        firebaseDatabaseReference = FirebaseDatabase.getInstance().reference

        recyclerSorteios = binding.recyclerSorteiosAbertos

        adapterSorteiosAbertos = AdapterSorteiosAbertos(listaSorteios, this)
        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerSorteios?.layoutManager = layoutManager
        recyclerSorteios?.setHasFixedSize(true)
        recyclerSorteios?.adapter = adapterSorteiosAbertos

        adapterSorteiosAbertos?.setOnItemClickListener(object :AdapterSorteiosAbertos.AdapterSorteiosListener{
            override fun onClickCurto(position: Int) {
                abrirAddParticipantes(position)
            }

            override fun onClickLongo(position: Int) {

            }
        })
    }

    private fun abrirAddParticipantes(position: Int){
        var sorteio = Sorteios()
        sorteio = listaSorteios.get(position)
        val intent = Intent(this,DetalhesSorteioActivity::class.java)
        intent.putExtra("objetoLista",listaSorteios)
        intent.putExtra("posicao", position)
        intent.putExtra("tipoSorteio", sorteio.tipo)
        startActivity(intent)
    }

    private fun recuperarSorteios(){
        listaSorteios.clear()

        val uuid = FirebaseAuth.getInstance().currentUser!!.uid

        sorteioRef = firebaseDatabaseReference.child("sorteiosAbertos").child(uuid)

        valueEventListenerSorteios = sorteioRef?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dados in snapshot.children) {
                        val sorteios: Sorteios? = dados.getValue(Sorteios::class.java)
                        if (sorteios != null) {
                            listaSorteios.add(sorteios)
                        }
                    }
                    adapterSorteiosAbertos?.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    override fun onStart() {
        super.onStart()
        recuperarSorteios()
    }
}