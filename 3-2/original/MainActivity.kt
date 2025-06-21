package com.example.cryptotracker

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
    val current_price: Double
)

interface CoinApi {
    @GET("coins/markets?vs_currency=usd&order=market_cap_desc&per_page=10&page=1&sparkline=false")
    suspend fun getCoins(): List<Coin>
}

class MainActivity : AppCompatActivity() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.coingecko.com/api/v3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val api = retrofit.create(CoinApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this)
        setContentView(textView)

        CoroutineScope(Dispatchers.Main).launch {
            val coins = withContext(Dispatchers.IO) { api.getCoins() }
            textView.text = coins.joinToString("\n") { "${it.name} - $${it.current_price}" }
        }
    }
}
