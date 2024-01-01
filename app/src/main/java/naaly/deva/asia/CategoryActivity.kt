// CategoryActivity.kt
package naaly.deva.asia

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
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
    private lateinit var list: ArrayList<Category>
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var dbHelper: DatabaseHelper

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var categoryTitle: String
    private var dailyHour: Int = 0
    private var dailyMinute: Int = 0
    private var weeklyHour: Int = 0
    private var weeklyMinute: Int = 0

    private var currentCategoryDeleted = false
    private var isCurrentCategoryEdited = false

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

        sharedPreferences = getSharedPreferences("naalyMain", Context.MODE_PRIVATE)
        categoryTitle = sharedPreferences.getString("categoryTitle", "").toString()
        dailyHour = sharedPreferences.getInt("dailyHour", 0)
        dailyMinute = sharedPreferences.getInt("dailyMinute", 0)
        weeklyHour = sharedPreferences.getInt("weeklyHour", 0)
        weeklyMinute = sharedPreferences.getInt("weeklyMinute", 0)

        dbHelper = DatabaseHelper(this)

        list = ArrayList()

        categoryRepository = CategoryRepository(this)

        // Use categoryRepository to get category names and populate your list
        val categoryTableName = "categories" // replace with the actual table name

        list = categoryRepository.getCategoryNames(categoryTableName) as ArrayList<Category>
        list = list.sortedByDescending { it.countOfOpen  }.toMutableList() as ArrayList<Category>

        recyclerView.setHasFixedSize(true)
        if (list.size == 0)
            recyclerView.layoutManager = GridLayoutManager(this, 1)
        else
            recyclerView.layoutManager = GridLayoutManager(this, 2)

