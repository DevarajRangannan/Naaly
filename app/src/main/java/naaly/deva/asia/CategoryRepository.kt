package naaly.deva.asia

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context

data class Category(var categoryName: String, val countOfOpen: Int)


class CategoryRepository(private val context: Context) {

    private val dbHelper = DatabaseHelper(context)

    @SuppressLint("Range")
    fun getCategoryNames(categoryTableName: String): List<Category> {
        val categoryNames = mutableListOf<Category>()
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery("SELECT categoryName, countOfOpen FROM $categoryTableName", null)
        cursor.use {
            while (it.moveToNext()) {
                val categoryName = it.getString(it.getColumnIndex("categoryName"))
                val countOfOpen = it.getInt(it.getColumnIndex("countOfOpen"))
                val category = Category(categoryName, countOfOpen)
                categoryNames.add(category)

            }
        }

        db.close()
        return categoryNames
    }


    fun insertCategory(categoryName: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("categoryName", categoryName)
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


}
