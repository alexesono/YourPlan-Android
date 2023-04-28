package com.example.yourplan

open class Persona(val nombre: String, val email: String, val password: String) {

    //Constructores


    override fun toString(): String {
        return this.nombre + this.email
    }

    fun guardarFirebase(): Boolean {
        return false
    }

    fun comprobarPassword(pass: String): Boolean {
        return this.password.equals(pass)
    }

}