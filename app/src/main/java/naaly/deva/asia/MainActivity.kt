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
        val showCategory = sharedPreferences.getBoolean("showCategory", true)

        var intent: Intent? = null

        if(isFirstTime){
            intent = Intent(this, PromiseActivity::class.java)
        }
        else{

            if(showCategory){
                intent = Intent(this, CategoryActivity::class.java)
            }
            else{
                intent = Intent(this, HomeActivity::class.java)
            }
        }

        startActivity(intent)
        finish()

    }
}