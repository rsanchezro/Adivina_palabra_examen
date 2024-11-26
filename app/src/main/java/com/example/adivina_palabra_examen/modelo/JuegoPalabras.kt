package com.example.adivina_palabra_examen.modelo

import kotlin.random.Random
import kotlin.random.nextInt

class JuegoPalabras() {

    var palabras =
       listOf("hola", "balon", "cacahuete", "aula", "ordenador", "raton", "pizarra")
    val pistas =
        listOf("Faltan caracteres", "Cambio vocal", "Posicion caracter", "Aumentar posicion")
    var puntos = 2
        get() = field
        set(value) {
            field = value
        }

    fun obtener_Palabra(): String {
      /*  var valor= Random.nextInt(0..palabras.size-1)
        return palabras[valor]*/
        return palabras.random()
    }

    fun obtener_Pista(num:Int):String{
        return pistas.get(num)
    }

    constructor(pal:List<String>):this(){
        this.palabras=pal
    }
}