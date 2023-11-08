package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CartActivity
import com.example.myapplication.CartItemClickListener
import com.example.myapplication.R
import com.example.myapplication.database.ShoesDB
import com.example.myapplication.entity.Cart
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartAdapter(private val myListener: CartItemClickListener) : RecyclerView.Adapter<CartAdapter.FoodHolder>()  {

    var listener: ShoesAdapter.OnItemClickListener? = null
    var ctx: Context? = null
    var arrCarts = ArrayList<Cart>()

    class FoodHolder(view: View): RecyclerView.ViewHolder(view){

    }

    fun setData(arrData : List<Cart>){
        arrCarts = arrData as ArrayList<Cart>
    }

    fun setClickListener(listener1: ShoesAdapter.OnItemClickListener){
        listener = listener1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        ctx = parent.context
        return FoodHolder(LayoutInflater.from(parent.context).inflate(R.layout.checkout_card_element_layout,parent,false))
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {

        val shoesNameTextView = holder.itemView.findViewById<TextView>(R.id.shoesName)
        val shoesPrice = holder.itemView.findViewById<TextView>(R.id.shoesPrice)
        val shoesQuantity = holder.itemView.findViewById<TextView>(R.id.quantityText)

        shoesNameTextView.text = arrCarts[position].shoesAdded.shoesName
        shoesPrice.text = "Ár: " + arrCarts[position].shoesAdded.price.toString() + "Ft"
        shoesQuantity.text = arrCarts[position].quantity.toString()


        val imageView = holder.itemView.findViewById<ImageView>(R.id.shoesPicture)
        val imageUrl = arrCarts[position].shoesAdded.picture
        Picasso.get().load(imageUrl).into(imageView)

        val minusBtn = holder.itemView.findViewById<Button>(R.id.minusQuantity)
        val plusBtn = holder.itemView.findViewById<Button>(R.id.plusQuantity)
        val deleteItem = holder.itemView.findViewById<Button>(R.id.deleteItem)

        deleteItem.setOnClickListener { view ->
            CoroutineScope(Dispatchers.IO).launch {
                val cartDao = ShoesDB.getDatabase(view.context).getCartDao()
                cartDao.deleteItem(arrCarts[position])

                (view.context as? CartActivity)?.runOnUiThread {
                    myListener.onItemDelete(arrCarts[position])
                    arrCarts.removeAt(position)
                    notifyDataSetChanged()
                }
            }
        }

        minusBtn.setOnClickListener { view ->
            CoroutineScope(Dispatchers.IO).launch {
                val cartDao = ShoesDB.getDatabase(view.context).getCartDao()
                val cartItem = cartDao.getCartItem(arrCarts[position].id)

                cartItem.quantity--
                cartDao.updateQuantity(cartItem)
                arrCarts[position].quantity--

                (view.context as? CartActivity)?.runOnUiThread {
                    if(cartItem.quantity<=0){
                        CoroutineScope(Dispatchers.IO).launch {

                            cartDao.deleteItem(arrCarts[position])

                            (view.context as? CartActivity)?.runOnUiThread {
                                myListener.onItemQuantityMinus(cartItem.shoesAdded.price)
                                arrCarts.removeAt(position)
                                notifyDataSetChanged()
                            }
                        }
                    }else{
                        shoesQuantity.text = cartItem.quantity.toString()
                        myListener.onItemQuantityMinus(cartItem.shoesAdded.price)
                    }
                }
            }
        }

        plusBtn.setOnClickListener { view ->
            CoroutineScope(Dispatchers.IO).launch {
                val cartDao = ShoesDB.getDatabase(view.context).getCartDao()
                val cartItem = cartDao.getCartItem(arrCarts[position].id)

                cartItem.quantity++
                cartDao.updateQuantity(cartItem)
                arrCarts[position].quantity++

                (view.context as? CartActivity)?.runOnUiThread {
                    shoesQuantity.text = cartItem.quantity.toString()
                    myListener.onItemQuantityPlus(cartItem.shoesAdded.price)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return arrCarts.size
    }

    fun clear() {
        arrCarts.clear()
        notifyDataSetChanged()
    }

}