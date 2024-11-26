package com.example.adivina_palabra_examen

import android.os.Bundle
import android.os.CountDownTimer
import android.text.method.KeyListener
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
            if(p%2==0)
                ({//La posicion es par
                    c.lowercaseChar().code-'a'.code+1
                })
            else
                c

        },4,3),
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


        comenzar_aplicacion()

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
            var num=Random.nextInt(0..lista_funciones.size-1)

            var func_lambda=lista_funciones.get(num)
            var descolocar:Boolean=false
            if(num==1)
                descolocar=true


            var cadena_transformada=palabragenerada.transformar(descolocar,func_lambda.f)
            //Muestro la palabra
            mibinding.palabraModificadaText.text=cadena_transformada
            //Muestro la pista
            mibinding.pistaTextview.text=juego.obtener_Pista(num)

            //Temporizador
            temporizador=object:CountDownTimer(18000,1000){
                override fun onTick(p0: Long) {
                    var minutos=p0/60
                    var segundos=p0%60
                    var texto_segundos:String=segundos.toString()
                    if(segundos<10)
                    {
                        texto_segundos="0$segundos"
                    }
                    //Actualizar el textview que muestra el tiempo
                    mibinding.relojTextview.text="$minutos:$texto_segundos"

                }

                override fun onFinish() {
                   //Inicializamos el juego
                    comenzar_aplicacion()
                    //Poner imagen de perdido
                    mibinding.resultadoImagen.setImageDrawable(ContextCompat.getDrawable(this@MainActivity,R.drawable.ic_launcher_background))

                }

            }








        }


        mibinding.comprobarBoton.setOnClickListener {
            //Comprobar si el jugador va acertando las palabras o no
            //Incrementar y decrementar y controlar el fin del juego

            if()



        }




    }

    private fun comenzar_aplicacion() {

        mibinding.palabraModificadaText.text=""
        mibinding.pistaTextview.text=""

        //Inhabilito el editext
        mibinding.palabraTextview.tag=mibinding.palabraTextview.keyListener
        mibinding.palabraTextview.keyListener=null

        mibinding.palabraTextview.hint="Introduce Palabra"
        mibinding.comprobarBoton.isEnabled=false
        mibinding.relojTextview.text=tiempo_inicial
        mibinding.puntosTextview.text=juego.puntos.toString()
        mibinding.resultadoImagen.setImageDrawable(null)
        mibinding.jugarBoton.isEnabled=false


    }
}