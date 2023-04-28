package com.example.yourplan

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat
import android.hardware.SensorEvent
import android.hardware.SensorEventListener


class PantallaPrincipal : AppCompatActivity(), SensorEventListener, GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener {

    private lateinit var mDetector: GestureDetectorCompat
    private lateinit var sensorManager: SensorManager
    private var proximitySensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantalla_principal)

        mDetector = GestureDetectorCompat(this, this)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if (proximitySensor == null) {
            Toast.makeText(this, "No hay sensor de proximidad", Toast.LENGTH_LONG).show()
            finish()
        }

        val bienvenida: TextView = findViewById(R.id.textViewBienvenida)
        val llamar: Button = findViewById(R.id.botonLlamada)
        val mensaje: Button = findViewById(R.id.botonMensaje)
        val maps: Button = findViewById(R.id.botonMaps)
        val phoneNumber: String = "654766358"

        val bundle = intent.extras
        val nombre = bundle?.getString("Nombre")

        bienvenida.setText("Bienvenido a YourPlan, " + nombre.toString())

        llamar.setOnClickListener {
            val intentLlamar = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            startActivity(intentLlamar)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        var isRed = false
        llamar.setOnLongClickListener {
            if (isRed) {
                llamar.setBackgroundResource(R.drawable.btn_redondo) // Cambia el color a gris (color original)
                isRed = false
            } else {
                llamar.setBackgroundResource(R.drawable.btn_redondo1) // Cambia el color a rojo
                isRed = true
            }
            true
        }

        // Te lleva a la app de correo (Ej: para dar una valoracion de nuestra)
        mensaje.setOnClickListener {
            val intentEmail = Intent()
            intentEmail.setAction(Intent.ACTION_SENDTO)
            intentEmail.setData(Uri.parse("mailto:"))
            intentEmail.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf("yourplan@gmail.com")
            )
            intentEmail.putExtra(Intent.EXTRA_SUBJECT, "YourPlan")
            intentEmail.putExtra(Intent.EXTRA_TEXT, "Una app que todo el mundo necesita, pero no lo sabe aun.")
            startActivity(intentEmail)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        var isRed2 = false
        mensaje.setOnLongClickListener {
            if (isRed2) {
                mensaje.setBackgroundResource(R.drawable.btn_redondo) // Cambia el color a gris (color original)
                isRed2 = false
            } else {
                mensaje.setBackgroundResource(R.drawable.btn_redondo1) // Cambia el color a rojo
                isRed2 = true
            }
            true
        }

        //Esto nos abrira google maps con una ubicacion que le hemos puesto
        maps.setOnClickListener {
            val intentMaps = Intent()
            intentMaps.setAction(Intent.ACTION_VIEW)
            intentMaps.data = Uri.parse("geo:0,0?q=Style+Shop")
            startActivity(intentMaps)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        var isRed3 = false
        maps.setOnLongClickListener {
            if (isRed3) {
                maps.setBackgroundResource(R.drawable.btn_redondo) // Cambia el color a gris (color original)
                isRed3 = false
            } else {
                maps.setBackgroundResource(R.drawable.btn_redondo1) // Cambia el color a rojo
                isRed3 = true
            }
            true
        }




    }//FIN DEL MAIN


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event==null){
            return false
        }
        return if (mDetector.onTouchEvent(event)) {
            true
        } else {
            return super.onTouchEvent(event)
        }
    }

    override fun onSingleTapConfirmed(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onDoubleTap(p0: MotionEvent?): Boolean {
        Toast.makeText(this@PantallaPrincipal, "HAS HECHO DOUBLE TAP", Toast.LENGTH_SHORT).show()
        return true
    }

    override fun onDoubleTapEvent(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onShowPress(p0: MotionEvent?) {

    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onLongPress(p0: MotionEvent?) {

    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onResume() {
        super.onResume()
        proximitySensor?.also { proximity ->
            sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.values[0] < proximitySensor!!.maximumRange) {
                Toast.makeText(this, "Alejate del movil", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}


}