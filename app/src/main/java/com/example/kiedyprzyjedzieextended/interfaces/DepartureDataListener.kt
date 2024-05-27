import com.example.kiedyprzyjedzieextended.types.Departure

interface DepartureDataListener {
    fun onDepartureDataChanged(departures: List<Departure>)
}