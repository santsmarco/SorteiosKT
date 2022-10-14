package com.java.sorteioskt.ui.SorteiosUi

import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.java.sorteioskt.adapter.AdapterSorteiosAbertos
import com.java.sorteioskt.anim.MyBounce
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
    private var idSorteio: String = ""
    private var sorteio = Sorteios()

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
                sorteio = listaSorteios.get(position)
                idSorteio = sorteio.idSorteio
                deletarSorteio(idSorteio)
            }
        })
    }

    private fun deletarSorteio(idSorteio: String) {
        var alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle("Remover")
        alertDialog.setMessage("Deseja remover esse sorteio?")
        alertDialog.setPositiveButton("SIM", DialogInterface.OnClickListener { dialogInterface, i ->
            MyBounce.vibrar(alertDialog.getContext())
            if(verificaInternet()) {
                val uuid = FirebaseAuth.getInstance().currentUser!!.uid
                sorteioRef = firebaseDatabaseReference.database.reference.child("sorteiosAbertos")
                    .child(uuid)
                sorteioRef!!.child(idSorteio).removeValue()
                listaSorteios.clear()
                adapterSorteiosAbertos?.notifyDataSetChanged()
            }else{
                Toast.makeText(this, "Sem conexão", Toast.LENGTH_SHORT).show()
            }
            }).setNegativeButton("NÃO", DialogInterface.OnClickListener { dialogInterface, i ->
            MyBounce.vibrar(alertDialog.getContext())
        })
        alertDialog.create()
        alertDialog.show()


    }

    private fun abrirAddParticipantes(position: Int){
        var sorteio = Sorteios()
        sorteio = listaSorteios.get(position)
        val intent = Intent(this, DetalhesSorteioActivity::class.java)
        intent.putExtra("objetoLista",listaSorteios)
        intent.putExtra("posicao", position)
        intent.putExtra("tipoSorteio", sorteio.tipo)
        startActivity(intent)
    }

    private fun recuperarSorteios(){

        if(verificaInternet()) {
            listaSorteios.clear()
            val uuid = FirebaseAuth.getInstance().currentUser!!.uid
            sorteioRef = firebaseDatabaseReference.child("sorteiosAbertos").child(uuid)
            valueEventListenerSorteios =
                sorteioRef?.addValueEventListener(object : ValueEventListener {
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
        }else{
            Toast.makeText(this, "Sem conexão", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verificaInternet(): Boolean {
        val conexao = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = conexao.activeNetworkInfo
        return info != null && info.isConnected
    }

    override fun onStart() {
        super.onStart()
        recuperarSorteios()
    }
}