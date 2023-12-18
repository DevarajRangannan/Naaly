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
        const val COLUMN_CATEGORY_NAME = "categoryName"
        const val COLUMN_DAILY_HOUR = "dailyHour"
        const val COLUMN_DAILY_MINUTE = "dailyMinute"
        const val COLUMN_WEEKLY_HOUR = "weeklyHour"
        const val COLUMN_WEEKLY_MINUTE = "weeklyMinute"
        const val COLUMN_COUNT_OF_OPEN = "countOfOpen"
        const val COLUMN_DATE = "date"
        const val COLUMN_TITLE = "title"
        const val COLUMN_START_HOUR = "startHour"
        const val COLUMN_START_MINUTE = "startMinute"
        const val COLUMN_END_HOUR = "endHour"
        const val COLUMN_END_MINUTE = "endMinute"
    }


    override fun onCreate(db: SQLiteDatabase) {
         db.execSQL("CREATE TABLE IF NOT EXISTS categories ($COLUMN_CATEGORY_NAME TEXT PRIMARY KEY, $COLUMN_DAILY_HOUR INTEGER, $COLUMN_DAILY_MINUTE INTEGER, $COLUMN_WEEKLY_HOUR INTEGER, $COLUMN_WEEKLY_MINUTE INTEGER, $COLUMN_COUNT_OF_OPEN INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Implement database upgrade if needed
    }

    fun createCategoryTable(categoryName: String) {
        // Create a new table for each category dynamically
        val createTableQuery = "CREATE TABLE IF NOT EXISTS $categoryName ($COLUMN_DATE TEXT, $COLUMN_TITLE TEXT PRIMARY KEY, $COLUMN_START_HOUR INTEGER, $COLUMN_START_MINUTE INTEGER, $COLUMN_END_HOUR INTEGER, $COLUMN_END_MINUTE INTEGER)"
        writableDatabase.execSQL(createTableQuery)
    }

    fun deleteCategoryTable(categoryName: String) {
        // Drop the table if it exists
        val dropTableQuery = "DROP TABLE IF EXISTS $categoryName"
        writableDatabase.execSQL(dropTableQuery)
    }


}
