package naaly.deva.asia

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "naaly"
        const val DATABASE_VERSION = 1
        const val COLUMN_ID = "id"
        const val COLUMN_CATEGORY_NAME = "category_name"
    }


    override fun onCreate(db: SQLiteDatabase) {
         db.execSQL("CREATE TABLE IF NOT EXISTS categories (_id INTEGER PRIMARY KEY AUTOINCREMENT, categoryName TEXT, countOfOpen INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Implement database upgrade if needed
    }

    fun createCategoryTable(categoryName: String) {
        // Create a new table for each category dynamically
        val createTableQuery = "CREATE TABLE IF NOT EXISTS $categoryName ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_CATEGORY_NAME TEXT)"
        writableDatabase.execSQL(createTableQuery)
    }

    fun deleteCategoryTable(categoryName: String) {
        // Drop the table if it exists
        Log.i("XXX", categoryName)
        val dropTableQuery = "DROP TABLE IF EXISTS $categoryName"
        writableDatabase.execSQL(dropTableQuery)
    }


}
