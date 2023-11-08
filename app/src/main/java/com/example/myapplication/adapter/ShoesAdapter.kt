package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.dao.CartDao
import com.example.myapplication.database.ShoesDB
import com.example.myapplication.entity.Cart
import com.example.myapplication.entity.Shoes
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoesAdapter : RecyclerView.Adapter<ShoesAdapter.RecipeViewHolder>() {

    var listener: OnItemClickListener? = null
    var ctx: Context? = null
    var arrShoes = ArrayList<Shoes>()

    class RecipeViewHolder(view: View): RecyclerView.ViewHolder(view){

    }

    fun setData(arrData : List<Shoes>){
        arrShoes = arrData as ArrayList<Shoes>
    }

    fun setClickListener(listener1: OnItemClickListener){
        listener = listener1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        ctx = parent.context
        return RecipeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cardelement_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return arrShoes.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {

        val shoesNameTextView = holder.itemView.findViewById<TextView>(R.id.shoesName)
        val shoesDesc = holder.itemView.findViewById<TextView>(R.id.shoesDescription)
        val shoesPrice = holder.itemView.findViewById<TextView>(R.id.shoesPrice)
        val plusButton = holder.itemView.findViewById<Button>(R.id.plusButton)

        shoesNameTextView.text = arrShoes[position].shoesName
        shoesDesc.text = arrShoes[position].description
        shoesPrice.text = "Ár: " + arrShoes[position].price.toString() + "Ft"

        val imageView = holder.itemView.findViewById<ImageView>(R.id.shoesPicture)
        val imageUrl = arrShoes[position].picture
        Picasso.get().load(imageUrl).into(imageView)

        val sharedPreferences = holder.itemView.context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("loggedInUser", "")

        if (savedUsername.isNullOrEmpty()) {
            plusButton.visibility = View.INVISIBLE
        } else {
            plusButton.visibility = View.VISIBLE

            plusButton.setOnClickListener { view ->

                val shoes = arrShoes[position]
                val cartDao = ShoesDB.getDatabase(view.context).getCartDao()

                addToCart(shoes, cartDao)

                Toast.makeText(
                    view.context,
                    "Kosárhoz adva!",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }

    }

    fun addToCart(shoes: Shoes, cartDao: CartDao) {

        CoroutineScope(Dispatchers.IO).launch {
            val existingCartItem = cartDao.checkIfAdded(shoes)

            if (existingCartItem != null) {
                existingCartItem.quantity++
                cartDao.updateQuantity(existingCartItem)
            } else {
                val newCart = Cart(0, shoes, 1)
                cartDao.insertCartItem(newCart)
            }

        }

    }

    interface OnItemClickListener{
        fun onClicked(categoryName:String)
    }

}