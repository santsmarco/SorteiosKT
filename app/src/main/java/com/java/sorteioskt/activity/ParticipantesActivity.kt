package com.java.sorteioskt.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.java.sorteioskt.adapter.AdapterParticipantes
import com.java.sorteioskt.databinding.ActivityParticipantesBinding
import com.java.sorteioskt.model.Participantes
import com.java.sorteioskt.model.Sorteios

class ParticipantesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParticipantesBinding
    private var tipoSorteio:String = ""
    private var recyclerParticipantes: RecyclerView? = null
    private var adapterParticipantes: AdapterParticipantes? = null
    private var listaParticipantes: ArrayList<Participantes> = ArrayList<Participantes>()
    private var participantesRef: DatabaseReference? = null
    private var firebaseRef: DatabaseReference? = null
    private var valueEventListenerParticipantes: ValueEventListener? = null
    private var listaSorteios: ArrayList<Sorteios> = ArrayList<Sorteios>()
    private var idSorteio:String = ""
    private var sorteio = Sorteios()
    private var positionList:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParticipantesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recuperarExtras()
        title = "Participante sorteio"
        firebaseRef = FirebaseDatabase.getInstance().reference

        recyclerParticipantes = binding.recyclerParticipantes
        adapterParticipantes = AdapterParticipantes(listaParticipantes, this)
        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerParticipantes?.layoutManager = layoutManager
        recyclerParticipantes?.setHasFixedSize(true)
        recyclerParticipantes?.adapter = adapterParticipantes

        adapterParticipantes?.setOnItemClickListener(object : AdapterParticipantes.AdapterParticipantesListener{
            override fun onClickCurto(position: Int) {
                Toast.makeText(applicationContext, "Curto", Toast.LENGTH_SHORT).show()
            }

            override fun onClickLongo(position: Int) {
                Toast.makeText(applicationContext, "Longo", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun recuperarExtras() {
        tipoSorteio = intent.getStringExtra("tipoSorteio").toString()
        listaSorteios = intent.getSerializableExtra("objetoLista") as ArrayList<Sorteios>
        positionList = intent.getIntExtra("position",0)
        sorteio = listaSorteios[positionList]
        idSorteio = sorteio.idSorteio
    }

    override fun onStart() {
        super.onStart()
        recuperarParticipantes()
    }

    private fun recuperarParticipantes() {
        listaParticipantes.clear()

        val uuid = FirebaseAuth.getInstance().currentUser!!.uid

        participantesRef = firebaseRef?.child("sorteioParticipantes")?.child(uuid)?.child(idSorteio)

        valueEventListenerParticipantes = participantesRef?.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(dados in snapshot.children){
                    val participantes: Participantes? = dados.getValue(Participantes::class.java)
                    if(participantes!=null){
                        listaParticipantes.add(participantes)
                    }
                }

                adapterParticipantes?.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}