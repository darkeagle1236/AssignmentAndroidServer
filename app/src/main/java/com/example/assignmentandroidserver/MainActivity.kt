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
import com.example.assignmentandroidserver.network.API
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_product.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rcvProducts.layoutManager = LinearLayoutManager(this)
        getAllProduct()

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

    private fun showError(errorMsg: String) {

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
