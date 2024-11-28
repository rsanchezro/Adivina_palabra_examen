package com.example.adivina_palabra_examen.modelo

data class funcionlambda(
    var f: (Char, Int) -> Char,
    var puntos_incremento:Int,
    var puntos_decremento:Int)
