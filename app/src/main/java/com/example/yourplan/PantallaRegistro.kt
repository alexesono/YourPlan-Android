package com.example.yourplan

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class PantallaRegistro : AppCompatActivity(), SensorEventListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantalla_registro)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        auth = FirebaseAuth.getInstance()
        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val passwordEditText = findViewById<EditText>(R.id.loginContraseña)
        val nameEditText = findViewById<EditText>(R.id.editTextNombre)
        val confirmPasswordEditText = findViewById<EditText>(R.id.editTextRepetirContraseña)
        val apellidosEditText = findViewById<EditText>(R.id.editTextApellidos)
        val registro = findViewById<Button>(R.id.botonCrearCuenta)

        registro.setOnClickListener {

            registro.animate().apply {
                duration = 300
                rotationXBy(-360f)
                alpha(0.3f)
                alpha(1f)
            }

            val nombre = nameEditText.text.toString()
            val apellidos = apellidosEditText.text.toString()
            val email = emailEditText.text.toString()
            val contraseña = passwordEditText.text.toString()
            val repetirContraseña = confirmPasswordEditText.text.toString()

            if (nombre.isEmpty()||apellidos.isEmpty()||email.isEmpty()||contraseña.isEmpty()||repetirContraseña.isEmpty()){
                Toast.makeText(this, "Campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (contraseña!=repetirContraseña){
                Toast.makeText(this, "Contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, contraseña).addOnCompleteListener(this){ task->
               if (task.isSuccessful) {
                   val user = auth.currentUser
                   Toast.makeText(this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show()
                   val PantallaLogin = Intent(this, PantallaLogin::class.java)
                   startActivity(PantallaLogin)
                   overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
               }else {
                   Toast.makeText(this, "Error al crear el usuario", Toast.LENGTH_SHORT).show()
               }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        accelerometer?.also { accelerometer ->
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val acceleration = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()

            if (acceleration > 12) {
                Toast.makeText(this, "No agites el movil", Toast.LENGTH_SHORT).show()
            }
        }
    }
}