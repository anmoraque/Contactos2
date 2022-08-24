package com.anmoraque.contactos2

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*

class MainActivity : AppCompatActivity() {
    //Creamos la variable para la vista
    var lista: ListView? = null
    //Creamos la variable para el GridView
    var grid: GridView? = null
    //Creamos una variable para usar el SwitchView
    var viewSwitcher: ViewSwitcher? = null
    //Creamos una lista de contactos y los dos adaptadores mas unas funciones
    // los transformamos en un objeto estatico de esta forma.
    // Sirve para ser llamado en cualquier actividad y que no se borre su contenido
    companion object{
        var contactos: ArrayList<Contacto>? = null
        var adaptadorCustom: AdaptadorCustom? = null
        var adaptadorCustomGrid: AdaptadorCustomGrid? = null
        //Creo una funcion agregarContacto que recibe un objeto contacto
        //y lo añade a la lista contactos
        fun agregarContacto (contacto: Contacto){
            adaptadorCustom?.agregarItem(contacto)
        }
        //Creo una funcion ObtenerContacto que requiere un index y regresa
        //la posicion del adaptador y lo casteamos a contacto
        fun obtenerContacto (index: Int): Contacto{
            return adaptadorCustom?.getItem(index) as Contacto
        }
        //Creo una funcion eliminarContacto que requiere un index i de la lista contacto lo elimino
        fun eliminarContacto (index: Int){
            adaptadorCustom?.eliminarItem(index)
        }
        //Creo una funcion actualizarContactos que requiere de un index y un nuevo contacto
        fun actualizarContacto(index: Int, nuevoContacto: Contacto){
            //Los arrayList (contactos) tienen una propiedad que permite
            //cambiar el contenido de una posicion especifica
            //Aqui cambiamos el contacto de la posicion index por el nuevoContacto
            adaptadorCustom?.actualizarItem(index, nuevoContacto)
        }
    }
    /**
     * Crea una lista de contactos y los agrega a la lista.
     *
     * @param savedInstanceState Un objeto Bundle que contiene el estado guardado previamente de la actividad.
     * Si la actividad nunca ha existido antes, el valor del objeto Bundle es nulo.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Creamos la lista de contactos y agregamos contactos
        contactos = ArrayList()
        contactos?.add(Contacto("Marcos", "Rivas", "Intel", 25, "17/4/1997", "Urbanizacion Las adejas, 24", "34 678456789", "marcos@intel.com", R.drawable.foto_01))
        contactos?.add(Contacto("Eugenio", "Morales", "Intel", 25, "17/4/1997", "Urbanizacion Las adejas, 24", "34 678456789", "marcos@intel.com", R.drawable.foto_02))
        contactos?.add(Contacto("Antonio", "Guerra", "Intel", 25, "17/4/1997", "Urbanizacion Las adejas, 24", "34 678456789", "marcos@intel.com", R.drawable.foto_03))
        contactos?.add(Contacto("Luis", "Ruiz", "Intel", 25, "17/4/1997", "Urbanizacion Las adejas, 24", "34 678456789", "marcos@intel.com", R.drawable.foto_04))
        //Inicializamos la ListView
        lista = findViewById(R.id.lista)
        //Inicializamos la GridView
        grid = findViewById(R.id.grid)
        //Inicializamos el viewSwitcher
        viewSwitcher = findViewById(R.id.viewSwitcher)
        //Inicializamos el AdaptadorCustom
        adaptadorCustom = AdaptadorCustom(this, contactos!!)
        //Inicializamos el AdaptadorCustomGrid
        adaptadorCustomGrid = AdaptadorCustomGrid(this, contactos!!)
        //Decimos a la ListView que ponga lo que tiene su AdaptadorCustom
        lista?.adapter = adaptadorCustom
        //Decimos a la GridView que ponga lo que tiene su AdaptadorCustomGrid
        grid?.adapter = adaptadorCustomGrid
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
    /**
     * Crea el botón de búsqueda y el botón de cambio.
     *
     * @param menu El menú en el que colocas tus items.
     * @return El método devuelve un valor booleano.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        //Creamos la variable para el servicio de busqueda
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        //Inicializamos el boton de busqueda
        val itemBusqueda = menu?.findItem(R.id.searchView)
        //Inicializamos el Switch
        val itemSwitch = menu?.findItem(R.id.switchView)
        //Le decimos al switch cual es su recurso layout (es un relative layout)
        itemSwitch?.setActionView(R.layout.switch_item)
        //Ahora lo inicializamos dentro del relative layout por su ID
        val switchView = itemSwitch?.actionView?.findViewById<Switch>(R.id.sCambiarVista)
        //Implementamos un evento para saber cuando cambia el estado del switch
        switchView?.setOnCheckedChangeListener { compoundButton, b ->
            //Cuando de detecte un cambio en el viewSwitcher se usa este metodo
            viewSwitcher?.showNext()
            //Ponemos un toast para saber el cambio
            Toast.makeText(this, R.string.tvCambioVista, Toast.LENGTH_SHORT).show()
        }
        //Implementamos la busqueda en el boton
        val searchView = itemBusqueda?.actionView as SearchView
        //Configuramos el servicio de busqueda mediante el nombre
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        //Implementamos los metodos para detectar que esta buscando el usuario
        //El primero para obtener el foco y preparar los datos
        searchView.setOnQueryTextFocusChangeListener { view, b -> }
        //Aqui van dos metodos que hay que implementar
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(p0: String?): Boolean {
                //El segundo sirve para filtrar cada vez que se va introduciendo el texto a buscar
                //Llamamos a la funcion filtar del adapter le pasamos el contenido p0
                adaptadorCustom?.filtrar(p0!!)
                return true
            }
            override fun onQueryTextSubmit(p0: String?): Boolean {
                //El tercero sirve para filtrar el texto ya completo
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
    /**
     * Funcion que permite definir los items del menu
     * Se llama cuando se selecciona un elemento del menú.
     *
     * @param item El elemento del menú que se seleccionó.
     * @return Se devuelve el método de la superclase.
     */
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
    /**
     * Usamos onResume para actualizar el adaptador de la vista con el nuevo elemento (contacto creado).
     */
    override fun onResume() {
        super.onResume()
        //Actualizamos el adaptador
        adaptadorCustom?.notifyDataSetChanged()
    }
}