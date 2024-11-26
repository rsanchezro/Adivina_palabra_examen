package com.example.adivina_palabra_examen.modelo

data class funcionlambda(
    var f: (Char, Int) -> Any,
    var puntos_incremento:Int,
    var puntos_decremento:Int)
