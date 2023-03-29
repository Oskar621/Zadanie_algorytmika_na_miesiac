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

        //Sprawdz
        val Sprawdz = findViewById<Button>(R.id.Sprawdz)

        val BruteCzas = findViewById<TextView>(R.id.BruteForceCzas)
        val KMPCzas = findViewById<TextView>(R.id.KMPCzas)
        val BmCzas = findViewById<TextView>(R.id.BmCzas)
        val RkCzas = findViewById<TextView>(R.id.RkCzas)

        Sprawdz.setOnClickListener {
            //Ilosc znakow i wzorzec

            val iloscZnakow = findViewById<EditText>(R.id.IloscZnakow).text
            val wzorzec = findViewById<EditText>(R.id.PodajWzorzec).text



            if(iloscZnakow.toString() == "" || wzorzec.toString() == "")
                return@setOnClickListener
            val tekst = wylosujTekst(iloscZnakow.toString().toInt())

            var czas = measureTimeMillis {
                bruteForce(tekst,wzorzec.toString())
            }

            BruteCzas.text = String.format("%s ms",czas)
            czas = measureTimeMillis {
                KMP(tekst,wzorzec.toString())
            }
            KMPCzas.text = String.format("%s ms",czas)

            czas = measureTimeMillis {
                bm(tekst,wzorzec.toString())
            }
            BmCzas.text = String.format("%s ms",czas)
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

    //algorytm KMP
    fun KMP(tekst: String, wzorzec: String) {
        val pozycje = mutableListOf<Int>()
        val n = tekst.length
        val m = wzorzec.length
        // Tworzenie tablicy określającej najdłuższy prefiks-sufiks dla każdego prefiksu wzorca
        val lps = buildLPS(wzorzec)
        var i = 0  // indeks wzorca
        var j = 0  // indeks tekstu
        while (j < n) {
            if (wzorzec[i] == tekst[j]) {
                i++
                j++
                if (i == m) {
                    pozycje.add(j-m)
                    i = lps[i-1]  // przesuwamy indeks wzorca
                }
            } else if (i > 0) {
                i = lps[i-1]  // przesuwamy indeks wzorca
            } else {
                j++  // przesuwamy indeks tekstu
            }
        }
    }

    //tworzenie prefikso-sufiksow
    fun buildLPS(wzorzec: String): IntArray {
        val m = wzorzec.length
        val lps = IntArray(m)
        var len = 0
        var i = 1
        while (i < m) {
            if (wzorzec[i] == wzorzec[len]) {
                len++
                lps[i] = len
                i++
            } else if (len > 0) {
                len = lps[len-1]
            } else {
                lps[i] = 0
                i++
            }
        }
        return lps
    }

    //algorytm B-M
    fun bm(tekst: String, wzorzec: String): Int{
        val n = tekst.length
        val m = wzorzec.length

        val skip = IntArray(256) { m }

        for (i in 0 until m - 1) {
            skip[wzorzec[i].toInt()] = m - i - 1
        }

        var i = m - 1

        while (i < n) {
            var j = m - 1
            while (tekst[i] == wzorzec[j]) {
                if (j == 0) {
                    return i
                }
                i--
                j--
            }
            i += maxOf(skip[tekst[i].toInt()], m - j)
        }
        return 0
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