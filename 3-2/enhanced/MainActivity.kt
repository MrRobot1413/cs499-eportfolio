import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// --- Data Model ---
data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
    val current_price: Double
)

// --- Retrofit API ---
interface CoinApi {
    @GET("coins/markets?vs_currency=usd&order=market_cap_desc&per_page=10&page=1&sparkline=false")
    suspend fun getCoins(): List<Coin>
}

object RetrofitService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.coingecko.com/api/v3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: CoinApi = retrofit.create(CoinApi::class.java)
}

// --- ViewModel ---
class CoinViewModel {
    var coins by mutableStateOf<List<Coin>>(emptyList())
        private set

    private val scope = CoroutineScope(Dispatchers.Main)

    fun fetchCoins() {
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching { RetrofitService.api.getCoins() }
            }
            result.onSuccess { coins = it }.onFailure { coins = emptyList() }
        }
    }
}

// --- UI ---
@Composable
fun CoinListScreen(viewModel: CoinViewModel) {
    val coins = viewModel.coins
    LaunchedEffect(true) { viewModel.fetchCoins() }

    Scaffold(topBar = { TopAppBar(title = { Text("Crypto Tracker") }) }) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(coins) { coin ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = coin.name, style = MaterialTheme.typography.titleMedium)
                        Text(text = "${coin.symbol.uppercase()} - $${coin.current_price}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

// --- Main Activity ---
class MainActivity : ComponentActivity() {
    private val viewModel = CoinViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CoinListScreen(viewModel) }
    }
}
