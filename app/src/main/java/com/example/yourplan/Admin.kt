package com.example.yourplan

class Admin(
    nombre: String,
    email: String,
    password: String,
    val permisos: Int,
) : Persona(nombre, email, password) {

    override fun toString(): String {
        return super.toString() + " - Permisos: " + this.permisos
    }

}