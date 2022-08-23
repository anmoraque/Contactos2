package com.anmoraque.contactos2

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class NuevoContacto : AppCompatActivity() {
    //Creamos una variable global para la actividad para el index (id del contacto)
    //Ponemos -1 para que no crea que quiero modificar el elemento 0, sino ninguno por ahora
    var index: Int = -1
    //Creamos los valores que necesitamos y igualamos a null
    var nombre: EditText? = null
    var apellido: EditText? = null
    var empresa: EditText? = null
    var edad: EditText? = null
    var cumpleaños: EditText? = null
    var direccion: EditText? = null
    var telefono: EditText? = null
    var email: EditText? = null
    var fotoContacto: ImageView? = null
    //Para validar el formulario
    var formularioValido = false
    //Para el indice de las fotos a seleccionar
    var fotoIndex: Int = 0
    //Creamo una lista con las fotos a seleccionar y su ubicacion
    val fotos = arrayOf(R.drawable.foto_01, R.drawable.foto_02, R.drawable.foto_03, R.drawable.foto_04, R.drawable.foto_05, R.drawable.foto_06)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_contacto)
        //Creamos el boton de ir atras
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //Iniciamos la funcion que carga las vistas
        iniciarActividad()
        //Le hacemos el listener a la fotoContacto
        fotoContacto?.setOnClickListener {
            //Iniciamos la funcion seleccionarFoto
            seleccionarFoto()
        }
        //Tengo que reconocer si es nuevo contacto o editar un contacto
        //Si el intent es ID es que hay que editarlo
        if (intent.hasExtra("IDCONTACTOAMODIFICAR")){
            //Lo ponemos en el index
            index = intent.getStringExtra("IDCONTACTOAMODIFICAR")?.toInt()!!
            //Llamamos a la funcion rellenarDatos
            rellenarDatos(index)
        }
    }
    //Funcion que permite inflar el menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_nuevo, menu)
        return super.onCreateOptionsMenu(menu)
    }
    //Funcion que permite definir los items del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Con un when definimos cada item
        when (item?.itemId) {
            //Al boton de atras le hacemos finish para que se termine la actividad
            //y no se pierdan los contactos
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.iCrearContacto -> {
                //Validar los campos
                formularioValido = esFormularioValido()
                if (formularioValido != true) {
                    //Si el formulario no es válido creamos un toast
                    Toast.makeText(
                        this,
                        R.string.tvRellenecorrecto,
                        Toast.LENGTH_SHORT).show()
                    }else{
                    //Si el formulario es válido
                        if (index > -1){
                            //Si el formulario es válido y el index
                            //es mayor a -1 estamos actualizando el contacto
                            Log.d("ETIQUETA", "Contacto a modificar")
                            //Llamamos la funcion actualizarContacto del MainActivity
                            MainActivity.actualizarContacto(index, Contacto(nombre?.text.toString(), apellido?.text.toString(), empresa?.text.toString(), edad?.text.toString().toInt(), cumpleaños?.text.toString(), direccion?.text.toString(), telefono?.text.toString(), email?.text.toString(), obtenerFoto(fotoIndex)))
                            Toast.makeText(this, R.string.tvContactoModificado, Toast.LENGTH_SHORT).show()
                            //Terminamos la actividad
                            finish()
                        }else{
                            //Si el formulario es válido y el index
                            //no es mayor a -1 estamos añadimos un nuevo contacto
                            Log.d("ETIQUETA", "Contacto nuevo")
                            MainActivity.agregarContacto(Contacto(nombre?.text.toString(), apellido?.text.toString(), empresa?.text.toString(), edad?.text.toString().toInt(), cumpleaños?.text.toString(), direccion?.text.toString(), telefono?.text.toString(), email?.text.toString(), obtenerFoto(fotoIndex)))
                            Toast.makeText(this, R.string.tvContactoAñadido, Toast.LENGTH_SHORT).show()
                            //Terminamos la actividad
                            finish()
                        }
                    }
                return true
            }
            else -> { return super.onOptionsItemSelected(item) }
        }
    }
    //Inicializamos todas las vistas
    fun iniciarActividad() {
        //Inicializamos los valores con las vistas
        //Con this, me estoy refiriendo a la propia pantalla que estoy visualizando en este momento
        nombre = findViewById(R.id.tvNombreNuevo)
        apellido = findViewById(R.id.tvApellidosNuevo)
        direccion = findViewById(R.id.tvDireccionNuevo)
        edad = findViewById(R.id.tvEdadNuevo)
        cumpleaños = findViewById(R.id.tvCumpleanosNuevo)
        telefono = findViewById(R.id.tvTelefonoNuevo)
        email = findViewById(R.id.tvEmailNuevo)
        empresa = findViewById(R.id.tvEmpresaNuevo)
        fotoContacto = findViewById(R.id.imageNuevo)
    }
    //Valida el teléfono
    private fun esTelefonoValido(): Boolean {
        var telefono_valido = false
        var telefono: String = this.telefono?.text.toString()
        //Con esta expresión, estoy diciendo, que el telefono es válido
        val pattern =  Patterns.PHONE
        telefono_valido = pattern.matcher(telefono).matches()
        return telefono_valido
    }
    //Valida el nombre
    private fun esNombreValido(): Boolean {
        var nombre_valido = false
        var nombre: String = this.nombre?.text.toString()
        //Con esta expresión regular, estoy diciendo, que el nombre es válido
        //si tiene caracteres del alfabéticos de la a la z mayusculas o minusuclas y espacios y al menos uno (+)
        nombre_valido = nombre != null && nombre.matches("[a-zA-Z áéíóúÁÉÍÓÚñÑ\\s]+".toRegex())
        return nombre_valido
    }
    //Valida el apellido
    private fun esApellidoValido(): Boolean {
        var apellido_valido = false
        var apellido: String = this.apellido?.text.toString()
        //Con esta expresión regular, estoy diciendo, que el apellido es válido
        //si tiene caracteres del alfabéticos de la a la z mayusculas o minusuclas y espacios y al menos uno (+)
        apellido_valido = apellido != null && apellido.matches("[a-zA-Z áéíóúÁÉÍÓÚñÑ\\s]+".toRegex())
        return apellido_valido
    }
    //Valida la empresa
    private fun esEmpresaValido(): Boolean {
        var empresa_valido = false
        var empresa: String = this.empresa?.text.toString()
        //Con esta expresión regular, estoy diciendo, que la empresa es válida
        //si tiene caracteres del alfabéticos de la a la z mayusculas o minusuclas y espacios y al menos uno (+)
        empresa_valido =
            empresa != null && empresa.matches("[a-zA-Z áéíóúÁÉÍÓÚñÑ\\s]+".toRegex())
        return empresa_valido
    }
    //Valida el cumpleaños
    private fun esCumpleañosValido(): Boolean {
        var cumpleaño_valido = false
        var cumpleaño: String = this.cumpleaños?.text.toString()
        //Con esta expresión regular, estoy diciendo, que el cumpleaños es válido
        //si tiene caracteres numericos, una barra/ y al menos uno (+)
        cumpleaño_valido = cumpleaño != null && cumpleaño.matches("[0-9/]+".toRegex())
        return cumpleaño_valido
    }
    //Valida el edad
    private fun esEdadValido(): Boolean {
        var edad_valido = false
        var edad: String = this.edad?.text.toString()
        //Con esta expresión regular, estoy diciendo, que la edad es válida
        //si tiene caracteres numericos
        edad_valido = edad != null && edad.matches("[0-9]+".toRegex())
        return edad_valido
    }
    //Valida la dirección
    private fun esDireccionValida(): Boolean {
        var direccion_valida = false
        var direccion: String = this.direccion?.text.toString()
        //Con esta expresión regular, estoy diciendo, que la direccion es válida
        //si tiene caracteres del alfabéticos de la a la z mayusculas o minusculas y espacios y al menos uno (+)
        direccion_valida = direccion != null && direccion.matches("[a-zA-Z0-9,.ªº()áéíóúÁÉÍÓÚñÑ\\s]+".toRegex())
        return direccion_valida
    }
    //Valida el email
    private fun esEmailValido(): Boolean {
        var email_valido = false
        var email: String = this.email?.text.toString()
        //Con esta expresión regular, estoy diciendo, que el email es válido
        val pattern = Patterns.EMAIL_ADDRESS
        email_valido = pattern.matcher(email).matches()
        return email_valido
    }
    //Valida el formulario completo
    private fun esFormularioValido(): Boolean {
        //LOS BOOLEAN SE INICIALIZAN A FALSE POR DEFECTO
        var formulario_valido = false
        var nombre_valido = false
        var apellido_valido = false
        var empresa_valida = false
        var cumpleaños_valido = false
        var edad_valido = false
        var direccion_valido = false
        var email_valido = false
        var telefono_valido = false
        nombre_valido = esNombreValido()
        apellido_valido = esApellidoValido()
        empresa_valida = esEmpresaValido()
        cumpleaños_valido = esCumpleañosValido()
        edad_valido = esEdadValido()
        direccion_valido = esDireccionValida()
        email_valido = esEmailValido()
        telefono_valido = esTelefonoValido()
        //Si todos son validados (true) retorno el formulario valido
        formulario_valido =
            telefono_valido && nombre_valido && apellido_valido && empresa_valida && cumpleaños_valido && edad_valido && direccion_valido && email_valido
        return formulario_valido
    }
    //Esta funcion me va a permitir seleccionar una foto
    fun seleccionarFoto(){
        //Creamos un AlertDialog
        val builder = AlertDialog.Builder(this)
        //Le ponemos un titulo
        builder.setTitle(R.string.tvSeleccionarImagen)
        //Primero requiero un adaptador del dialogo
        val adaptadorDialogo = ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item)
        //Ahora le añadimos los elementos (fotos) los mapeamos
        adaptadorDialogo.add("01")
        adaptadorDialogo.add("02")
        adaptadorDialogo.add("03")
        adaptadorDialogo.add("04")
        adaptadorDialogo.add("05")
        adaptadorDialogo.add("06")
        //Añadimos el adaptador al builder
        builder.setAdapter(adaptadorDialogo){
            dialog, which -> fotoIndex = which
            //Añadimos la foto seleccionada al formulario de crear contacto
            //La obtenemos de la funcion obtenerFoto y pasandole el index (foto seleccionada)
            fotoContacto?.setImageResource(obtenerFoto(fotoIndex))
        }
        //Ponemos un boton por si desea cancelar
        builder.setNegativeButton(R.string.tvCancelar){ dialog, which -> dialog.dismiss() }
        //Por ultimo lo mostramos
        builder.show()
    }
    //Creamos otra funcion para obtener la foto donde se pide un indice y va a regresar un numero
    //Que sera la foto a escoger
    fun obtenerFoto(index: Int): Int{
        return fotos.get(index)
    }
    fun rellenarDatos(index: Int){
        //Creamos un nuevo contacto con la funcion obtenerContacto del MainActivity
        //pasandole el index
        val contacto = MainActivity.obtenerContacto(index)
        //En vex de .text como vamos a editar usamos setText y al final especificamos que es editable
        nombre?.setText(contacto.nombre, TextView.BufferType.EDITABLE)
        apellido?.setText(contacto.apellidos, TextView.BufferType.EDITABLE)
        direccion?.setText(contacto.direccion, TextView.BufferType.EDITABLE)
        edad?.setText(contacto.edad.toString(), TextView.BufferType.EDITABLE)
        cumpleaños?.setText(contacto.cumpleanos, TextView.BufferType.EDITABLE)
        telefono?.setText(contacto.telefono, TextView.BufferType.EDITABLE)
        email?.setText(contacto.email, TextView.BufferType.EDITABLE)
        empresa?.setText(contacto.empresa, TextView.BufferType.EDITABLE)
        fotoContacto?.setImageResource(contacto.foto)

        //Creamos una variable para la posicion de la foto dentro de la lista fotos
        var posicion = 0
        //Recorremos las fotos de la lista de fotos
        for (foto in fotos){
            //Si la foto del contacto es igual a foto
            if (contacto.foto == foto){
                fotoIndex = posicion
                //No hace falta poner el else
            }
            //Si no lo encuentra incremento en uno la posicion
            posicion++
        }
    }
}