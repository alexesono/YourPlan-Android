package com.example.yourplan

class User(
    nombre: String,
    email: String,
    password: String,
    val telf: String,
) : Persona(nombre, email, password) {

}