package com.java.sorteioskt.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.java.sorteioskt.R
import com.java.sorteioskt.anim.MyBounce
import com.java.sorteioskt.databinding.ActivityDetalhesSorteioBinding
import com.java.sorteioskt.model.Participantes
import com.java.sorteioskt.model.Sorteios

class DetalhesSorteioActivity : AppCompatActivity() {

    private var listaSorteios: ArrayList<Sorteios> = ArrayList<Sorteios>()
    private var sorteio = Sorteios()
    private var positionList:Int = 0
    private var tipoSorteio:String = ""
    private lateinit var binding: ActivityDetalhesSorteioBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fibaseReferece: DatabaseReference
    private var participante = Participantes()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhesSorteioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        fibaseReferece = FirebaseDatabase.getInstance().reference

        mostrarDadosSorteio()
        var cod:String = binding.txtAddCod.text.toString()
        title = "Sorteio $cod"

        binding.buttonAddSalvar.setOnClickListener{
            MyBounce.animationBounce(it,this)
            MyBounce.vibrar(this)
            verificarCampos()
        }

        binding.imageViewDetalhesSorteio.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        }

        binding.switchAddParticipantes.setOnCheckedChangeListener{ switch, isCheked ->
            if(isCheked){
                binding.edtAddNome.visibility = View.VISIBLE
                binding.edtAddCpf.visibility = View.VISIBLE
                binding.edtAddEndereco.visibility = View.VISIBLE
                binding.edtAddTelefone.visibility = View.VISIBLE
                binding.buttonAddSalvar.visibility = View.VISIBLE
            }else{
                binding.edtAddNome.visibility = View.GONE
                binding.edtAddCpf.visibility = View.GONE
                binding.edtAddEndereco.visibility = View.GONE
                binding.edtAddTelefone.visibility = View.GONE
                binding.buttonAddSalvar.visibility = View.GONE
            }
            
        }
    }

    private fun verificarCampos() {
        var chaveAleatoria:String = fibaseReferece.push().key.toString()
        var idParticipante = chaveAleatoria
        var idSorteio = sorteio.idSorteio
        var idUsuarioSorteio = sorteio.idUsuarioSorteio
        var nomeParticipante = binding.edtAddNome.text.toString()
        var cpfParticipante = binding.edtAddCpf.text.toString()
        var enderecoParticipante = binding.edtAddEndereco.text.toString()
        var telefoneParticipante = binding.edtAddTelefone.text.toString()

        if(!nomeParticipante.isEmpty()){
            if(!cpfParticipante.isEmpty()){
                if(!enderecoParticipante.isEmpty()){
                    if(!telefoneParticipante.isEmpty()){

                        salvarDados(idParticipante, idSorteio, idUsuarioSorteio, nomeParticipante, cpfParticipante, enderecoParticipante, telefoneParticipante)

                    }else{ Toast.makeText(this, "Telefone vazio", Toast.LENGTH_SHORT).show() }
                }else{ Toast.makeText(this, "Endereço vazio", Toast.LENGTH_SHORT).show() }
            }else{ Toast.makeText(this, "Cpf vazio", Toast.LENGTH_SHORT).show() }
        }else{ Toast.makeText(this, "Nome vazio", Toast.LENGTH_SHORT).show() }
    }

    private fun salvarDados(idParticipante: String, idSorteio: String, idUsuarioSorteio: String, nomeParticipante: String, cpfParticipante: String, enderecoParticipante: String, telefoneParticipante: String) {

        participante.idParticipante = idParticipante
        participante.idSorteio = idSorteio
        participante.idUsuarioSorteio = idUsuarioSorteio
        participante.nome = nomeParticipante
        participante.cpf = cpfParticipante
        participante.endereco = enderecoParticipante
        participante.telefone = telefoneParticipante
        participante.salvarParticipanteSorteioDatabase()

    }


    private fun mostrarDadosSorteio() {
        //Recuperar array list
        listaSorteios = intent.getSerializableExtra("objetoLista") as ArrayList<Sorteios>
        positionList = intent.getIntExtra("posicao",0)
        tipoSorteio = intent.getStringExtra("tipoSorteio").toString()
        sorteio = listaSorteios[positionList]

        //Método para pegar apenas o final da chave aleatória da ocorrência
        val idSorteios: String = sorteio.idSorteio
        val finalId: String = idSorteios.substring(idSorteios.length - 5)

        binding.txtAddTipoSorteio.setText(sorteio.tipo)
        binding.txtAddCod.setText(finalId)
        binding.txtAddDataInicio.setText(sorteio.dataInicio)
        binding.txtAddDataFim.setText(sorteio.dataFim)

        if(tipoSorteio.equals("Dinheiro")){
            binding.backgroundDetalhesSorteio.setBackgroundResource(R.color.cor_dinheiro)
            binding.buttonAddSortear.setBackgroundResource(R.drawable.border_custom_degrade_dinheiro)
        }else if (tipoSorteio.equals("Palavras")){
            binding.backgroundDetalhesSorteio.setBackgroundResource(R.color.cor_palavras)
            binding.buttonAddSortear.setBackgroundResource(R.drawable.border_custom_degrade_palavras)
        }else if (tipoSorteio.equals("Músicas")){
            binding.backgroundDetalhesSorteio.setBackgroundResource(R.color.cor_musicas)
            binding.buttonAddSortear.setBackgroundResource(R.drawable.border_custom_degrade_musicas)
        }else if (tipoSorteio.equals("Vídeos")){
            binding.backgroundDetalhesSorteio.setBackgroundResource(R.color.cor_videos)
            binding.buttonAddSortear.setBackgroundResource(R.drawable.border_custom_degrade_videos)
        }
    }
}