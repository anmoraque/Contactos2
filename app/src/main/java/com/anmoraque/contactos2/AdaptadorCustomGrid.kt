package com.anmoraque.contactos2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

//Definimos el adaptador para que coja de referencia el archivo
//template_contacto_grid para nuestra ListView
//Voy a recibir el contexto y cada contacto del ArrayList, heredo de BaseAdapter
class AdaptadorCustomGrid(var contexto: Context, items: ArrayList<Contacto>): BaseAdapter() {
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
    //Aqui regreso el numero de elementos de mi lista
    /**
     * It returns the number of items in the list.
     *
     * @return The number of items in the list.
     */
    override fun getCount(): Int {
        //Retorno el contador de items, lo pongo opcional ? y para obtener el valor de la propiedad !!
        return this.items?.count()!!
    }
    //Aqui regresamos el objeto entero (el contacto entero)
    /**
     * The function returns the item at the position that was touched (touched contact), I put it
     * optional? And to get the value of the property !!
     *
     * @param p0 Int: The position of the item within the adapter's data set of the item whose view we
     * want.
     * @return The item in the position touched (touched contact), I put it optional? And to get the
     * value of the property !!
     */
    override fun getItem(p0: Int): Any {
        //Retorno el item de la posicion tocada (contacto tocado), lo pongo opcional ? y para obtener el valor de la propiedad !!
        return items?.get(p0)!!
    }
    //Aqui regreso (obtengo) la posicion del item (contacto) que seleccionemos
    /**
     * It returns the position of the item in the list.
     *
     * @param p0 The position of the item within the adapter's data set of the item whose view we want.
     * @return The position of the item in the list.
     */
    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }
    //Creamos una funcion para agregar un item (para agregar un contacto)
    /**
     * It adds an item to the list.
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
    //Creamos una funcion para eliminar un item (eliminar un contacto)
    /**
     * It removes an item from the list.
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
    //Creamos una funcion para actualizar un item (actualizar un contacto)
    /**
     * We update the item in the unmodifiable list, then we make a copy of the unmodifiable list to the
     * modifiable list, and finally we call the notifyDataSetChanged() function to render the changes
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
    //Aqui asociamos el renderizado de los elementos con el objeto asociado (en este caso con clase Contacto)
    //Para esto se necesita un ViewHolder (funcion echa mas abajo)
    /**
     * We create a viewHolder variable, then we create a view variable and assign it to the view that
     * the function is asking us to reference (p1). Then we validate if the view is empty or not. If it
     * is empty, we inflate the template_contacto_grid layout, then we define the viewHolder, and give
     * it a TAG to identify the view. If the view is not empty, we assign the viewHolder to the view's
     * TAG. Then we create a variable with the position that the item occupies and cast it to a
     * Contacto. Finally, we assign the values to the graphic elements
     *
     * @param p0 The position of the item within the adapter's data set of the item whose view we want.
     * @param p1 View? = The view that is being recycled.
     * @param p2 ViewGroup?
     * @return The view
     */
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        //Creo la variable del viewHolder
        var viewHolder: ViewHolder? = null
        //Creo la vista que la funcion me pide referenciar (p1)
        var vista: View? = p1
        //Validamos si esta vista esta vacia o tiene algo
        if (vista == null){
            //Si esta vacia le inflamos el template_contacto_grid
            vista = LayoutInflater.from(contexto).inflate(R.layout.template_contacto_grid, null)
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
        viewHolder?.foto?.setImageResource(item.foto)
        return vista!!
    }
    //Creamos una funcion para filtrar la busqueda, recibo un texto de busqueda (str)
    /**
     * We create a copy of the items list, then we clear the items list, then we check if the search
     * text is empty, if it is, we set the items list to the copy of the items list, if it isn't, we
     * create a variable with the search text, then we loop through the copy of the items list and
     * check if the name of the item contains the search text, if it does, we add it to the items list,
     * if it doesn't, we don't add it to the items list
     *
     * @param str String
     * @return the number of items in the list.
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
    //Voy a requerir una vista como parametro
    /* The ViewHolder class is a class that is used to hold the views that are used in the
     * RecyclerView.
    */
    private class ViewHolder(vista: View) {
        //A esta vista le cargo los elementos y los inicializo
        //¿Que elementos necesito para el template_contacto?
        //El nombre y la foto. Pues los creo
        var nombre: TextView? = null
        var foto: ImageView? = null
        init {
            nombre = vista.findViewById(R.id.tvNombreGrid)
            foto = vista.findViewById(R.id.ivFotoGrid)
        }
    }
}