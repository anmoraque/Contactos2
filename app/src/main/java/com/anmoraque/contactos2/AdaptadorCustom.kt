package com.anmoraque.contactos2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

//Definimos el adaptador para que coja de referencia el archivo
//template_contacto para nuestra ListView
//Voy a recibir el contexto y cada contacto del ArrayList, heredo de BaseAdapter
class AdaptadorCustom(var contexto: Context, items: ArrayList<Contacto>): BaseAdapter() {

    //Creo la variable para almacenar cada elemento a mostrar en el ListView (cada contacto)
    //Esta si se modifica
    var items: ArrayList<Contacto>? = null
    //Creo un nuevo ArrayList para filtar la busqueda y no se modifica
    var copiaItems: ArrayList<Contacto>? = null
    //Inicializamos la variable
    init {
        //Inicializo items con el ArrayList de items asi consigo una copia nueva
        this.items = ArrayList(items)
        //Con esta accion items y copiaItems no tienen la misma informacion
        //a nivel memoria pero si a nivel contenido
        this.copiaItems = items
    }
    /**
     * Aqui regreso el numero de items de mi lista.
     *
     * @return El numero de items en la lista.
     */
    override fun getCount(): Int {
        //Retorno el contador de items, lo pongo opcional ? y para obtener el valor de la propiedad !!
        return this.items?.count()!!
    }
    /**
     * Aqui regresamos el objeto entero (el contacto entero)
     * La función devuelve el elemento en la posición que se tocó (contacto tocado), lo puse
     * ¿opcional? Y para obtener el valor de la propiedad !!
     *
     * @param p0 Int: La posición del elemento dentro del conjunto de datos del adaptador
     * es el elemento cuya vista queremos.
     * @return El elemento en la posición tocado (contacto tocado), lo pongo opcional?
     * Y para obtener el valor de la propiedad !!
     */
    override fun getItem(p0: Int): Any {
        //Retorno el item de la posicion tocada (contacto tocado), lo pongo opcional ? y para obtener el valor de la propiedad !!
        return items?.get(p0)!!
    }
    /**
     * Aqui regreso (obtengo) la posicion del item (contacto) que seleccionemos
     * Devuelve la posición del elemento en la lista.
     *
     * @param p0 La posición del elemento dentro del conjunto de datos del adaptador
     * es el elemento cuya vista queremos.
     * @return La posición del elemento en la lista.
     */
    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }
    /**
     * Agrega un elemento a la lista.
     *
     * @param item Contacto
     */
    fun agregarItem (item: Contacto){
        //Añadimos el item a la lista no modificable
        copiaItems?.add(item)
        //Hacemos una copia a la lista modificable
        items = ArrayList(copiaItems)
        //Para que vuelva a renderizar usamos esta funcion
        notifyDataSetChanged()
    }
    /**
     * Elimina un elemento de la lista.
     *
     * @param index Int
     */
    fun eliminarItem(index: Int){
        //Eliminamos el item a la lista no modificable
        copiaItems?.removeAt(index)
        //Hacemos una copia a la lista modificable
        items = ArrayList(copiaItems)
        //Para que vuelva a renderizar usamos esta funcion
        notifyDataSetChanged()
    }
    /**
     * Creamos una funcion para actualizar un item (actualizar un contacto)
     * Actualizamos el elemento en la lista no modificable, luego hacemos una copia de esa lista
     * a la lista modificable y finalmente llamamos a la función notificarDataSetChanged()
     * para representar los cambios.
     *
     * @param index The index of the item to be updated.
     * @param nuevoItem Contacto
     */
    fun actualizarItem(index: Int, nuevoItem: Contacto){
        //Actualizamos el item a la lista no modificable
        copiaItems?.set(index, nuevoItem)
        //Hacemos una copia a la lista modificable
        items = ArrayList(copiaItems)
        //Para que vuelva a renderizar usamos esta funcion
        notifyDataSetChanged()
    }
    /**
     * Aqui asociamos el renderizado de los elementos con el objeto asociado (en este caso con clase Contacto)
     * Para esto se necesita un ViewHolder (funcion echa mas abajo)
     *
     * @param p0 La posición del elemento dentro del conjunto de datos del adaptador del elemento cuya vista queremos.
     * @param p1 View? - La vieja vista para reutilizar, si es posible. Nota: Debe comprobar que esta vista es
     * no nulo y de un tipo apropiado antes de usar. Si no es posible convertir esta vista a
     * mostrar los datos correctos, este método puede crear una nueva vista.
     * @param p2 ViewGroup?
     * @return La vista
     */
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        //Creo la variable del viewHolder
        var viewHolder: ViewHolder? = null
        //Creo la vista que la funcion me pide referenciar (p1)
        var vista: View? = p1
        //Validamos si esta vista esta vacia o tiene algo
        if (vista == null){
            //Si esta vacia le inflamos el template_contacto
            vista = LayoutInflater.from(contexto).inflate(R.layout.template_contacto, null)
            //Ahora definimos el holder
            viewHolder = ViewHolder(vista)
            //Damos un TAG para identificar la vista
            vista.tag = viewHolder
        }else{
            //Si no esta vacia pues lo igualamos al tag de la vista
            viewHolder = vista.tag as? ViewHolder
        }
        //Creamos una variable con la posicion que ocupa y la casteamos a contacto
        val item = getItem(p0) as Contacto
        //Le asignamos los valores a los elementos graficos
        viewHolder?.nombre?.text = item.nombre + " " + item.apellidos
        viewHolder?.empresa?.text = item.empresa
        viewHolder?.foto?.setImageResource(item.foto)
        return vista!!
    }
    /**
     * Creamos la funcion para filtrar la busqueda, recibo un texto de busqueda (str)
     *
     * @param str String
     * @return el numero de elementos de la lista
     */
    fun filtrar(str: String){
        //Necesito hacer una copia exacta de la informacion
        //que tengo en mis items (Cada contacto del ArrayList)
        //Eliminamos el contenido de la copia
        items?.clear()
        //Validamos si el texto de busqueda esta vacio
        if (str.isEmpty()){
            //No esta buscando nada el usuario, items es igual a copiaItems
            items = ArrayList(copiaItems)
            //Para que vuelva a renderizar usamos esta funcion
            notifyDataSetChanged()
            //Return para salir de la funcion
            return
        }
        //Creamos una variable con el texto de busqueda
        var busqueda = str
        //Buscamos solo con minusculas para no tener conflictos
        busqueda = busqueda.lowercase()
        //Recorremos cada uno de los elementos de copiaItems para encontrar la busqueda que hago
        //Por cada item de copiaItems (!! para obtener el contenido)
        for (item in copiaItems!!){
            //Creamos un valor para nombre en minuscula
            val nombre = item.nombre.lowercase()
            //Validamos si mi val nombre contiene la var busqueda (es true)
            if (nombre.contains(busqueda)){
                //Añado ese elemento a la lista modificacble items
                items?.add(item)
            }//Y si no pues no se agrega nada
        }
        //Para que vuelva a renderizar usamos esta funcion
        notifyDataSetChanged()
    }
    /* La clase ViewHolder es una clase que contiene las vistas que necesitamos para completar la lista */
    private class ViewHolder(vista: View) {
        //A esta vista le cargo los elementos y los inicializo
        //¿Que elementos necesito para el template_contacto?
        //El nombre, la empresa y la foto. Pues los creo
        var nombre: TextView? = null
        var empresa: TextView? = null
        var foto: ImageView? = null
        init {
            nombre = vista.findViewById(R.id.tvNombre)
            empresa = vista.findViewById(R.id.tvEmpresa)
            foto = vista.findViewById(R.id.ivFoto)
        }
    }
}