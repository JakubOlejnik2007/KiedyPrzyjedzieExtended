package com.example.kiedyprzyjedzieextended.ui.map

import android.annotation.SuppressLint
import com.example.kiedyprzyjedzieextended.R
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kiedyprzyjedzieextended.databinding.FragmentMapBinding
import com.example.kiedyprzyjedzieextended.helpers.convertJsonToStopArray
import com.example.kiedyprzyjedzieextended.helpers.fetchStopsJSONData
import com.example.kiedyprzyjedzieextended.types.Stop
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.views.MapView
import org.osmdroid.util.GeoPoint
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!


    val map by lazy{ binding.map }

    private var currentInfoWindow: InfoWindow? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root
        map.setTileSource(TileSourceFactory.MAPNIK)

        val ctx: Context = requireContext()
        Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

        val startPoint = GeoPoint(52.415864,21.1786338)
        map.controller.setZoom(15.0)
        map.controller.setCenter(startPoint)
        map.setMultiTouchControls(true)

        getStopsList()

        return root
    }
    private fun getStopsList() {
        val data = fetchStopsJSONData()
        data.observe(viewLifecycleOwner) { result ->
            val resArray = convertJsonToStopArray(result)
            addMarkersToMap(resArray.toList())
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    fun addMarkersToMap(stops: List<Stop>) {
        val drawable = resources.getDrawable(R.drawable.bus_stop_icon2, null)
        val coloredDrawable = setColorFilter(drawable, resources.getColor(R.color.mazarine_blue)) // Możesz zmienić Color.RED na dowolny kolor

        for (stop in stops) {
            val marker = Marker(map)
            marker.position = GeoPoint(stop.lat/1000000, stop.lon/1000000)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = stop.stopName
            marker.icon = coloredDrawable
            marker.infoWindow = CustomInfoWindow(map)
            map.overlays.add(marker)
            val customInfoWindow = CustomInfoWindow(map)
            marker.infoWindow = customInfoWindow

            marker.setOnMarkerClickListener { m, _ ->
                if (currentInfoWindow != null) {
                    currentInfoWindow!!.close()
                }
                currentInfoWindow = customInfoWindow
                marker.showInfoWindow()
                true
            }
            marker.infoWindow = customInfoWindow
        }
        map.invalidate()
    }
    fun setColorFilter(drawable: Drawable, color: Int): Drawable {
        val wrappedDrawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(wrappedDrawable, color)
        return wrappedDrawable
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }
}

class CustomInfoWindow(mapView: MapView) : InfoWindow(R.layout.custom_info, mapView) {

    override fun onOpen(item: Any?) {
        val marker = item as Marker
        val titleView = mView.findViewById<TextView>(R.id.title)
        val descriptionView = mView.findViewById<TextView>(R.id.description)

        titleView.text = marker.title
        descriptionView.text = marker.snippet

    }

    override fun onClose() {
        // Możesz dodać kod, który powinien być wykonany, gdy chmurka jest zamykana
    }
}