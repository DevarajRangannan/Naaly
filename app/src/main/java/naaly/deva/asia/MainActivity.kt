package naaly.deva.asia

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1000)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("naalyMain", Context.MODE_PRIVATE)
        val isFirstTime = sharedPreferences.getBoolean("isFirstTime", true)

        if(isFirstTime){
//            val editor = sharedPreferences.edit()
//            editor?.putInt("dailyHour", 0)
//            editor?.putInt("dailyMinute", 0)
//            editor?.putInt("weeklyHour", 0)
//            editor?.putInt("weeklyMinute", 0)
//            editor?.putString("categoryTitle", "")
//            editor?.apply()
            val intent = Intent(this, PromiseActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}