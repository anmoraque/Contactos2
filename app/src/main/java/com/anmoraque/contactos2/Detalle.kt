package com.anmoraque.contactos2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Detalle : AppCompatActivity() {
    //Creamos los valores que necesitamos y igualamos a null
    var nombre: TextView? = null
    var empresa: TextView? = null
    var edad: TextView? = null
    var cumpleaños: TextView? = null
    var direccion: TextView? = null
    var telefono: TextView? = null
    var email: TextView? = null
    var fotoContacto: ImageView? = null
    //Creamos una variable global en la actividad para el index (id del contacto)
    var index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)
        //Creamos el boton de ir atras
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //Creo una variable con la ID que trae el intent del MainActivity y lo paso a Int
        index = intent.getStringExtra("ID")?.toInt()!!
        //Inicializamos los valores con las vistas mediante la funcion iniciarActividad
        iniciarActividad()
    }
    //Inicializamos todas las vistas
    fun iniciarActividad() {
        //Creo una variable para traer el contacto del MainActivity con el Id igual al index lo casteo a Int
        val contacto = MainActivity.obtenerContacto(index)
        //Inicializamos los valores con las vistas
        //Con this, me estoy refiriendo a la propia pantalla que estoy visualizando en este momento
        nombre = findViewById(R.id.tvNombreDetalle)
        direccion = findViewById(R.id.tvDireccionDetalle)
        edad = findViewById(R.id.tvEdadDetalle)
        cumpleaños = findViewById(R.id.tvCumpleanosDetalle)
        telefono = findViewById(R.id.tvTelefonoDetalle)
        email = findViewById(R.id.tvEmailDetalle)
        empresa = findViewById(R.id.tvEmpresaDetalle)
        fotoContacto = findViewById(R.id.imageDetalle)

        nombre?.text = contacto.nombre + " " + contacto?.apellidos
        direccion?.text = contacto.direccion
        edad?.text = contacto.edad.toString()
        cumpleaños?.text = contacto.cumpleanos
        telefono?.text = contacto.telefono
        email?.text = contacto.email
        empresa?.text = contacto.empresa
        fotoContacto?.setImageResource(contacto.foto)
    }
    //Funcion que permite inflar el menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detalle, menu)
        return super.onCreateOptionsMenu(menu)
    }
    //Funcion que permite definir los items del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Con un when definimos cada item
        when (item.itemId) {
            //Creamos un intent que nos lleve de nuevo a la actividad NuevoContacto
            R.id.iEditar -> {
                var intent = Intent(this, NuevoContacto::class.java)
                //Nos llevamos en el intent el index (id del contacto) a modificar
                intent.putExtra("IDCONTACTOAMODIFICAR", index.toString())
                startActivity(intent)
                return true
            }
            //Llamamos a la funcion eliminar contacto y le pasamos
            //el index que llego del intent y luego un finish
            R.id.iEliminar -> {
                MainActivity.eliminarContacto(index)
                finish()
                return true
            }
            //Al boton de atras le hacemos finish para que se termine la actividad
            //y no se pierdan los contactos
            android.R.id.home -> {
                finish()
                return true
            }
            else -> { return super.onOptionsItemSelected(item) }
        }
    }
    //En on resume volvemos a cargar las vistas para que el contacto modificado se vea
    override fun onResume() {
        super.onResume()
        //Inicializamos los valores con las vistas mediante la funcion iniciarActividad
        iniciarActividad()
    }
}