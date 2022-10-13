package com.java.sorteioskt.model

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Exclude
import com.google.firebase.database.FirebaseDatabase

class Usuario () {

    var id: String = ""
    var nome: String = ""
    var rg: String = ""
    var email: String = ""
    @get:Exclude
    var senha: String = ""

    fun salvarDatabase(){
        val firebaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        val usuarioRef: DatabaseReference = firebaseRef.child("usuarios").child(id)
        usuarioRef.setValue(this)
    }
}