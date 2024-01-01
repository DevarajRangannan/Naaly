package naaly.deva.asia

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import naaly.deva.asia.DatabaseHelper.Companion.COLUMN_CATEGORY_NAME
import naaly.deva.asia.DatabaseHelper.Companion.COLUMN_COUNT_OF_OPEN
import naaly.deva.asia.DatabaseHelper.Companion.COLUMN_DAILY_HOUR
import naaly.deva.asia.DatabaseHelper.Companion.COLUMN_DAILY_MINUTE
import naaly.deva.asia.DatabaseHelper.Companion.COLUMN_WEEKLY_HOUR
import naaly.deva.asia.DatabaseHelper.Companion.COLUMN_WEEKLY_MINUTE

data class Category(
    var categoryName: String,
    val dailyHour: Int,
    val dailyMinute: Int,
    val weeklyHour: Int,
    val weeklyMinute: Int,
    val countOfOpen: Int
    )


class CategoryRepository(private val context: Context) {

    private val dbHelper = DatabaseHelper(context)

    @SuppressLint("Range")
    fun getCategoryNames(categoryTableName: String): List<Category> {
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
                val countOfOpen = it.getInt(it.getColumnIndex(COLUMN_COUNT_OF_OPEN))
                val category = Category(categoryName, dailyHour, dailyMinute, weeklyHour, weeklyMinute, countOfOpen)
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
            put(COLUMN_COUNT_OF_OPEN, 0)

        }
        db.insert("categories", null, values)
        db.close()
    }

    fun updateCategory(oldCategoryName: String, newCategoryName: String) {
        val db = dbHelper.writableDatabase

        val renameTableQuery = "ALTER TABLE $oldCategoryName RENAME TO $newCategoryName"
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

    fun updateCountOfOpen(categoryName: String) {
        val db = dbHelper.writableDatabase

        // Increment countOfOpen by 1
        val incrementCountQuery = "UPDATE categories SET $COLUMN_COUNT_OF_OPEN = $COLUMN_COUNT_OF_OPEN + 1 WHERE $COLUMN_CATEGORY_NAME = ?"
        db.execSQL(incrementCountQuery, arrayOf(categoryName))


        db.close()
    }
}
