package com.example.adivina_palabra_examen

import kotlin.random.Random
import kotlin.random.nextInt

fun String.transformar(descolado:Boolean,funcion:(c:Char,pos:Int)->Any):String{

    var micaracter_transformado:Char
    var cadena_resultado= mutableListOf<Char>()
    for(indice in 0..this.length-1){
            micaracter_transformado= funcion(this[indice],indice) as Char
            cadena_resultado.add(micaracter_transformado)
    }
    var cadena_resultado_bis= mutableListOf<Char>()

    if(descolado){
        //De la forma más rápida
        //cadena_resultado.shuffle()

        //Reinventando la rueda
        //Creo el set para almacenar las posiciones aleatorias
        var miconjunto=mutableSetOf<Int>()
        while(miconjunto.size<this.length)
        {
            miconjunto.add(Random.nextInt(0..this.length-1))
        }

        //Genero la nueva cadena de dos formas diferentes
       /* miconjunto.forEach {
            cadena_resultado_bis.add(cadena_resultado.get(it))
        }*/
        for(valor in miconjunto)
        {
            cadena_resultado_bis.add(cadena_resultado.get(valor))
        }
        cadena_resultado=cadena_resultado_bis

    }

    return cadena_resultado.toString()
}
