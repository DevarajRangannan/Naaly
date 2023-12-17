package naaly.deva.asia

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoryActivity : AppCompatActivity() {

    private lateinit var categoryTitleView: TextView
    private lateinit var dailyHourView: TextView
    private lateinit var dailyMinuteView: TextView
    private lateinit var weeklyHourView: TextView
    private lateinit var weeklyMinuteView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: naaly.deva.asia.Adapter
    private lateinit var list: ArrayList<String>

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        categoryTitleView = findViewById(R.id.categoryTitleText)
        dailyHourView = findViewById(R.id.categoryDailyHourText)
        dailyMinuteView = findViewById(R.id.categoryDailyMinuteText)
        weeklyHourView = findViewById(R.id.categoryWeeklyHourText)
        weeklyMinuteView = findViewById(R.id.categoryWeeklyMinuteText)
        recyclerView = findViewById(R.id.categoryRecylerView)


        val sharedPreferences = getSharedPreferences("naalyMain", Context.MODE_PRIVATE)
        val categoryTitle = sharedPreferences.getString("categoryTitle", "")
        val dailyHour = sharedPreferences.getInt("dailyHour", 0)
        val dailyMinute = sharedPreferences.getInt("dailyMinute", 0)
        val weeklyHour = sharedPreferences.getInt("weeklyHour", 0)
        val weeklyMinute = sharedPreferences.getInt("weeklyMinute", 0)


        list = ArrayList()
//        list.add("Work")
//        list.add("Exercise")
//        list.add("Self Improvement")
//        list.add("Health")
//        list.add("Entertainment")


        adapter = Adapter(list)
        recyclerView.setHasFixedSize(true)
        if(list.size == 0)
            recyclerView.layoutManager = GridLayoutManager(this,1)
        else
            recyclerView.layoutManager = GridLayoutManager(this,2)

        recyclerView.adapter = adapter

        if (categoryTitle != "") {
            categoryTitleView.text = categoryTitle?.uppercase()
        } else {
            categoryTitleView.text = "PLEASE ADD CATEGORY"
        }
        //dailyHourView
        if (dailyHour > 1) {
            dailyHourView.text = "$dailyHour Hours"
        } else {
            dailyHourView.text = "$dailyHour Hour"
        }
        //dailyMinuteView
        if (dailyMinute > 1) {
            dailyMinuteView.text = "$dailyMinute Minutes"
        } else {
            dailyMinuteView.text = "$dailyMinute Minute"
        }

        //weeklyHourView
        if (weeklyHour > 1) {
            weeklyHourView.text = "$weeklyHour Hours"
        } else {
            weeklyHourView.text = "$weeklyHour Hour"
        }
        //weeklyMinuteView
        if (weeklyMinute > 1) {
            weeklyMinuteView.text = "$weeklyMinute Minutes"
        } else {
            weeklyMinuteView.text = "$weeklyMinute Minute"
        }



    }
}