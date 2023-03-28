package com.example.zadanie_algorytmika_na_miesiac

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Ilosc znakow i wzorzec
        val iloscZnakow = findViewById<EditText>(R.id.IloscZnakow)
        val wzorzec = findViewById<EditText>(R.id.PodajWzorzec)

        //Sprawdz
        val Sprawdz = findViewById<Button>(R.id.Sprawdz)

        //Wyniki
        val BruteCzas = findViewById<TextView>(R.id.BruteForceCzas)
        val KMPCzas = findViewById<TextView>(R.id.KMPCzas)
        val BmCzas = findViewById<TextView>(R.id.BmCzas)
        val RkCzas = findViewById<TextView>(R.id.RkCzas)

        Sprawdz.setOnClickListener {
            if(iloscZnakow.text.toString() == "" || wzorzec.text.toString() == "")
                return@setOnClickListener
            val tekst = wylosujTekst(iloscZnakow?.text.toString().toInt())
            Toast.makeText(applicationContext, tekst, Toast.LENGTH_LONG).show()

        }
    }
    fun wylosujTekst(ilosc: Int): String{
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0' .. '9')// lista znaków, z których będzie losowany ciąg
        return (1..ilosc)
            .map { Random.nextInt(0, charPool.size) } // losowanie indeksów ze zbioru znaków
            .map(charPool::get) // pobieranie znaków o wylosowanych indeksach z listy charPool
            .joinToString("")
    }
}