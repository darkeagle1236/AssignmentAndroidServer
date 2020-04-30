package com.example.assignmentandroidserver

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentandroidserver.model.GetAllProductReponseItem
import com.example.assignmentandroidserver.model.GetCartByIdResponseItem
import com.example.assignmentandroidserver.network.API
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.item_cart.view.*
import kotlinx.android.synthetic.main.item_product.view.*
import kotlinx.android.synthetic.main.item_product.view.ivAdd
import kotlinx.android.synthetic.main.item_product.view.tvPrice
import kotlinx.android.synthetic.main.item_product.view.tvProductName
import kotlinx.android.synthetic.main.item_product.view.tvProductType
import kotlinx.android.synthetic.main.toolbar.*

class CartActivity : AppCompatActivity() {
    var username:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        username = intent.getStringExtra("username")
        title = "Cart"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        rcvCart.layoutManager = LinearLayoutManager(this)
        getCartById()
    }

    private fun getCartById() {
        API.service.getAllCartItem(username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                rcvCart.adapter = CartAdapter(result,this)
            }, { ex ->
                println(ex.printStackTrace())
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    inner class CartAdapter(
        var productList: List<GetCartByIdResponseItem>,
        context: Context
    ) :
        RecyclerView.Adapter<CartAdapter.RepositoryHolder>() {
        var context: Context = context
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RepositoryHolder {
            val v: View =
                LayoutInflater.from(context)
                    .inflate(R.layout.item_cart, parent, false)
            return RepositoryHolder(v)
        }

        override fun onBindViewHolder(
            holder: RepositoryHolder,
            position: Int
        ) {
            holder.tvProductName.text = productList[position].productName
            holder.tvProductType.text = productList[position].productType
            holder.tvProductPrice.text = productList[position].price.toString()
            holder.tvProductQuantity.text = productList[position].quantity.toString()
            holder.ivAddToCart.setOnClickListener(View.OnClickListener {
//                addItemToCart(username,productList[position].productName,productList[position].productType,Integer.parseInt(productList[position].price))
            })
        }

        override fun getItemCount(): Int {
            return productList.size
        }

        inner class RepositoryHolder(v: View) : RecyclerView.ViewHolder(v) {
            var tvProductName: TextView = v.tvProductName
            var tvProductPrice: TextView = v.tvPrice
            var tvProductType: TextView = v.tvProductType
            var tvProductQuantity:TextView = v.tvProductQuantity
            var ivAddToCart: ImageView = v.ivAdd
            var view = v
        }

    }
}
