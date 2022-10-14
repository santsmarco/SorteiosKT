package com.java.sorteioskt.model

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Exclude
import com.google.firebase.database.FirebaseDatabase
import java.io.Serializable

class Participantes: Serializable {

    var idParticipante: String = ""
    var idSorteio: String = ""
    var idUsuarioSorteio: String = "" //dinheiro, palavras
    var nome: String = ""
    var cpf: String = ""
    var endereco: String = ""
    var telefone: String = ""

    fun salvarParticipanteSorteioDatabase(){
        val firebaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        val usuarioRef: DatabaseReference = firebaseRef.child("sorteioParticipantes")
            .child(idUsuarioSorteio).child(idSorteio).child(idParticipante)
        usuarioRef.setValue(this)
    }
}