package com.java.sorteioskt.model

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Exclude
import com.google.firebase.database.FirebaseDatabase
import java.io.Serializable

class Sorteios: Serializable {

    var idUsuarioSorteio: String = ""
    var idSorteio: String = ""
    var tipo: String = "" //dinheiro, palavras
    var qtParticipantes: String = ""
    var dataInicio: String = ""
    var dataFim: String = ""
    var premio: String = ""


    fun salvarSorteioDatabase(){
        val firebaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        val usuarioRef: DatabaseReference = firebaseRef.child("sorteiosAbertos").child(idUsuarioSorteio).child(idSorteio)
        usuarioRef.setValue(this)
    }
}