package tn.esprit.nascar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.nascar.models.Product
import tn.esprit.nascar.utils.ApiInterface

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var ivProductDetails: ImageView
    private lateinit var tvNameDetails: TextView
    private lateinit var tvDescriptionDetails: TextView
    private lateinit var tvCarbonFootprint: TextView
    private lateinit var tvWaterConsumption: TextView
    private lateinit var tvRecyclability: TextView
    private lateinit var tvBrand: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvStock: TextView
    private lateinit var tvCategory: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        ivProductDetails = findViewById(R.id.iv_image_product_details)
        tvNameDetails = findViewById(R.id.tv_name_details)
        tvDescriptionDetails = findViewById(R.id.tv_productDescription)
        tvCarbonFootprint = findViewById(R.id.tv_carbonFootprint)
        tvWaterConsumption = findViewById(R.id.tv_waterConsumption)
        tvRecyclability = findViewById(R.id.tv_recyclability)
        tvBrand = findViewById(R.id.tv_brand)
        tvPrice = findViewById(R.id.tv_price)
        tvStock = findViewById(R.id.tv_stock)
        tvCategory = findViewById(R.id.tv_category)


        val productId = intent.getStringExtra("productId")
        fetchProductDetails(productId)

    }


    private fun fetchProductDetails(productId: String?) {
        if (productId == null) {
            finish() // Ferme l'activité si productId est nul
            return
        }

        val apiInterface = ApiInterface.create()
        apiInterface.getProductById(productId).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    val product = response.body()
                    if (product != null) {
                        displayProductDetails(product)
                    }
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Toast.makeText(this@ProductDetailsActivity, "Connexion error!", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun displayProductDetails(product: Product) {
        Glide.with(this)
            .load(product.image)
            .into(ivProductDetails)

        tvNameDetails.text = product.name
        tvDescriptionDetails.text = product.description
        tvCarbonFootprint.text = "${product.carbonFootPrint} kg"
        tvWaterConsumption.text = "${product.waterConsumption} L"
        tvRecyclability.text = "${product.recyclability} %"
        tvBrand.text = product.brand
        tvPrice.text = "${product.price} USD"
        tvStock.text = "${product.stock}"
        tvCategory.text = "Category: ${product.category}"
    }

}