/**
 * Clase que contiene el método que muestra el splash screen al inicio
 * @author Virginia Ojeda Corona
 */
package edu.virginiaojeda.cuencamovil

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import edu.virginiaojeda.cuencamovil.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySplashScreenBinding

    /**
     * Muestra el splash screen durante 1.5 segundos y después inicia la actividad MainActivity
     * con una animación de transición entre ambas. Está actividad se elimina de la pila de
     * actividades para liberar recursos
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, MainActivity::class.java)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            this.finish()
            }, 1500)
    }
}