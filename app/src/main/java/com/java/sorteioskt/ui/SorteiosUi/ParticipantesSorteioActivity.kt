package com.java.sorteioskt.ui.SorteiosUi

import android.content.DialogInterface
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.java.sorteioskt.adapter.AdapterParticipantes
import com.java.sorteioskt.anim.MyBounce
import com.java.sorteioskt.databinding.ActivityParticipantesBinding
import com.java.sorteioskt.model.Participantes
import com.java.sorteioskt.model.Sorteios

class ParticipantesSorteioActivity : AppCompatActivity() {

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
    private var idParticipante:String = ""
    private var participante = Participantes()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParticipantesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recuperarExtras()
        title = "Participantes sorteio"
        firebaseRef = FirebaseDatabase.getInstance().reference

        recyclerParticipantes = binding.recyclerParticipantes
        adapterParticipantes = AdapterParticipantes(listaParticipantes, this)
        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerParticipantes?.layoutManager = layoutManager
        recyclerParticipantes?.setHasFixedSize(true)
        recyclerParticipantes?.adapter = adapterParticipantes

        adapterParticipantes?.setOnItemClickListener(object : AdapterParticipantes.AdapterParticipantesListener{
            override fun onClickCurto(position: Int) {
                //TODO: CASO QUEIRA ABRIR OS DADOS DO PARTICIPANTE EM TELA GRANDE (FUTURAS IMPLEMENTAÇÕES)
            }

            override fun onClickLongo(position: Int) {
                participante = listaParticipantes.get(position)
                idParticipante = participante.idParticipante
               deletarParticipante(idParticipante)
            }
        })
    }

    private fun deletarParticipante(idParticipante: String) {
        var alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle("Remover")
        alertDialog.setMessage("Deseja remover esse participante?")
        alertDialog.setPositiveButton("SIM", DialogInterface.OnClickListener { dialogInterface, i ->
            MyBounce.vibrar(alertDialog.getContext())
            if(verificaInternet()) {
                val uuid = FirebaseAuth.getInstance().currentUser!!.uid
                participantesRef =
                    firebaseRef?.database?.reference?.child("sorteioParticipantes")?.child(uuid)
                        ?.child(idSorteio)
                participantesRef!!.child(idParticipante).removeValue()
                listaParticipantes.clear()
                adapterParticipantes?.notifyDataSetChanged()
            }else{
                Toast.makeText(this, "Sem conexão", Toast.LENGTH_SHORT).show()
            }
        }).setNegativeButton("NÃO", DialogInterface.OnClickListener { dialogInterface, i ->
            MyBounce.vibrar(alertDialog.getContext())
        })
        alertDialog.create()
        alertDialog.show()
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
       
       if(verificaInternet()) {
           listaParticipantes.clear()
           val uuid = FirebaseAuth.getInstance().currentUser!!.uid
           participantesRef =
               firebaseRef?.child("sorteioParticipantes")?.child(uuid)?.child(idSorteio)

           valueEventListenerParticipantes =
               participantesRef?.addValueEventListener(object : ValueEventListener {
                   override fun onDataChange(snapshot: DataSnapshot) {
                       for (dados in snapshot.children) {
                           val participantes: Participantes? =
                               dados.getValue(Participantes::class.java)
                           if (participantes != null) {
                               listaParticipantes.add(participantes)

                           }
                       }

                       adapterParticipantes?.notifyDataSetChanged()
                   }

                   override fun onCancelled(error: DatabaseError) {
                   }
               })
       }else{
           Toast.makeText(this, "Sem conexão", Toast.LENGTH_SHORT).show()
       }
    }

    private fun verificaInternet(): Boolean {
        val conexao = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = conexao.activeNetworkInfo
        return info != null && info.isConnected
    }
}