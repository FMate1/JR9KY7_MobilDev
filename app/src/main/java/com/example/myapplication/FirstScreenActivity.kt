package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.ShoesAdapter
import com.example.myapplication.entity.Shoes
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FirstScreenActivity : ComponentActivity() , View.OnClickListener {

    var arrShoes = ArrayList<Shoes>()
    var shoesAdapter = ShoesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        arrShoes.add(Shoes(1, "Nike Dunk Low", 38000,"Leírás_1!", "https://www.kicksonfire.com/wp-content/uploads/2020/12/nike-dunk-low-white-black-8.jpg"))
        arrShoes.add(Shoes(2, "Adidas Samba", 40000,"Leírás_2", "https://static.footshop.com/img/p/8/1/8/2/9/0/818290.jpg"))
        shoesAdapter.setData(arrShoes)

        val recyclerView = findViewById<RecyclerView>(R.id.shoesCardElement)
        recyclerView.adapter = shoesAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        bottomNavigationHandler()

    }

    override fun onClick(p0: View?) {
        var intent = Intent(this@FirstScreenActivity, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun bottomNavigationHandler() {
        val cart = findViewById<FloatingActionButton>(R.id.cartIcon)
        cart.setOnClickListener {
            var intent = Intent(this@FirstScreenActivity, CartActivity::class.java)
            startActivity(intent)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.profile -> {
                    var intent = Intent(this@FirstScreenActivity, ProfileActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }

                else -> false
            }
        }
    }

}
