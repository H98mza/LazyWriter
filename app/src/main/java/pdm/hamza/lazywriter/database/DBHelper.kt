package pdm.hamza.lazywriter.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            """
                CREATE TABLE $MESSAGES_TABLE (
                    ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COL_TEXT NOT NULL
                )
            """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Niente per ora
    }

    // Crea un nuovo messaggio
    fun addMessage(text: String): Long {
        Log.d("aaa", text)
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COL_TEXT, text)
        }

        return db?.insert(MESSAGES_TABLE, null, values) ?: -1
    }

    // Elimina un messaggio esistente
    fun deleteMessage(id: Long) {
        writableDatabase.delete(
            MESSAGES_TABLE,
            "${BaseColumns._ID} = ?",
            arrayOf(id.toString())
        )
    }

    // Restituisce la lista dei messaggi
    fun listMessages(): MutableList<Message> {
        val db = readableDatabase

        val projection = arrayOf(
            BaseColumns._ID,
            COL_TEXT
        )

        val cursor = db.query(
            MESSAGES_TABLE,
            projection,
            null,
            null,
            null,
            null,
            "$COL_TEXT ASC"
        )

        val list = mutableListOf<Message>()
        with(cursor) {
            while(moveToNext()) {
                list.add(
                    Message(
                        getLong(getColumnIndex(BaseColumns._ID)),
                       getString(getColumnIndex(COL_TEXT))
                    ))
            }
        }

        return list
    }

    companion object {
        const val DATABASE_NAME = "lazyWriterDB"
        const val DATABASE_VERSION = 1

        const val MESSAGES_TABLE = "Messages"
        const val COL_TEXT = "Text"
    }
}