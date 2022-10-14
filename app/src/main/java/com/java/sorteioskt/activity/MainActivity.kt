package com.java.sorteioskt.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.java.sorteioskt.R
import com.java.sorteioskt.anim.MyBounce
import com.java.sorteioskt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var mWidth: Int? = null
    private var mHeight: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Sorteios"
        animInit()
        initialize()
    }

    fun abrirLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.menu_main_sair){
            firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signOut()
            abrirLogin()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun animInit() {
        val metrics = DisplayMetrics()
        val result = windowManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //pega tamanho da tela
            mWidth = result.currentWindowMetrics.bounds.width()
            mHeight = result.currentWindowMetrics.bounds.height()
        }else{
            result.defaultDisplay.getMetrics(metrics)
            mWidth = metrics.widthPixels
            mHeight = metrics.heightPixels
        }

        val imageLogo = binding.imageLogo
        val containerSorteio = binding.containerSorteio

        containerSorteio.y = (mHeight!! * 0.56).toFloat()

        imageLogo.y =(mHeight!! * 0.25).toFloat()
        imageLogo.scaleX = (mHeight!! * 0.0006).toFloat()
        imageLogo.scaleY = (mHeight!! * 0.0006).toFloat()

        Handler(Looper.getMainLooper()).postDelayed({
            containerSorteio.animate().duration = 1000
            containerSorteio.animate().yBy(-(mHeight!! * 0.56).toFloat())
            imageLogo.animate().duration = 1000
            imageLogo.animate().yBy(-(mHeight!! * 0.25).toFloat())
            imageLogo.animate().scaleX((mHeight!! * 0.0006).toFloat())
            imageLogo.animate().scaleY((mHeight!! * 0.0006).toFloat())
        }, 1000)
    }

    private fun animAscendente(){
        binding.imageLogo.setOnClickListener(this)
        val imageLogo = binding.imageLogo
        val containerSorteio = binding.containerSorteio

        containerSorteio.y = (mHeight!! * 0.56).toFloat()

        imageLogo.y = (mHeight!! * 0.20).toFloat()
        imageLogo.scaleX = (mHeight!! * 0.0006).toFloat()
        imageLogo.scaleY = (mHeight!! * 0.0006).toFloat()

        containerSorteio.animate().duration = 1000
        containerSorteio.animate().yBy(-(mHeight!! * 0.20).toFloat())
        imageLogo.animate().duration = 1000
        imageLogo.animate().yBy(-(mHeight!! * 0.15).toFloat())
        imageLogo.animate().scaleX((mHeight!! * 0.0006).toFloat())
        imageLogo.animate().scaleY((mHeight!! * 0.0006).toFloat())
    }

    private fun animDescendente(){
        binding.imageLogo.setOnClickListener(this)
        val imageLogo = binding.imageLogo
        val containerSorteio = binding.containerSorteio

        containerSorteio.y = (mHeight!! * 0.56).toFloat()

        imageLogo.y = -(mHeight!! * -0.25).toFloat()
        imageLogo.scaleX = (mHeight!! * 0.0000).toFloat()
        imageLogo.scaleY = (mHeight!! * 0.0000).toFloat()

        containerSorteio.animate().duration = 1000
        containerSorteio.animate().yBy((mHeight!! * 0.56).toFloat())
        imageLogo.animate().duration = 1000
        imageLogo.animate().yBy((mHeight!! * 0.25).toFloat())
        imageLogo.animate().scaleX((mHeight!! * 0.0006).toFloat())
        imageLogo.animate().scaleY((mHeight!! * 0.0006).toFloat())
    }

    override fun onClick(view: View?) {
        if(view?.id == R.id.card_dinheiro){

            MyBounce.animationBounce(view,this)
            MyBounce.vibrar(this)
            var intent = Intent(this@MainActivity, CriarSorteioActivity::class.java)
            intent.putExtra("tipoSorteio", "Dinheiro")
            startActivity(intent)

        }else if(view?.id == R.id.card_palavras){

            MyBounce.animationBounce(view,this)
            MyBounce.vibrar(this)
            var intent = Intent(this@MainActivity, CriarSorteioActivity::class.java)
            intent.putExtra("tipoSorteio", "Palavras")
            startActivity(intent)

        }else if(view?.id == R.id.card_musicas){

            MyBounce.animationBounce(view,this)
            MyBounce.vibrar(this)
            var intent = Intent(this@MainActivity, CriarSorteioActivity::class.java)
            intent.putExtra("tipoSorteio", "Músicas")
            startActivity(intent)

        }else if(view?.id == R.id.card_videos){

            MyBounce.animationBounce(view,this)
            MyBounce.vibrar(this)
            var intent = Intent(this@MainActivity, CriarSorteioActivity::class.java)
            intent.putExtra("tipoSorteio", "Vídeos")
            startActivity(intent)

        }else if (view?.id == R.id.image_logo){

            MyBounce.animationBounce(view,this)
            MyBounce.vibrar(this)
            var intent = Intent(this@MainActivity, CriarSorteioActivity::class.java)
            intent.putExtra("tipoSorteio", "Dinheiro")
            startActivity(intent)
        }
    }

    private fun initialize(){
        binding.cardDinheiro.setOnClickListener(this)
        binding.cardPalavras.setOnClickListener(this)
        binding.cardMusicas.setOnClickListener(this)
        binding.cardVideos.setOnClickListener(this)
    }
}