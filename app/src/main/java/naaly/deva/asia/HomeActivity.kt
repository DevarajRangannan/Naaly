package naaly.deva.asia

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView

class HomeActivity : AppCompatActivity() {

    private lateinit var categoryTitleView: TextView
    private lateinit var dailyHourView: TextView
    private lateinit var dailyMinuteView: TextView
    private lateinit var weeklyHourView: TextView
    private lateinit var weeklyMinuteView: TextView

    private lateinit var categoryTitle: String
    private var dailyHour: Int = 0
    private var dailyMinute: Int = 0
    private var weeklyHour: Int = 0
    private var weeklyMinute: Int = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val sharedPreferences = getSharedPreferences("naalyMain", Context.MODE_PRIVATE)
        categoryTitle = sharedPreferences.getString("categoryTitle", "").toString()
        dailyHour = sharedPreferences.getInt("dailyHour", 0)
        dailyMinute = sharedPreferences.getInt("dailyMinute", 0)
        weeklyHour = sharedPreferences.getInt("weeklyHour", 0)
        weeklyMinute = sharedPreferences.getInt("weeklyMinute", 0)

        val menuIcon = findViewById<CardView>(R.id.menuIconCard)
        registerForContextMenu(menuIcon)

        menuIcon.setOnClickListener {
            showPopupMenu(menuIcon)
        }



        categoryTitleView = findViewById(R.id.mainCategoryTitleText)
        dailyHourView = findViewById(R.id.mainCategoryDailyHourText)
        dailyMinuteView = findViewById(R.id.mainCategoryDailyMinuteText)
        weeklyHourView = findViewById(R.id.mainCategoryWeeklyHourText)
        weeklyMinuteView = findViewById(R.id.mainCategoryWeeklyMinuteText)

        mainDisplay()
//        categoryTitleView.text = categoryTitle
//        dailyHourView.text = dailyHour.toString()
//        dailyMinuteView.text = dailyMinute.toString()
//        weeklyHourView.text = weeklyHour.toString()
//        weeklyMinuteView.text = weeklyMinute.toString()
    }

    private fun mainDisplay(){
        if (categoryTitle != "") {
            categoryTitleView.text = categoryTitle.uppercase()
        } else {
            categoryTitleView.text = "CATEGORY"
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

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.option_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.context_menu_categories -> {
                    // Implement logic for Categories option
                    val intent = Intent(this, CategoryActivity::class.java)
                    startActivity(intent)

                    true
                }
                R.id.context_menu_settings -> {
                    // Implement logic for Settings option
                    showToast("Settings option selected")
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}