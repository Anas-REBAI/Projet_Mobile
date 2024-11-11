package tn.esprit.nascar.models


data class User(
    val name: String,
    val phone: String
)

data class findedUser(
    val _id: String,
    val name: String,
    val phone: String,
    val __v: Int
)

data class Product(
    val _id: String,
    val name: String,
    val description: String,
    val image: String,
    val code: String,
    val carbonFootPrint: Number,
    val waterConsumption: Number,
    val recyclability: Number,
    val category: String,
    val brand: String,
    val price: Number,
    val stock: Number,
)

data class Historique(
    val _id: String,
    val userId: String,
    val productId: Product,
    val date: String
)