//        recyclerView.adapter = adapter
        adapter = Adapter(list, this)
        recyclerView.adapter = adapter
        registerForContextMenu(recyclerView)

        mainDisplay()
    }

    @SuppressLint("SetTextI18n")
    private fun mainDisplay(){
        Log.i("mainDisplay", "mainDisplay: "+list.size.toString())
        if (categoryTitle != "") {
            categoryTitleView.text = categoryTitle.uppercase()
        } else {
            if(list.size > 0)
                categoryTitleView.text = "PLEASE SELECT CATEGORY"
            else
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
    override fun onItemClick(selectedCategoryTitle: String) {
        // Update the categoryTitleView when an item is clicked
        val selectedCategory = list.find { it.categoryName == selectedCategoryTitle.replace(" ", "_").lowercase() }

        if (selectedCategory != null) {
            categoryTitle = selectedCategory.categoryName.replace("_", " ")
        }
        if (selectedCategory != null) {
            dailyHour = selectedCategory.dailyHour
        }
        if (selectedCategory != null) {
            dailyMinute = selectedCategory.dailyMinute
        }
        if (selectedCategory != null) {
            weeklyHour = selectedCategory.weeklyHour
        }
        if (selectedCategory != null) {
            weeklyMinute = selectedCategory.weeklyMinute
        }

        mainDisplay()
        proceedBTN.alpha = 1f
        proceedBTN.isEnabled = true


    }

    @SuppressLint("NotifyDataSetChanged", "CommitPrefEdits")
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = adapter.selectedPosition

        return when (item.itemId) {
            R.id.context_menu_edit -> {

                val dialog = Dialog(this)

                // Set the content view from the layout
                dialog.setContentView(R.layout.popup_update_category)

                // Calculate the width of the dialog based on the screen width
                val displayMetrics = DisplayMetrics()
                windowManager.defaultDisplay.getMetrics(displayMetrics)
                val dialogWidth = (displayMetrics.widthPixels * 0.9).toInt()

                // Set the width of the dialog
                val window = dialog.window
                val layoutParams = window?.attributes
                layoutParams?.width = dialogWidth
                window?.attributes = layoutParams

                // Find your views in the layout
                val updateET: EditText = dialog.findViewById(R.id.UpdateEditTextCategory)
                val updateBTN: Button = dialog.findViewById(R.id.buttonUpdateCategory)
                val oldName: String = list[position].categoryName
                updateET.setText(oldName.replace("_", " ").capitalizeWords())

                // Set click listener for the "Update Category" button
                updateBTN.setOnClickListener {
                    updateBTN.isEnabled = false
                    val categoryName = updateET.text.toString().trim().lowercase().replace(" ", "_")
                    if (categoryName.isNotEmpty()) {
                        Log.i("onItemUpdated", "onItemUpdated: ${sharedPreferences.getString("categoryTitle", "").toString()}:: ${oldName.replace("_", " ")} :: $categoryName")

                        if (oldName != categoryName) {
                            if (categoryTitle == oldName.replace("_", " "))
                                categoryTitleView.text = updateET.text.toString().uppercase()

                            list[position].categoryName = categoryName

                            adapter.notifyDataSetChanged()

                            // update data to the category table
                            categoryRepository.updateCategory(oldName, categoryName)

                            if(sharedPreferences.getString("categoryTitle", "").toString() == oldName.replace("_", " ")){
                                val editor = sharedPreferences.edit()
                                editor.putString("categoryTitle",  categoryName.replace("_", " "))
                                isCurrentCategoryEdited = true
                                categoryTitle = categoryName.replace("_", " ")
                                editor.apply()
                            }
                        }
                        dialog.dismiss()

                    } else {
                        updateBTN.isEnabled = true

                        Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT)
                            .show()
                    }

                }

                // Show the dialog
                dialog.show()
                true
            }

            R.id.context_menu_delete -> {
                val dialog = Dialog(this)

                // Set the content view from the layout
                dialog.setContentView(R.layout.popup_delete_category)

                // Calculate the width of the dialog based on the screen width
                val displayMetrics = DisplayMetrics()
                windowManager.defaultDisplay.getMetrics(displayMetrics)
                val dialogWidth = (displayMetrics.widthPixels * 0.9).toInt()

                // Set the width of the dialog
                val window = dialog.window
                val layoutParams = window?.attributes
                layoutParams?.width = dialogWidth
                window?.attributes = layoutParams

                // Find your views in the layout
                val DeleteCategoryTitleTV: TextView = dialog.findViewById(R.id.deleteCategoryTV)
                val DeleteCategoryBTN: TextView = dialog.findViewById(R.id.confirmDeleteCategoryBTN)
                val CancelCategoryTitleBTN: TextView =
                    dialog.findViewById(R.id.cancelDeleteCategoryBTN)

                val DeleteCategoryTitle: String = list[position].categoryName
                DeleteCategoryTitleTV.setText(DeleteCategoryTitle.replace("_", " ").capitalizeWords())

                // Set click listener for the "Update Category" button
                DeleteCategoryBTN.setOnClickListener {

                    list.removeIf { it.categoryName == DeleteCategoryTitle }

                    if (list.size == 0) {
                        recyclerView.layoutManager = GridLayoutManager(this, 1)
                    }


                    if(categoryTitleView.text == DeleteCategoryTitle.replace("_", " ").uppercase()){
                        categoryTitle = ""
                        dailyHour = 0
                        dailyMinute = 0
                        weeklyHour = 0
                        weeklyMinute = 0

                        Log.i("DeleteCategoryBTNDeleteCategoryBTN", "onContextItemSelected: " + sharedPreferences.getString("categoryTitle", "").toString() + " "+DeleteCategoryTitle)
                        if(sharedPreferences.getString("categoryTitle", "").toString() == DeleteCategoryTitle){
                            val editor = sharedPreferences.edit()

                            currentCategoryDeleted = true
                            editor?.putBoolean("showCategory", true)
                            editor?.putString("categoryTitle", "")
                            editor.apply()
                        }

                    }
                    adapter.notifyDataSetChanged()

                    // update data to the category table
                    categoryRepository.deleteCategory(DeleteCategoryTitle)
                    dbHelper.deleteCategoryTable(DeleteCategoryTitle)
                    mainDisplay()

                    dialog.dismiss()

                }

                CancelCategoryTitleBTN.setOnClickListener {

                    dialog.dismiss()

                }


                // Show the dialog
                dialog.show()
                true
            }

            else -> super.onContextItemSelected(item)
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        Log.i("onBackPressed", "onBackPressed: 1")
        // Check your condition here

        if(isCurrentCategoryEdited){
            Log.i("onBackPressed", "onBackPressed: 4")
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
        else if (currentCategoryDeleted) {
            Log.i("onBackPressed", "onBackPressed: 2")

            finishAffinity()
        } else {
            // Call super.onBackPressed() to allow the default back press behavior
            Log.i("onBackPressed", "onBackPressed: 3")

            super.onBackPressed()
        }
    }

    @SuppressLint("CommitPrefEdits")
    fun clickProceed(view: View) {
        proceedBTN.isEnabled = false
        Log.i("xxxy", "onContextItemSelected: ${categoryTitle}")

        val editor = sharedPreferences.edit()

        val intent = Intent(this, HomeActivity::class.java)

        editor?.putString("categoryTitle", categoryTitle)
        editor?.putInt("dailyHour", dailyHour)
        editor?.putInt("dailyMinute", dailyMinute)
        editor?.putInt("weeklyHour", weeklyHour)
        editor?.putInt("weeklyMinute", weeklyMinute)
        editor?.putBoolean("showCategory", false)
        editor?.apply()

        categoryRepository.updateCountOfOpen(categoryTitle.replace(" ","_"))

        startActivity(intent)
        finishAffinity()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun showAddCategoryPopup(view: View) {
        val dialog = Dialog(this)

        // Set the content view from the layout
        dialog.setContentView(R.layout.popup_add_category)

        // Calculate the width of the dialog based on the screen width
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val dialogWidth = (displayMetrics.widthPixels * 0.9).toInt()

        // Set the width of the dialog
        val window = dialog.window
        val layoutParams = window?.attributes
        layoutParams?.width = dialogWidth
        window?.attributes = layoutParams

        // Find your views in the layout
        val editTextCategory: EditText = dialog.findViewById(R.id.EditTextCategory)
        val buttonAddCategory: Button = dialog.findViewById(R.id.buttonAddCategory)

        // Set click listener for the "Add Category" button
        buttonAddCategory.setOnClickListener {
            val categoryName = editTextCategory.text.toString().trim().lowercase().replace(" ", "_")
            if (categoryName.isNotEmpty()) {
                // Create a new table for the category
                val categoryExists = list.any { it.categoryName == categoryName }
                if (!categoryExists) {
                    if (list.size == 0) {
                        recyclerView.layoutManager = GridLayoutManager(this, 2)
                    }

                    val categoryItem = Category(categoryName, 0, 0, 0, 0, 0, )
                    list.add(categoryItem)
                    adapter.notifyDataSetChanged()

                    dbHelper.createCategoryTable(categoryName)

                    // Add data to the category table
                    categoryRepository.insertCategory(categoryName)

                    // Notify the adapter of the dataset changes

                    // Close the popup
                    mainDisplay()

                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Category already exist", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show()


            }

        }

        // Show the dialog
        dialog.show()
    }

}

fun String.capitalizeWords(): String {
    return this.split(" ").joinToString(" ") { it.capitalize() }
}
