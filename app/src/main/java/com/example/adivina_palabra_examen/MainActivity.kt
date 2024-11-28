package com.example.adivina_palabra_examen

import android.os.Bundle
import android.os.CountDownTimer
import android.text.method.KeyListener
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adivina_palabra_examen.databinding.ActivityMainBinding
import com.example.adivina_palabra_examen.modelo.JuegoPalabras
import com.example.adivina_palabra_examen.modelo.funcionlambda
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {
    lateinit var mibinding:ActivityMainBinding
    var tiempo_inicial="3:00"
    var juego:JuegoPalabras= JuegoPalabras()
    var palabragenerada:String=""
    var num_funcion_transf:Int=0
    lateinit var temporizador:CountDownTimer

    var lista_funciones=listOf(funcionlambda({c:Char,p:Int->
        if(Random.nextInt(0..1)==0)
            '_'
        else
            c

    },8,1),
        funcionlambda({c:Char,p:Int->
            when(c){
                'a'->'e'
                'e'->'i'
                'i'->'o'
                'o'->'u'
                'u'->'a'
                else->c

            }

        },5,2

        ),
        funcionlambda({c:Char,p:Int->
            if(p%2!=0)
            {//Solo los impares se convierten
                (c.lowercaseChar().code+1).toChar()

            }
            else
                c

        },4,3))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mibinding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(mibinding.root)


        comenzar_aplicacion(true)

        añadir_escuchadores()



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun añadir_escuchadores() {
     //Boton comienza
        mibinding.jugarBoton.setOnClickListener {
            //Habilito el editText
            mibinding.palabraTextview.keyListener=mibinding.palabraTextview.tag as KeyListener
            //Habilito boton comprobar palabra
            mibinding.comprobarBoton.isEnabled=true
            //Deshabilito boton comienza juego
            mibinding.jugarBoton.isEnabled=false
            //Eliminamos la imagen
            mibinding.resultadoImagen.setImageDrawable(null)

            //Genero la palabra
            this.palabragenerada=juego.obtener_Palabra()


            //Genero el valor numerico para obtener la funcion que
            //transforma la cadena de forma aleatoria
            num_funcion_transf=Random.nextInt(0..lista_funciones.size-1)

            var func_lambda=lista_funciones.get(num_funcion_transf)
            var descolocar:Boolean=false
            if(num_funcion_transf==1)
                descolocar=true


            var cadena_transformada=palabragenerada.transformar(descolocar,func_lambda.f)
            //Muestro la palabra
            mibinding.palabraModificadaText.text=cadena_transformada
            //Muestro la pista
            mibinding.pistaTextview.text=juego.obtener_Pista(num_funcion_transf)

            //Temporizador
            temporizador=object:CountDownTimer(180000,1000){
                override fun onTick(p0: Long) {
                    var s=p0/1000
                    var minutos=s/60
                    var segundos=s%60
                    var texto_segundos:String=segundos.toString()
                    if(segundos<10)
                    {
                        texto_segundos="0$segundos"
                    }
                    //Actualizar el textview que muestra el tiempo
                    mibinding.relojTextview.text="$minutos:$texto_segundos"

                }

                override fun onFinish() {

                    //Poner imagen de perdido
                    mibinding.resultadoImagen.setImageDrawable(ContextCompat.getDrawable(this@MainActivity,R.drawable.baseline_face_retouching_off_24))
                    //Inicializamos el juego
                    comenzar_aplicacion(false)
                }

            }
            //Arranco el reloj
            temporizador.start()








        }


        mibinding.comprobarBoton.setOnClickListener {
            //Comprobar si el jugador va acertando las palabras o no
            //Incrementar y decrementar y controlar el fin del juego

            if(palabragenerada.equals(mibinding.palabraTextview.text))
            { //Las palabras son iguales
                //Sumar los puntos, que dependen de la funcion
                //transformadora y de la longitud de la palabra generada
                juego.puntos+=lista_funciones.get(num_funcion_transf).puntos_incremento+(palabragenerada.length/3)
                //Actualizar los puntos en la pantalla
                mibinding.puntosTextview.text=juego.puntos.toString()
                //Comprobar si ha ganado
                if(juego.puntos>50)
                {

                    //Salte mensaje ganador
                    Toast.makeText(this,"HAS GANADO, ENHORABUENA!!",Toast.LENGTH_LONG).show()

                    //Cambiar la imagen
                    mibinding.resultadoImagen.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.baseline_tag_faces_24))
                    //Reinicie el juego
                    comenzar_aplicacion(false)
                    temporizador.cancel()
                }
                else
                    if(juego.puntos<=0)
                    {
                        //Ha perdido
                        //Salte mensaje ganador
                        Toast.makeText(this,"HAS PERDIDO, LO SIENTO!!",Toast.LENGTH_LONG).show()

                        //Cambiar la imagen
                        mibinding.resultadoImagen.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.baseline_face_retouching_off_24))
                        //Reinicie el juego
                        comenzar_aplicacion(false)
                        temporizador.cancel()

                    }

                //generar una nueva palabra
                palabragenerada=juego.obtener_Palabra()
                //transformarla
                num_funcion_transf=Random.nextInt(1..lista_funciones.size-1)
                var descolocado:Boolean=false
                if(num_funcion_transf==1)
                    descolocado=true

                mibinding.palabraModificadaText.text=palabragenerada.transformar(descolocado,lista_funciones.get(num_funcion_transf).f)

            }



        }




    }

    private fun comenzar_aplicacion(inicio:Boolean) {

        mibinding.palabraModificadaText.text=""
        mibinding.pistaTextview.text=""

        //Inhabilito el editext
        mibinding.palabraTextview.tag=mibinding.palabraTextview.keyListener
        mibinding.palabraTextview.keyListener=null

        mibinding.palabraTextview.hint="Introduce Palabra"
        mibinding.comprobarBoton.isEnabled=false
        mibinding.relojTextview.text=tiempo_inicial
        mibinding.puntosTextview.text=juego.puntos.toString()
        if(inicio)
            mibinding.resultadoImagen.setImageDrawable(null)
        mibinding.jugarBoton.isEnabled=true


    }
}