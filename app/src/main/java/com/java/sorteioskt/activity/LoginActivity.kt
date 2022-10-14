package com.java.sorteioskt.activity

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.java.sorteioskt.R
import com.java.sorteioskt.anim.MyBounce
import com.java.sorteioskt.databinding.ActivityLoginBinding
import com.java.sorteioskt.model.Usuario

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var validacao: String =""
    private var usuario = Usuario()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonEntrarLogin.setOnClickListener(this)
        binding.textViewCadastro.setOnClickListener(this)
        binding.textViewRecuperarSenha.setOnClickListener(this)
        title = "Entre ou cadastre-se"
        firebaseAuth = FirebaseAuth.getInstance()
        verificaUsuarioLogado()
    }

    private fun verificaUsuarioLogado() {
       if(!(firebaseAuth.uid == null)){
           abrirMainActivity()
       }
    }

    override fun onClick(view: View?) {
        if(view?.id == R.id.buttonEntrarLogin){
            MyBounce.animationBounce(view,this)
            MyBounce.vibrar(this)
            verificarPreenchimento()
        }else if(view?.id == R.id.textViewCadastro){
            MyBounce.animationBounce(view,this)
            MyBounce.vibrar(this)
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }else if(view?.id == R.id.textViewRecuperarSenha){
            MyBounce.animationBounce(view,this)
            MyBounce.vibrar(this)
            recuperarSenha()
        }
    }

    private fun recuperarSenha() {
        var email: String = binding.editTexttEmailLogin.text.toString()
        if(!email.isEmpty()){
            if(verificaInternet()){
                enviarEmailRedefinicao(email)
            }else{ Toast.makeText(this, R.string.info_sem_conexao, Toast.LENGTH_SHORT).show() }
        }else{ Toast.makeText(this, R.string.info_email, Toast.LENGTH_SHORT).show() }
    }

    private fun enviarEmailRedefinicao(email:String) {
        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener {
            Toast.makeText(this, "Email enviado para: $email", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this, "Erro ao enviar o email", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verificaInternet(): Boolean {
        val conexao = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = conexao.activeNetworkInfo
        return info != null && info.isConnected
    }

    private fun verificarPreenchimento() {
        val email= binding.editTexttEmailLogin.text.toString()
        val senha= binding.editTextSenhaLogin.text.toString()
        if(!email.isEmpty()){
            if(!senha.isEmpty()){
               usuario.email = email
               usuario.senha = senha
                logar()
            }else{ Toast.makeText(this, R.string.info_senha, Toast.LENGTH_SHORT).show() }
        }else{ Toast.makeText(this, R.string.info_email, Toast.LENGTH_SHORT).show() }
    }

    private fun logar() {
        firebaseAuth.signInWithEmailAndPassword(usuario.email, usuario.senha).addOnCompleteListener{
            if(it.isSuccessful){
                abrirMainActivity()
                Toast.makeText(this, R.string.sucesso_no_login, Toast.LENGTH_SHORT).show()
            }else {
                try {
                    throw it.exception!!
                } catch (e: FirebaseAuthInvalidUserException) {
                    validacao = "Usuário não está cadastrado!"
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    validacao = "Email e senha não correspondem a um usuário."
                } catch (e: Exception) {
                    validacao = "Erro ao logar usuário: " + e.message
                    e.printStackTrace()
                }
                Toast.makeText(this, validacao, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun abrirMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
