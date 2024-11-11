package tn.esprit.nascar

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.nascar.adapters.FavoritesAdapter
import tn.esprit.nascar.models.Historique
import tn.esprit.nascar.utils.ApiInterface

class EventsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var favoritesAdapter: FavoritesAdapter

    private lateinit var edtProductName: EditText
    private lateinit var triHistory: Spinner
    private var userHistory: MutableList<Historique> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_events, container, false)
        recyclerView = view.findViewById(R.id.rvFavorites)
        recyclerView.layoutManager = LinearLayoutManager(context)

        edtProductName = view.findViewById(R.id.edtProductName)
        triHistory = view.findViewById(R.id.triHistory)

        // Initialiser l'adaptateur avec une liste vide
        favoritesAdapter = FavoritesAdapter(userHistory)
        recyclerView.adapter = favoritesAdapter


        // get the history of user
        fetchUserFavorites()

        // Ajouter ItemTouchHelper pour la suppression par glissement
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                favoritesAdapter.removeItem(position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)


        // ***************************** SPINNER ************************************************************
        // Configurer l'adaptateur pour les options du Spinner
        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.sort_options, android.R.layout.simple_spinner_item )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        triHistory.adapter = adapter

        // rendre "EditText" invisible
        edtProductName.visibility = View.GONE

        // Écouter les sélections du Spinner
        triHistory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val selectedOption = parentView?.getItemAtPosition(position).toString()

                // Mettre à jour l'affichage en fonction de l'option sélectionnée
                when (selectedOption) {
                    "All" -> {
                        edtProductName.visibility = View.GONE
                        fetchUserFavorites()
                    }
                    "Date Ascending" -> {
                        edtProductName.visibility = View.GONE
                        sortHistoryByDateAscending()
                    }
                    "Date Descending" -> {
                        edtProductName.visibility = View.GONE
                        sortHistoryByDateDescending()
                    }
                    "Product name" -> {
                        edtProductName.visibility = View.VISIBLE

                        edtProductName.addTextChangedListener(object : TextWatcher {
                            override fun afterTextChanged(s: Editable?) {
                                val query = s.toString().toLowerCase()
                                filterHistoryByProductName(query)
                            }
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                        })
                    }

                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

        return view
    }


    private fun fetchUserFavorites() {
        val sharedPref = activity?.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        val userId = sharedPref?.getString("userId", "") ?: ""

        val apiInterface = ApiInterface.create()
        apiInterface.getUserHistory(userId).enqueue(object : Callback<List<Historique>> {
            override fun onResponse(call: Call<List<Historique>>, response: Response<List<Historique>>) {
                if (response.isSuccessful) {
                    val userHistory = response.body() ?: emptyList()
                    favoritesAdapter = FavoritesAdapter(userHistory.toMutableList())
                    recyclerView.adapter = favoritesAdapter
                } else {
                    Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Historique>>, t: Throwable) {
                Toast.makeText(context, "Failed to connect to server", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun sortHistoryByDateAscending() {
        userHistory.sortBy { it.date }
        favoritesAdapter.notifyDataSetChanged()
    }

    private fun sortHistoryByDateDescending() {
        userHistory.sortByDescending { it.date }
        favoritesAdapter.notifyDataSetChanged()
    }

    private fun filterHistoryByProductName(productName: String) {
        val filteredList = userHistory.filter { it.productId.name.contains(productName, ignoreCase = true) }
        favoritesAdapter.updateList(filteredList.toMutableList())
    }



}