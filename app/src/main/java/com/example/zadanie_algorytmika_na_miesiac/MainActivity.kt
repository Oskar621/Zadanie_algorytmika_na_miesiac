package com.example.zadanie_algorytmika_na_miesiac

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Ilosc znakow i wzorzec
        val iloscZnakow = findViewById<EditText>(R.id.IloscZnakow).text
        val wzorzec = findViewById<EditText>(R.id.PodajWzorzec).text

        //Sprawdz
        val Sprawdz = findViewById<Button>(R.id.Sprawdz)

        //Wyniki
        val BruteCzas = findViewById<TextView>(R.id.BruteForceCzas)
        val KMPCzas = findViewById<TextView>(R.id.KMPCzas)
        val BmCzas = findViewById<TextView>(R.id.BmCzas)
        val RkCzas = findViewById<TextView>(R.id.RkCzas)

        Sprawdz.setOnClickListener {
            if(iloscZnakow.toString() == "" || wzorzec.toString() == "")
                return@setOnClickListener
            val tekst = wylosujTekst(iloscZnakow.toString().toInt())

            var czas = measureTimeMillis {
                bruteForce(tekst.toString(),wzorzec.toString())
            }
            BruteCzas.text = String.format("%s ms",czas)

        }
    }

    //algorytm brute force
    fun bruteForce(tekst: String, wzorzec: String){
        val pozycje = mutableListOf<Int>()
        val n = tekst.length
        val m = wzorzec.length
        // Przeszukanie tekstu w poszukiwaniu wzorca
        for (i in 0..n-m) {
            var j = 0
            // Porównywanie kolejnych znaków tekstu i wzorca
            while (j < m && tekst[i+j] == wzorzec[j]) {
                j++
            }
            // Jeśli udało się dopasować cały wzorzec, dodajemy pozycję do listy
            if (j == m) {
                pozycje.add(i)
            }
        }
    }


    //Losowanie tekstu o podanej dlugosci znakow
    fun wylosujTekst(ilosc: Int): String{
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0' .. '9')// lista znaków, z których będzie losowany ciąg
        return (1..ilosc)
            .map { Random.nextInt(0, charPool.size) } // losowanie indeksów ze zbioru znaków
            .map(charPool::get) // pobieranie znaków o wylosowanych indeksach z listy charPool
            .joinToString("")
    }
}