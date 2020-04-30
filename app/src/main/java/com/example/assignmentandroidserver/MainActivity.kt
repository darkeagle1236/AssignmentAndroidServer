package com.example.assignmentandroidserver

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentandroidserver.model.GetAllProductReponseItem
import com.example.assignmentandroidserver.network.API
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_product.view.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {
    var username:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        username = intent.getStringExtra("username")
        rcvProducts.layoutManager = LinearLayoutManager(this)
        getAllProduct()

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.cart){
            var i = Intent(this,CartActivity::class.java)
            i.putExtra("username",username)
            startActivity(i)
        }
        return super.onOptionsItemSelected(item)
    }
    private fun getAllProduct() {
        API.service.getAllProduct()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                rcvProducts.adapter = MainAdapter(result,this)
            }, { ex ->
                showError(ex.toString())
            })
    }
    private fun addItemToCart(username:String,productName:String,productType:String,price:Int) {
        API.service.addItemToCart(username,productName,productType,price)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                Toast.makeText(this,"Thêm vào giỏ hàng thành công !",Toast.LENGTH_SHORT)
            }, { ex ->
                showError(ex.toString())
            })
    }

    private fun showError(errorMsg: String) {
        Toast.makeText(this,errorMsg,Toast.LENGTH_SHORT)
    }

    inner class MainAdapter(
        var productList: List<GetAllProductReponseItem>,
        context: Context
    ) :
        RecyclerView.Adapter<MainAdapter.RepositoryHolder>() {
        var context: Context = context
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RepositoryHolder {
            val v: View =
                LayoutInflater.from(context)
                    .inflate(R.layout.item_product, parent, false)
            return RepositoryHolder(v)
        }

        override fun onBindViewHolder(
            holder: RepositoryHolder,
            position: Int
        ) {
            holder.tvProductName.text = productList[position].productName
            holder.tvProductType.text = productList[position].productType
            holder.tvProductPrice.text = productList[position].price
            holder.ivAddToCart.setOnClickListener(View.OnClickListener {
                addItemToCart(username,productList[position].productName,productList[position].productType,Integer.parseInt(productList[position].price))
            })
        }

        override fun getItemCount(): Int {
            return productList.size
        }

        inner class RepositoryHolder(v: View) : RecyclerView.ViewHolder(v) {
            var tvProductName: TextView = v.tvProductName
            var tvProductPrice: TextView = v.tvPrice
            var tvProductType: TextView = v.tvProductType
            var ivAddToCart:ImageView = v.ivAdd
            var view = v
        }

    }
}
