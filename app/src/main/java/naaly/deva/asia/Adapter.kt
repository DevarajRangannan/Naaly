// Adapter.kt
package naaly.deva.asia

import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

interface OnItemClickListener {
    fun onItemClick(categoryTitle: String)
}

class Adapter(
    private var list: ArrayList<String>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // View types
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_ADD_BUTTON = 1
    var selectedPosition: Int = -1
        private set


    // ViewHolder for regular items
    class ItemViewHolder(view: View, private val itemClickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener {

        var text: TextView = view.findViewById(R.id.itemText)

        init {
            itemView.setOnClickListener {
                // Notify the activity about the item click
                itemClickListener.onItemClick(text.text.toString())
            }

            // Register the view for context menu
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu?.add(Menu.NONE, R.id.context_menu_edit, Menu.NONE, "Edit")
            menu?.add(Menu.NONE, R.id.context_menu_delete, Menu.NONE, "Delete")
        }
    }

    // ViewHolder for the "Add Category" button
    class AddButtonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // You can customize this view as needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_layout, parent, false)
                ItemViewHolder(view, itemClickListener).apply {
                    itemView.setOnLongClickListener {
                        selectedPosition = adapterPosition
                        false
                    }
                }
            }
            VIEW_TYPE_ADD_BUTTON -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_category_button, parent, false)
                AddButtonViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_ITEM -> {
                val itemHolder = holder as ItemViewHolder
                itemHolder.text.text = list[position]
            }
            VIEW_TYPE_ADD_BUTTON -> {
                // Handle the button view as needed
                // You can customize the button's appearance or behavior here
            }
        }
    }

    override fun getItemCount(): Int {
        // Add 1 to the item count for the "Add Category" button
        return list.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < list.size) {
            VIEW_TYPE_ITEM
        } else {
            VIEW_TYPE_ADD_BUTTON
        }
    }
}
