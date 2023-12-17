// CategoryActivity.kt
package naaly.deva.asia

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoryActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var categoryTitleView: TextView
    private lateinit var dailyHourView: TextView
    private lateinit var dailyMinuteView: TextView
    private lateinit var weeklyHourView: TextView
    private lateinit var weeklyMinuteView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var proceedBTN: Button
    private lateinit var adapter: Adapter
    private lateinit var list: ArrayList<String>

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        categoryTitleView = findViewById(R.id.categoryTitleText)
        dailyHourView = findViewById(R.id.categoryDailyHourText)
        dailyMinuteView = findViewById(R.id.categoryDailyMinuteText)
        weeklyHourView = findViewById(R.id.categoryWeeklyHourText)
        weeklyMinuteView = findViewById(R.id.categoryWeeklyMinuteText)
        recyclerView = findViewById(R.id.categoryRecylerView)
        proceedBTN = findViewById(R.id.proceedBTN)

        val sharedPreferences = getSharedPreferences("naalyMain", Context.MODE_PRIVATE)
        val categoryTitle = sharedPreferences.getString("categoryTitle", "")
        val dailyHour = sharedPreferences.getInt("dailyHour", 0)
        val dailyMinute = sharedPreferences.getInt("dailyMinute", 0)
        val weeklyHour = sharedPreferences.getInt("weeklyHour", 0)
        val weeklyMinute = sharedPreferences.getInt("weeklyMinute", 0)

        list = ArrayList()
        list.add("Work")
        list.add("Exercise")
        list.add("Self Improvement")
        list.add("Health")
        list.add("Entertainment")

//        adapter = Adapter(list, this)
        recyclerView.setHasFixedSize(true)
        if (list.size == 0)
            recyclerView.layoutManager = GridLayoutManager(this, 1)
        else
            recyclerView.layoutManager = GridLayoutManager(this, 2)

//        recyclerView.adapter = adapter
        adapter = Adapter(list, this)
        recyclerView.adapter = adapter
        registerForContextMenu(recyclerView)

        if (categoryTitle != "") {
            categoryTitleView.text = categoryTitle?.uppercase()
        } else {
            categoryTitleView.text = "PLEASE ADD CATEGORY"
            proceedBTN.alpha = 0.3f
            proceedBTN.isEnabled = false
        }

        if (dailyHour > 1) {
            dailyHourView.text = "$dailyHour Hours"
        } else {
            dailyHourView.text = "$dailyHour Hour"
        }

        if (dailyMinute > 1) {
            dailyMinuteView.text = "$dailyMinute Minutes"
        } else {
            dailyMinuteView.text = "$dailyMinute Minute"
        }

        if (weeklyHour > 1) {
            weeklyHourView.text = "$weeklyHour Hours"
        } else {
            weeklyHourView.text = "$weeklyHour Hour"
        }

        if (weeklyMinute > 1) {
            weeklyMinuteView.text = "$weeklyMinute Minutes"
        } else {
            weeklyMinuteView.text = "$weeklyMinute Minute"
        }
    }

    // Implement the onItemClick method from the OnItemClickListener interface
    override fun onItemClick(categoryTitle: String) {
        // Update the categoryTitleView when an item is clicked
        categoryTitleView.text = categoryTitle.uppercase()
        proceedBTN.alpha = 1f
        proceedBTN.isEnabled = true


    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = adapter.selectedPosition

        return when (item.itemId) {
            R.id.context_menu_edit -> {
                // Implement edit logic here using the position
                Log.i("xxx1", "Edit item at position: $position")
                Toast.makeText(this, "Edit item at position: $position", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.context_menu_delete -> {
                // Implement delete logic here using the position
                Log.i("xxx2", "Delete item at position: $position")
                Toast.makeText(this, "Delete item at position: $position", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun clickProceed(view: View) {
        Toast.makeText(this, "ProceedBTN", Toast.LENGTH_SHORT).show()
    }
}
