package com.anmoraque.contactos2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

//Definimos el adaptador para que coja de referencia el archivo
//template_contacto para nuestra ListView
//Voy a recibir el contexto y cada contacto del ArrayList, heredo de BaseAdapter
class AdaptadorCustom(var contexto: Context, items: ArrayList<Contacto>): BaseAdapter() {

    //Creo la variable para almacenar cada elemento a mostrar en el ListView (cada contacto)
    var items: ArrayList<Contacto>? = null
    //Inicializamos la variable
    init {
        this.items = items
    }
    //Aqui regreso el numero de elementos de mi lista
    override fun getCount(): Int {
        //Retorno el contador de items, lo pongo opcional ? y para obtener el valor de la propiedad !!
        return this.items?.count()!!
    }
    //Aqui regresamos el objeto entero (el contacto entero)
    override fun getItem(p0: Int): Any {
        //Retorno el item de la posicion tocada (contacto tocado), lo pongo opcional ? y para obtener el valor de la propiedad !!
        return items?.get(p0)!!
    }
    //Aqui regreso la posicion del item (contacto) que seleccionemos
    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }
    //Aqui asociamos el renderizado de los elementos con el objeto asociado (en este caso con clase Contacto)
    //Para esto se necesita un ViewHolder (funcion echa mas abajo)
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
    //Voy a requerir una vista como parametro
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