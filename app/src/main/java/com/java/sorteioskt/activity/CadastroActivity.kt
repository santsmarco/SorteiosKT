package com.java.sorteioskt.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.*
import com.java.sorteioskt.R
import com.java.sorteioskt.anim.MyBounce
import com.java.sorteioskt.databinding.ActivityCadastroBinding
import com.java.sorteioskt.model.Usuario

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var usuario = Usuario()
    private var validacao: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        title = "Cadastre-se"

        binding.buttonCadastrar.setOnClickListener {
            MyBounce.animationBounce(it,this)
            MyBounce.vibrar(this)
            validarCampos()
        }
    }

    private fun validarCampos() {
        val nome = binding.editTextNomeCadastro.text.toString()
        val rg = binding.editTextRgCadastro.text.toString()
        val email = binding.editTextEmailCadastro.text.toString()
        val senha = binding.editTextSenhaCadastro.text.toString()
        val repetirSenha = binding.editTextRepetirSenhaCadastro.text.toString()

        if (!nome.isEmpty()) {
            if (!rg.isEmpty()) {
                if (!email.isEmpty()) {
                    if (!senha.isEmpty()) {
                        if (!repetirSenha.isEmpty()) {
                            if (!(repetirSenha != senha)) {

                                cadastrarUsuario(nome, rg, email, senha)

                            } else { Toast.makeText(this,
                                R.string.info_senha_repetir_comparacao, Toast.LENGTH_SHORT).show() }
                        } else { Toast.makeText(this, R.string.info_senha_repetir, Toast.LENGTH_SHORT).show() }
                    } else { Toast.makeText(this, R.string.info_senha, Toast.LENGTH_SHORT).show() }
                } else { Toast.makeText(this, R.string.info_email, Toast.LENGTH_SHORT).show() }
            } else { Toast.makeText(this, R.string.info_rg, Toast.LENGTH_SHORT).show() }
        } else { Toast.makeText(this, R.string.info_nome, Toast.LENGTH_SHORT).show() }
    }

    private fun cadastrarUsuario(nome:String,rg:String,email:String,senha:String) {

        firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener {
            if (it.isSuccessful) {

                usuario.id = firebaseAuth.uid.toString()
                usuario.nome = nome
                usuario.rg = rg
                usuario.email = email
                usuario.senha = senha
                usuario.salvarDatabase()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {

                try {
                    throw it.exception!!
                } catch (e: FirebaseAuthWeakPasswordException) {
                    validacao = "Digite uma senha mais forte.";
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    validacao = "Por favor, digite um email válido.";
                } catch (e: FirebaseAuthUserCollisionException) {
                    validacao = "Este usuário já foi cadastrado.";
                } catch (e: Exception) {
                    validacao = " Erro ao cadastrar usuário: " + e.message;
                    e.printStackTrace();
                }
                Toast.makeText(this, validacao, Toast.LENGTH_SHORT).show()
            }
        }
    }



}