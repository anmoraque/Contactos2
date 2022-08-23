package com.anmoraque.contactos2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    //Creamos las variables para la vista y para el adaptador
    var lista: ListView? = null
    var adaptador: AdaptadorCustom? = null
    //Creamos una lista de contactos y  funciones
    // los transformamos en un objeto estatico de esta forma.
    // Sirve para ser llamado en cualquier actividad y que no se borre su contenido
    companion object{
        var contactos: ArrayList<Contacto>? = null
        //Creo una funcion agregarContacto que recibe un objeto contacto
        //y lo añade a la lista contactos
        fun agregarContacto (contacto: Contacto){
            contactos?.add(contacto)
        }
        //Creo una funcion ObtenerContacto que requiere un index y regresa un objeto tipo contacto
        fun obtenerContacto (index: Int): Contacto{
            return contactos?.get(index)!!
        }
        //Creo una funcion eliminarContacto que requiere un index i de la lista contacto lo elimino
        fun eliminarContacto (index: Int){
            contactos?.removeAt(index)
        }
        //Creo una funcion actualizarContactos que requiere de un index y un nuevo contacto
        fun actualizarContacto(index: Int, nuevoContacto: Contacto){
            //Los arrayList (contactos) tienen una propiedad que permite
            //cambiar el contenido de una posicion especifica
            //Aqui cambiamos el contacto de la posicion index por el nuevoContacto
            contactos?.set(index, nuevoContacto)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Creamos la lista de contactos y agregamos contactos
        contactos = ArrayList()
        contactos?.add(Contacto("Marcos", "Rivas", "Intel", 25, "17/4/1997", "Urbanizacion Las adejas, 24", "34 678456789", "marcos@intel.com", R.drawable.foto_01))
        //Inicializamos la ListView
        lista = findViewById<ListView>(R.id.lista)
        //Inicializamos el AdaptadorCustom
        adaptador = AdaptadorCustom(this, contactos!!)
        //Decimos a la ListView que ponga lo que tiene su AdaptadorCustom
        lista?.adapter = adaptador
        //Escuchamos cada elemento de la lista (item)
        lista?.setOnItemClickListener { adapterView, view, i, l ->
            //Creamos un nuevo intent para pasar la posicion a la pantalla Detalle
            val intent = Intent(this, Detalle::class.java)
            //i es la posicion
            intent.putExtra("ID", i.toString())
            startActivity(intent)
        }
    }
    //Funcion que permite inflar el menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
    //Funcion que permite definir los items del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Con un when definimos cada item
        when(item?.itemId){
            R.id.iNuevo -> {
                //Creamos un intent que vaya de esta vista a Detalle
                val intent = Intent(this, NuevoContacto::class.java)
                startActivity(intent)
                return true
            }
            else -> {return super.onOptionsItemSelected(item)}
        }
    }
    //Usamos onResume para actualizar el adaptador de la vista con el nuevo elemento (contacto creado)
    override fun onResume() {
        super.onResume()
        //Actualizamos el adaptador
        adaptador?.notifyDataSetChanged()
    }
}