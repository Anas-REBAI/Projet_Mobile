package tn.esprit.nascar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.nascar.R
import com.bumptech.glide.Glide
import tn.esprit.nascar.models.Product

class ProductsAdapter (private val favoritesList: List<Product>) :
    RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {

    // Tableau d'images statiques
    private val imageResources = listOf(
        R.drawable.bouteille,
        R.drawable.olive,
        R.drawable.shirt,
        R.drawable.velo,
        R.drawable.sacdos,
        R.drawable.chaussure,
        R.drawable.blutooth,
        R.drawable.montre,
        R.drawable.lunettes,
        R.drawable.pc,
        R.drawable.sacdos,
        R.drawable.aspirateur,
        R.drawable.maillot,
    )

    class ProductsViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.newsImage)
        val productName: TextView = itemView.findViewById(R.id.newsTitle)
        val productDescription: TextView = itemView.findViewById(R.id.newsDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.single_item_news, parent, false)
        return ProductsViewHolder(itemView)
    }

    override fun getItemCount() = favoritesList.size

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val currentItem = favoritesList[position]

        // Utiliser Glide pour charger les images
        //Glide.with(holder.itemView.context)
            //.load(currentItem.image)
           // .into(holder.productImage)

        // Assigner une image statique à partir du tableau
        val imageResource = imageResources[position % imageResources.size]
        holder.productImage.setImageResource(imageResource)

        holder.productName.text = currentItem.name
        holder.productDescription.text = currentItem.description
    }

}