package tn.esprit.nascar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {

    private lateinit var txtFullName: TextView
    private lateinit var txtPhone: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialiser les TextView
        txtFullName = view.findViewById(R.id.txtFullName)
        txtPhone = view.findViewById(R.id.txtEmail)

        // Charger les données de SharedPreferences
        loadUserFromPreferences()

        return view
    }


    private fun loadUserFromPreferences() {
        val sharedPref = activity?.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        val userName = sharedPref?.getString("userName", "Name not found")
        val userPhone = sharedPref?.getString("userPhone", "Phone not found")

        // Afficher les données dans les TextView
        txtFullName.text = userName
        txtPhone.text = userPhone
    }

}