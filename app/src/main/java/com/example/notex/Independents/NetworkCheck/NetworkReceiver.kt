import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.navigation.NavController
import com.example.notex.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class NetworkReceiver(private val navController: NavController,
                      private val bottomNav: BottomNavigationView
) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (isConnected(context)) {
            bottomNav.menu.findItem(R.id.noteFragment).isEnabled = true
            bottomNav.menu.findItem(R.id.homeFragment).isEnabled = true
            bottomNav.menu.findItem(R.id.categorieFragment).isEnabled = true
            bottomNav.menu.findItem(R.id.profileFragment2).isEnabled = true
        } else {
            bottomNav.menu.findItem(R.id.noteFragment).isEnabled = true
            bottomNav.menu.findItem(R.id.homeFragment).isEnabled = false
            bottomNav.menu.findItem(R.id.categorieFragment).isEnabled = false
            bottomNav.menu.findItem(R.id.profileFragment2).isEnabled = false

            var current = navController.currentDestination

            if (current?.id != R.id.noInternetFragment || current.id != R.id.noteFragment ||current.id != R.id.updateNoteFragment ||
                current.id != R.id.newNoteFragment ) {
                navController.navigate(R.id.noInternetFragment)
            }
        }
    }

    private fun isConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}
