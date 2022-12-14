package com.anmoraque.contactos2

/* La clase Contacto tiene un constructor con 9 parámetros, y cada parámetro se asigna a una propiedad
del mismo nombre */
class Contacto(nombre: String, apellidos: String, empresa: String, edad: Int, cumpleanos: String, direccion: String, telefono: String, email: String, foto: Int) {
    var nombre: String = ""
    var apellidos: String = ""
    var empresa: String = ""
    var edad: Int = 0
    var cumpleanos: String = ""
    var direccion: String = ""
    var telefono: String = ""
    var email: String = ""
    var foto: Int = 0

    init {
        this.nombre = nombre
        this.apellidos = apellidos
        this.empresa = empresa
        this.edad = edad
        this.cumpleanos = cumpleanos
        this.direccion = direccion
        this.telefono = telefono
        this.email = email
        this.foto = foto
    }
}