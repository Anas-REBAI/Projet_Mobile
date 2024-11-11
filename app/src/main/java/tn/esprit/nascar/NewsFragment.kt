package tn.esprit.nascar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.nascar.adapters.ProductsAdapter
import tn.esprit.nascar.models.Product
import tn.esprit.nascar.utils.ApiInterface

class NewsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var favoritesAdapter: ProductsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)
        recyclerView = view.findViewById(R.id.rvNews)
        recyclerView.layoutManager = LinearLayoutManager(context)
        fetchAllProducts()
        return view
    }


    private fun fetchAllProducts() {
        val apiInterface = ApiInterface.create()
        apiInterface.getAllProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    val productlist = response.body() ?: emptyList()
                    favoritesAdapter = ProductsAdapter(productlist)
                    recyclerView.adapter = favoritesAdapter
                } else {
                    Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(context, "Failed to connect to server", Toast.LENGTH_SHORT).show()
            }
        })
    }

}