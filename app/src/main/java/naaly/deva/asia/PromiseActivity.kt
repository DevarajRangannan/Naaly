package naaly.deva.asia

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

class PromiseActivity : AppCompatActivity() {
    private var promiseText: TextView? = null
    private var sharedPreferences: SharedPreferences? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promise)
        promiseText = findViewById(R.id.promiseText)
        sharedPreferences = getSharedPreferences("naalyMain", Context.MODE_PRIVATE)
    }

    fun clickPromise(view: View) {
        val editor = sharedPreferences?.edit()
        val intent = Intent(this, CategoryActivity::class.java)

        editor?.putBoolean("isFirstTime", false)
        editor?.apply()

        startActivity(intent)
        finish()
    }
}