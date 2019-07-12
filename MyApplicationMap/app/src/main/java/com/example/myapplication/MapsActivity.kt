package com.example.myapplication

//debemos implementar los servicio de localizacion en mbuild.glade(module:app)
//implementation 'com.google.android.gms:play-services-location:17.0.0'
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

//GoogleMap.OnMapClickListener para poder utilizar marcadores
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    //intanciamos clase que nos permite acceder a la localizacion
    //es para no darle un valor
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    //propiedad de la utima ubicacion del usuario
    private lateinit var lastLocation: Location

    //permisos de localizacion
    //puede ser cualqueir numero para identificar
    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    //retorna false por que nuestras clases van a hacer todo
    override fun onMarkerClick(p0: Marker?) = false

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap


        //se quita esto para poder dibujar un marcador nuestro
        /*// Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/

        //this por que esta actividad es que la que se va a encargar de los mapas
        map.setOnMarkerClickListener(this)

        //habilitar elacercacmiento y alejamiento
        map.uiSettings.isZoomControlsEnabled = true

        setUpMap()
    }


    //funcion para cambiar estilo de marker
    private fun placeMarket(location: LatLng){
        val markerOptions = MarkerOptions().position(location)

        //cambiar formas o colores
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))

        //agregamos marker al mapa)
        map.addMarker(markerOptions)
    }


    //esta funcion hace qu etengamos un permiso en especifico
    //permisos de locacion/ubicacion
    private fun setUpMap(){
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //si no tebemos elpermis lo pedimos
            //podemos pedir mas de 1 con arrayofb
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        //codigo para desplegar nuestra ubicacion
        //añade capa para poner el punto de nuestra ubicacion
        map.isMyLocationEnabled = true

        //cambiar forma de mapa
        //depende por completo de la informacion de google
        map.mapType = GoogleMap.MAP_TYPE_HYBRID

        fusedLocationClient.lastLocation.addOnSuccessListener(this) {
            location ->  //cuando ya exista una llamada de localizacion se ejecuta lo siguiente
            if (location != null){
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)

                //llamos a la funcion de nuestro marker nuevo
                placeMarket(currentLatLng)

                //y luego pasamos a nuestro mapa para mover la camara
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13f))
            }
        }
    }
}
