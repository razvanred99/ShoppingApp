package com.handstandsam.shoppingapp.features.checkout

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.handstandsam.shoppingapp.MyApplication
import com.handstandsam.shoppingapp.R
import com.handstandsam.shoppingapp.cart.ShoppingCart
import com.handstandsam.shoppingapp.features.home.ColorInts
import com.handstandsam.shoppingapp.features.itemdetail.ItemDetailActivity
import com.handstandsam.shoppingapp.features.itemdetail.ItemDetailPresenter
import com.handstandsam.shoppingapp.graph
import com.handstandsam.shoppingapp.models.ItemWithQuantity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


internal class CheckoutItemRowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    CoroutineScope by CoroutineScope(Dispatchers.Default) {

    private val textView: TextView = itemView.findViewById(R.id.text)

    private val imageView: AppCompatImageView = itemView.findViewById(R.id.image)
    private val addButton: AppCompatImageView = itemView.findViewById(R.id.add_button)
    private val removeButton: AppCompatImageView = itemView.findViewById(R.id.remove_button)

    private val shoppingCart: ShoppingCart
        get() {
            val application = (itemView.context.applicationContext as MyApplication)
            return application.graph().sessionGraph.shoppingCart
        }

    private var _itemWithQuantity: ItemWithQuantity? = null

    init {
        itemView.setOnClickListener { view ->
            val context = itemView.context
            val intent = Intent(view.context, ItemDetailActivity::class.java)
            val extras = Bundle()
            extras.putSerializable(ItemDetailPresenter.BUNDLE_PARAM_ITEM, _itemWithQuantity?.item)
            intent.putExtras(extras)
            context.startActivity(intent)
        }
        addButton.setOnClickListener {
            launch {
                shoppingCart.incrementItemInCart(_itemWithQuantity!!.item)
            }
        }
        removeButton.setOnClickListener {
            launch {
                shoppingCart.decrementItemInCart(_itemWithQuantity!!.item)
            }
        }
    }

    fun bindData(itemWithQuantity: ItemWithQuantity, position: Int) {
        this._itemWithQuantity = itemWithQuantity

        val imageUrl = itemWithQuantity.item.image
        if (!imageUrl.isEmpty()) {
            Glide.with(imageView.context).load(imageUrl).into(imageView)
        } else {
            itemView.setBackgroundResource(ColorInts.getColor(position))
        }

        textView.text = itemWithQuantity.quantity.toString() + " x " + itemWithQuantity.item.label
    }
}
