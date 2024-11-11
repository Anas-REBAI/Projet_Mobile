package tn.esprit.nascar.adapters

import android.view.LayoutInflater
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.nascar.R
import com.bumptech.glide.Glide
import tn.esprit.nascar.ProductDetailsActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.nascar.models.Historique
import tn.esprit.nascar.utils.ApiInterface

class FavoritesAdapter(private var favoritesList: MutableList<Historique>) :
    RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    // Tableau d'images statiques
    private val imageResources = listOf(
        R.drawable.bouteille,
        R.drawable.olive,
        R.drawable.sacdos,
        R.drawable.blutooth,
        R.drawable.montre,
    )


    class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.ivProductImage)
        val productName: TextView = itemView.findViewById(R.id.tvProductName)
        val historyDate: TextView = itemView.findViewById(R.id.tvDateHistory)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return FavoritesViewHolder(itemView)
    }

    override fun getItemCount() = favoritesList.size

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val currentItem = favoritesList[position]

        // Utiliser Glide pour charger les images
        //Glide.with(holder.itemView.context)
           // .load(currentItem.productId.image)
            //.into(holder.productImage)

        // Assigner une image statique à partir du tableau
        val imageResource = imageResources[position % imageResources.size]
        holder.productImage.setImageResource(imageResource)

        holder.productName.text = currentItem.productId.name
        holder.historyDate.text = "Date: ${currentItem.date}"

        // clic d'un produit
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("productId", currentItem.productId._id)
            context.startActivity(intent)
        }

    }

    // suppression du produit from recycler
    fun removeItem(position: Int) {
        val item = favoritesList[position]
        favoritesList.removeAt(position)
        notifyItemRemoved(position)

        val apiInterface = ApiInterface.create()
        apiInterface.deleteFromHistory(item._id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    //Toast.makeText(itemView.context, "Item deleted", Toast.LENGTH_SHORT).show()
                } else {
                   //Toast.makeText(itemView.context, "Failed to delete item", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                //Toast.makeText(itemView.context, "Failed to connect to server", Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun updateList(newList: MutableList<Historique>) {
        favoritesList = newList
        notifyDataSetChanged()
    }



}