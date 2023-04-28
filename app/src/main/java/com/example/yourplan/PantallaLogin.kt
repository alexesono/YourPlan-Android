package com.example.yourplan

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PantallaLogin : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantalla_login)

        var loginEmail: EditText = findViewById(R.id.loginEmail)
        var loginPassword: EditText = findViewById(R.id.loginContraseña)
        var loginButton: Button = findViewById(R.id.loginButton)
        var botonCrearCuenta: Button = findViewById(R.id.botonCrearCuenta)
        var checkRecuerdame: CheckBox = findViewById(R.id.checkBox_Recordarme)

        // Recogemos los datos de email y contraseña de la pantalla registro
        val bundle = intent.extras
        val emailRegistro = bundle?.getString("Email")
        val contraseñaRegistro = bundle?.getString("Contraseña")
        val nombreRegistro = bundle?.getString("Nombre")


        // Este boton nos llevara dentro de la app
        loginButton.setOnClickListener {

            loginButton.animate().apply {
                duration = 300
                rotationYBy(-360f)
                alpha(0.3f)
                alpha(1f)
            }

            val auth = Firebase.auth
            val email = loginEmail.text.toString()
            val password = loginPassword.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success
                        Toast.makeText(this, "Email y contraseña correctas", Toast.LENGTH_SHORT).show()
                        val pantallaPrincipal = Intent(this, PantallaPrincipal::class.java)
                        pantallaPrincipal.putExtra("Nombre", nombreRegistro)
                        startActivity(pantallaPrincipal)

                        this.finish()

                        //Esta es la transicion hacia la pantalla principal
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    } else {
                        // Sign in failed
                        Toast.makeText(this, "Email o contraseña incorrectas", Toast.LENGTH_SHORT).show()
                    }
                }
        }


        // Este boton nos lleva a la pantalla de registro
        botonCrearCuenta.setOnClickListener {

            botonCrearCuenta.animate().apply {
                duration = 300
                rotationYBy(-360f)
                alpha(0.3f)
                alpha(1f)
            }

            val PantallaRegistro: Intent = Intent()
            PantallaRegistro.setClassName(this, "com.example.yourplan.PantallaRegistro")
            startActivity(PantallaRegistro)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        //BIOMETRIA
        val botonHuella: Button = findViewById(R.id.botonHuella)
        botonHuella.setOnClickListener {
            // Aquí es donde se manejaría la lógica para acceder con la huella digital
            val biometricManager = BiometricManager.from(this)
            when (biometricManager.canAuthenticate()) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    val promptInfo = BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Acceder a YourPlan con huella digital")
                        .setSubtitle("Coloca tu dedo en el sensor")
                        .setNegativeButtonText("Cancelar")
                        .build()

                    val biometricPrompt =
                        BiometricPrompt(this, object : BiometricPrompt.AuthenticationCallback() {
                            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                                super.onAuthenticationSucceeded(result)
                                val pantallaPrincipal = Intent(this@PantallaLogin, PantallaPrincipal::class.java)
                                startActivity(pantallaPrincipal)
                            }
                        })

                    biometricPrompt.authenticate(promptInfo)
                }
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    // El dispositivo no tiene sensor de huella digital
                }
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    // El sensor de huella digital no está disponible temporalmente
                }
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    // No se ha registrado ninguna huella digital en el dispositivo
                }
            }
        }

    }//Fin del Main

}