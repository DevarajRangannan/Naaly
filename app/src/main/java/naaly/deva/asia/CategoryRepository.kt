package naaly.deva.asia

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import naaly.deva.asia.DatabaseHelper.Companion.COLUMN_CATEGORY_NAME
import naaly.deva.asia.DatabaseHelper.Companion.COLUMN_DAILY_HOUR
import naaly.deva.asia.DatabaseHelper.Companion.COLUMN_DAILY_MINUTE
import naaly.deva.asia.DatabaseHelper.Companion.COLUMN_RECENT_OPEN
import naaly.deva.asia.DatabaseHelper.Companion.COLUMN_WEEKLY_HOUR
import naaly.deva.asia.DatabaseHelper.Companion.COLUMN_WEEKLY_MINUTE
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class Category(
    var categoryName: String,
    val dailyHour: Int,
    val dailyMinute: Int,
    val weeklyHour: Int,
    val weeklyMinute: Int,
    val recentOpen: Long
    )


class CategoryRepository(private val context: Context) {

    private val dbHelper = DatabaseHelper(context)

    @SuppressLint("Range")
    fun getCategoryByNames(categoryTableName: String): List<Category> {
        val categoryNames = mutableListOf<Category>()
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM $categoryTableName", null)
        cursor.use {
            while (it.moveToNext()) {
                val categoryName = it.getString(it.getColumnIndex(COLUMN_CATEGORY_NAME))
                val dailyHour = it.getInt(it.getColumnIndex(COLUMN_DAILY_HOUR))
                val dailyMinute = it.getInt(it.getColumnIndex(COLUMN_DAILY_MINUTE))
                val weeklyHour = it.getInt(it.getColumnIndex(COLUMN_WEEKLY_HOUR))
                val weeklyMinute = it.getInt(it.getColumnIndex(COLUMN_WEEKLY_MINUTE))
                val recentOpen = it.getLong(it.getColumnIndex(COLUMN_RECENT_OPEN))

                val category = Category(categoryName, dailyHour, dailyMinute, weeklyHour, weeklyMinute, recentOpen)
                categoryNames.add(category)

            }
        }

        db.close()
        return categoryNames
    }

    fun insertCategory(categoryName: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CATEGORY_NAME, categoryName)
            put(COLUMN_DAILY_HOUR, 0)
            put(COLUMN_DAILY_MINUTE, 0)
            put(COLUMN_WEEKLY_HOUR, 0)
            put(COLUMN_WEEKLY_MINUTE, 0)
            put(COLUMN_RECENT_OPEN, 0)
        }
        db.insert("categories", null, values)
        db.close()
    }

    fun updateCategory(oldCategoryName: String, newCategoryName: String) {
        val db = dbHelper.writableDatabase

        val renameTableQuery = "ALTER TABLE `$oldCategoryName` RENAME TO `$newCategoryName`"
        db.execSQL(renameTableQuery)

        val values = ContentValues().apply {
            put("categoryName", newCategoryName)
        }
        db.update("categories", values, "categoryName = ?", arrayOf(oldCategoryName))

        db.close()
    }

    fun deleteCategory(categoryName: String) {
        val db = dbHelper.writableDatabase

        // 1. Delete the category from the 'categories' table
        db.delete("categories", "categoryName = ?", arrayOf(categoryName))

        // 2. Delete the category table
        dbHelper.deleteCategoryTable(categoryName)

        db.close()
    }

    fun updateRecentOpenDateTime(categoryName: String) {
        val db = dbHelper.writableDatabase

        val currentDateTime = Date().time

        Log.i("updateRecentOpenDateTime", "updateRecentOpenDateTime: $currentDateTime")

        val updateQuery = "UPDATE categories SET $COLUMN_RECENT_OPEN = '$currentDateTime' WHERE $COLUMN_CATEGORY_NAME = ?"
        db.execSQL(updateQuery, arrayOf(categoryName))


        db.close()
    }
}
