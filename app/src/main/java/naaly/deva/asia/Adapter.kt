package naaly.deva.asia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class Adapter(private var list: ArrayList<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // View types
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_ADD_BUTTON = 1

    // ViewHolder for regular items
    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var text: TextView = view.findViewById(R.id.itemText)

        init {
            itemView.setOnClickListener {
                // Show a Toast when the item is clicked
                val context = itemView.context
                val message = text.text
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ViewHolder for the "Add Category" button
    class AddButtonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var addButton: TextView = view.findViewById(R.id.addCategoryBTN)

        init {
            // Set click listener for the button
            addButton.setOnClickListener {
                // Handle the button click event
                // For example, you can add a new category to the list here
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
                ItemViewHolder(view)
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
