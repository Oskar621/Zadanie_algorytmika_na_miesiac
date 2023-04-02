package com.example.zadanie_algorytmika_na_miesiac

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.example.zadanie_algorytmika_na_miesiac.databinding.ActivityMainBinding
import kotlin.math.pow
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    private lateinit var binding:  ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Sprawdz

        binding.Sprawdz.setOnClickListener {

            //Ilosc znakow i wzorzec

            if(binding.PodajWzorzec.text.toString() == "" || binding.IloscZnakow.text.toString() == "")
                return@setOnClickListener
            val tekst = wylosujTekst(binding.IloscZnakow.text.toString().toInt())

            var czas = measureTimeMillis {
                bruteForce(tekst,binding.PodajWzorzec.text.toString())
            }
            binding.BruteForceCzas.text = String.format("%s ms", czas)

            czas = measureTimeMillis {
                KMP(tekst,binding.PodajWzorzec.text.toString())
            }
            binding.KMPCzas.text = String.format("%s ms", czas)

            czas = measureTimeMillis {
                bm(tekst,binding.PodajWzorzec.text.toString())
            }
            binding.BmCzas.text = String.format("%s ms", czas)

            czas = measureTimeMillis {
                RK(tekst, binding.PodajWzorzec.text.toString())
            }
            binding.RkCzas.text = String.format("%s ms", czas)

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

    //algorytm R-K
    fun RK(tekst: String, wzorzec: String) {
        val prime = 101 // wybieramy liczbę pierwszą jako podstawę
        val m = wzorzec.length
        val n = tekst.length
        val results = mutableListOf<Int>()
        val pHash = wzorzec.hashCode() // obliczamy hasz wzorca

        var tHash = tekst.substring(0, m).hashCode() // obliczamy hasz początkowego fragmentu tekstu
        if (tHash == pHash && tekst.substring(0, m) == wzorzec) {
            results.add(0)
        }

        val power = prime.toDouble().pow(m - 1).toInt() // obliczamy wartość prime^(m-1)

        for (i in 1..n - m) {
            // obliczamy hasz kolejnego fragmentu tekstu na podstawie poprzedniego
            tHash = prime * (tHash - tekst[i - 1].hashCode() * power) + tekst[i + m - 1].hashCode()

            // jeśli hasze się zgadzają, to porównujemy dokładnie wzorzec z tekstem
            if (tHash == pHash && tekst.substring(i, i + m) == wzorzec) {
                results.add(i)
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