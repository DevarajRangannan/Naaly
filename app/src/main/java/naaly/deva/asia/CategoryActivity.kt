package naaly.deva.asia

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class CategoryActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        val categoryTitleView = findViewById<TextView>(R.id.categoryTitleText)
        val dailyHourView = findViewById<TextView>(R.id.categoryDailyHourText)
        val dailyMinuteView = findViewById<TextView>(R.id.categoryDailyMinuteText)
        val weeklyHourView = findViewById<TextView>(R.id.categoryWeeklyHourText)
        val weeklyMinuteView = findViewById<TextView>(R.id.categoryWeeklyMinuteText)

        val sharedPreferences = getSharedPreferences("naalyMain", Context.MODE_PRIVATE)
        val categoryTitle = sharedPreferences.getString("categoryTitle", "")
        val dailyHour = sharedPreferences.getInt("dailyHour", 0)
        val dailyMinute = sharedPreferences.getInt("dailyMinute", 0)
        val weeklyHour = sharedPreferences.getInt("weeklyHour", 0)
        val weeklyMinute = sharedPreferences.getInt("weeklyMinute", 0)

        if(categoryTitle != ""){
            categoryTitleView.text = categoryTitle?.uppercase()
        }
        else{
            categoryTitleView.text = "PLEASE ADD CATEGORY"
        }
//dailyHourView
        if(dailyHour > 1){
            dailyHourView.text = "$dailyHour Hours"
        }
        else{
            dailyHourView.text = "$dailyHour Hour"
        }
//dailyMinuteView
        if(dailyMinute > 1){
            dailyMinuteView.text = "$dailyMinute Minutes"
        }
        else{
            dailyMinuteView.text = "$dailyMinute Minute"
        }

//weeklyHourView
        if(weeklyHour > 1){
            weeklyHourView.text = "$weeklyHour Hours"
        }
        else{
            weeklyHourView.text = "$weeklyHour Hour"
        }
//weeklyMinuteView
        if(weeklyMinute > 1){
            weeklyMinuteView.text = "$weeklyMinute Minutes"
        }
        else{
            weeklyMinuteView.text = "$weeklyMinute Minute"
        }

}
}