package com.java.sorteioskt.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.java.sorteioskt.R
import com.java.sorteioskt.anim.MyBounce
import com.java.sorteioskt.databinding.ActivityCriarSorteioBinding
import com.java.sorteioskt.model.Sorteios
import com.java.sorteioskt.util.MaskEditUtil

class CriarSorteioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCriarSorteioBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: DatabaseReference
    var sorteios = Sorteios()
    var tipoSorteio: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCriarSorteioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance().reference
        binding.editTextDataInicial.addTextChangedListener(MaskEditUtil.mask(binding.editTextDataInicial, MaskEditUtil.FORMAT_DATE))
        binding.editTextDataFinal.addTextChangedListener(MaskEditUtil.mask(binding.editTextDataFinal, MaskEditUtil.FORMAT_DATE))
        tipoSorteio = intent.getStringExtra("tipoSorteio").toString()
        binding.txtCriarTipoSorteio.setText(tipoSorteio)

        if(tipoSorteio.equals("Dinheiro")){
            binding.containerBackgroundCriarSorteio.setBackgroundResource(R.color.cor_dinheiro)
        }else if(tipoSorteio.equals("Palavras")){
            binding.containerBackgroundCriarSorteio.setBackgroundResource(R.color.cor_palavras)
        }else if(tipoSorteio.equals("Músicas")){
            binding.containerBackgroundCriarSorteio.setBackgroundResource(R.color.cor_musicas)
        }else if(tipoSorteio.equals("Vídeos")){
            binding.containerBackgroundCriarSorteio.setBackgroundResource(R.color.cor_videos)
        }


        binding.imageViewSorteiosDinheiro.setOnClickListener{
            MyBounce.animationBounce(it, this)
            MyBounce.vibrar(this)
            startActivity(Intent(this@CriarSorteioActivity,MainActivity::class.java))
            finish()
        }

        binding.textViewVoltarDinheiro.setOnClickListener{
            MyBounce.animationBounce(it, this)
            MyBounce.vibrar(this)
            startActivity(Intent(this@CriarSorteioActivity,MainActivity::class.java))
            finish()
        }

        binding.buttonSalvarSorteioDinheiro.setOnClickListener {
            MyBounce.animationBounce(it, this)
            MyBounce.vibrar(this)
            verificarPreenchimento()
        }

        binding.textViewSorteiosRecuperados.setOnClickListener{
            MyBounce.animationBounce(it, this)
            MyBounce.vibrar(this)
            startActivity(Intent(this@CriarSorteioActivity,SorteiosSalvosActivity::class.java))

        }
    }

    private fun verificarPreenchimento() {

        var qtParticipantes = binding.editTextQtParticipantes.text.toString()
        var dataInicio = binding.editTextDataInicial.text.toString()
        var dataFim = binding.editTextDataFinal.text.toString()
        var valorPremio =  binding.editTextPremio.text.toString()

        if (!qtParticipantes.isEmpty()){
            if(!dataInicio.isEmpty()){
                if (!dataFim.isEmpty()){
                    if(!valorPremio.isEmpty()){
                        salvarSorteio(qtParticipantes,dataInicio,dataFim,valorPremio)
                    }else{ Toast.makeText(this, "Valor vazio", Toast.LENGTH_SHORT).show() }
                }else{ Toast.makeText(this, "Data fim", Toast.LENGTH_SHORT).show() }
            }else{ Toast.makeText(this, "Data início vazio", Toast.LENGTH_SHORT).show() }
        }else{ Toast.makeText(this, "Quantidade participantes vazio", Toast.LENGTH_SHORT).show() }
    }

    private fun salvarSorteio(qtParticipantes: String, dataInicio: String, dataFim: String, valorPremio: String) {

        var chaveAleatoria:String = firebaseDatabase.push().key.toString()

        sorteios.idUsuarioSorteio = firebaseAuth.uid.toString()
        sorteios.idSorteio = chaveAleatoria
        sorteios.tipo = tipoSorteio
        sorteios.qtParticipantes = qtParticipantes
        sorteios.dataInicio = dataInicio
        sorteios.dataFim = dataFim
        sorteios.premio = valorPremio
        sorteios.salvarSorteioDatabase()

        startActivity(Intent(this@CriarSorteioActivity, SorteiosSalvosActivity::class.java))
        finish()
    }


}