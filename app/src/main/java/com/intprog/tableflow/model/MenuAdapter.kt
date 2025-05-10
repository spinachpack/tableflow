package com.intprog.tableflow.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.intprog.tableflow.R
import java.text.NumberFormat
import java.util.Locale

class MenuAdapter(
    private val context: Context,
    private var items: List<OrderItem>,
    private val onQuantityChangeListener: (MenuItem, Int) -> Unit
) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "PH"))

    fun updateMenuItems(newItems: List<OrderItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.menu_item_with_quantity, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val orderItem = items[position]
        val menuItem = orderItem.menuItem

        holder.nameTextView.text = menuItem.name
        holder.priceTextView.text = currencyFormatter.format(menuItem.price)
        holder.quantityTextView.text = orderItem.quantity.toString()

        // Set description if available
        if (menuItem.description.isNotEmpty()) {
            holder.descriptionTextView.visibility = View.VISIBLE
            holder.descriptionTextView.text = menuItem.description
        } else {
            holder.descriptionTextView.visibility = View.GONE
        }

        // Don't update the UI directly - let the data model update trigger the UI update
        holder.decreaseButton.setOnClickListener {
            if (orderItem.quantity > 0) {
                val newQuantity = orderItem.quantity - 1
                onQuantityChangeListener(menuItem, newQuantity)
                // Let notifyDataSetChanged update the UI
            }
        }

        holder.increaseButton.setOnClickListener {
            val newQuantity = orderItem.quantity + 1
            onQuantityChangeListener(menuItem, newQuantity)
            // Let notifyDataSetChanged update the UI
        }

        return view
    }

    private class ViewHolder(view: View) {
        val nameTextView: TextView = view.findViewById(R.id.itemName)
        val descriptionTextView: TextView = view.findViewById(R.id.itemDescription)
        val priceTextView: TextView = view.findViewById(R.id.itemPrice)
        val quantityTextView: TextView = view.findViewById(R.id.itemCount)
        val decreaseButton: ImageView = view.findViewById(R.id.decreaseItem)
        val increaseButton: ImageView = view.findViewById(R.id.increaseItem)
    }
}