/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package utxj.ddi.fgv.practica06.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import utxj.ddi.fgv.practica06.R
import utxj.ddi.fgv.practica06.presentation.theme.Practica06Theme


class MainActivity : ComponentActivity() {
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null
    private var mHandler: Handler? = null
    private var mRunnable: Runnable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
                    // Aquí obtienes la ubicación actual
                    val latitude = location.latitude
                    val longitude = location.longitude
                    // Haz lo que necesites con la ubicación
                    val saludosTextView = findViewById<TextView>(utxj.ddi.fgv.practica06.R.id.Saludo)
                    val lolTextView = findViewById<TextView>(utxj.ddi.fgv.practica06.R.id.lol)

                    saludosTextView.text = "Latitud: $latitude"
                    lolTextView.text = "Longitud: $longitude"
                }
            }
        }

        mHandler = Handler()
        mRunnable = Runnable {
            /*updateTime()
            mRunnable?.let { mHandler?.postDelayed(it, 1000) }*/ // Actualiza cada segundo
        }

    }

    /*private fun updateTime() {
        val timeTextView = findViewById<TextView>(R.id.lol) // Reemplaza con el ID de tu TextView
        val saludoText = findViewById<TextView>(R.id.Saludo) //Reemplaza saludo
        val maniana: String = SimpleDateFormat("05:59:59", Locale.getDefault()).format(Date()) //Mañana
        val tarde: String = SimpleDateFormat("11:59:59", Locale.getDefault()).format(Date()) //Tarde
        val noche: String = SimpleDateFormat("18:59:59", Locale.getDefault()).format(Date()) //Noche
        val madrugada: String = SimpleDateFormat("00:59:59", Locale.getDefault()).format(Date()) //Madrugada


        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()) //Variable que guarda el tiempo

        timeTextView.text = currentTime //La wea pone tiempo

        when(currentTime) { //El switch que va hacer la wea
            in maniana..tarde -> saludoText.text = "Buenos dias" //05:59:59-11:59:59
            in tarde..noche -> saludoText.text = "Buenas tardes" //11:59:59-18:59:59
            in noche..madrugada -> saludoText.text = "Buenas noches" //18:59:59-00:00:00
            in madrugada..maniana -> saludoText.text = "Buenas madrugadas" //00:00:00-05:59:59
            else -> saludoText.text = "la wea no cambio" //Si la wea fallo
        }
    }*/
    override fun onResume() {
        super.onResume();
        startLocationUpdates();
        mRunnable?.let { mHandler?.post(it) }
    }

    override fun onPause() {
        super.onPause();
        stopLocationUpdates();
        mRunnable?.let { mHandler?.removeCallbacks(it) }
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            val locationRequest: LocationRequest = LocationRequest.create()
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            locationRequest.setInterval(5000) // Intervalo en milisegundos para obtener actualizaciones de ubicación
            fusedLocationClient!!.requestLocationUpdates(locationRequest, locationCallback, null)
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient!!.removeLocationUpdates(locationCallback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            }
        }
    }
}

@Composable
fun WearApp(greetingName: String) {
    Practica06Theme {
        /* If you have enough items in your list, use [ScalingLazyColumn] which is an optimized
         * version of LazyColumn for wear devices with some added features. For more information,
         * see d.android.com/wear/compose.
         */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center
        ) {
            Greeting(greetingName = greetingName)
        }
    }
}

@Composable
fun Greeting(greetingName: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(R.string.hello_world, greetingName)
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}