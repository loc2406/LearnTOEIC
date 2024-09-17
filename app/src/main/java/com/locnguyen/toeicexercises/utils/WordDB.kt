package com.locnguyen.toeicexercises.utils

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.locnguyen.toeicexercises.R
import com.locnguyen.toeicexercises.model.Example
import com.locnguyen.toeicexercises.model.Word
import com.locnguyen.toeicexercises.model.WordKindMean
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class WordDB(private val context: Context, version: Int = 1) :
    SQLiteOpenHelper(context, context.getString(R.string.db_name), null, version) {

    private var db: SQLiteDatabase? = null

    // bỏ 3 hàm dưới nếu lấy dữ liệu từ trên server về
    init {
        // Sao chép cơ sở dữ liệu từ assets nếu chưa tồn tại
        if (!isExistedDatabase()) {
            this.readableDatabase
            copyDatabase()
        }
    }

    private fun isExistedDatabase(): Boolean {
        val dbFile = context.getDatabasePath(context.getString(R.string.db_name))
        return dbFile.exists()
    }

    // Sao chép tệp cơ sở dữ liệu từ thư mục assets vào thư mục cơ sở dữ liệu của ứng dụng.
    private fun copyDatabase() {
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        try {
            // Mở cơ sở dữ liệu từ assets
            inputStream = context.assets.open(context.getString(R.string.db_name))
            val outFileName =
                context.getDatabasePath(context.getString(R.string.db_name)).absolutePath
            outputStream = FileOutputStream(outFileName)
            // Sao chép cơ sở dữ liệu
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }
            outputStream.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            outputStream?.close()
            inputStream?.close()
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    private fun openDb(): SQLiteDatabase? {
        val dbFile = context.getDatabasePath(context.getString(R.string.db_name))

        return if (!dbFile.exists()) {
            null
        } else {
            SQLiteDatabase.openDatabase(dbFile.absolutePath, null, SQLiteDatabase.OPEN_READONLY)
        }
    }

    fun getListWord(level: Int? = null, limit: Int? = null): List<Word> {
        val listWord: ArrayList<Word> = ArrayList()

        if (db == null) db = openDb()

        val cursor: Cursor? = when {
            level == null && limit == null -> {
                db?.rawQuery("Select * from words", null)
            }

            level == null -> {
                db?.rawQuery("Select * from words where limit == $limit", null)
            }

            limit == null -> {
                db?.rawQuery("Select * from words where level == $level", null)
            }

            else -> {
                db?.rawQuery("Select * from words where level == $level && limit == $limit", null)
            }
        }

        cursor?.let{
            it.moveToFirst()

            while (!it.isAfterLast) {
                val idValue = it.getColumnIndex("id")
                val wordValue = it.getColumnIndex("word")
                val shortMeanValue = it.getColumnIndex("short_mean")
                val listMeansValue = it.getColumnIndex("means")
                val levelValue = it.getColumnIndex("level")
                val pronounceValue = it.getColumnIndex("pronounce")

                val wordObject = Word(
                    if (idValue != -1) it.getInt(idValue) else null,
                    if (wordValue != -1) it.getString(wordValue) else null,
                    if (shortMeanValue != -1) it.getString(shortMeanValue) else null,
                    if (listMeansValue != -1) WordKindMean.covertFromJsonStringToList(
                        it.getString(
                            listMeansValue
                        )
                    ) else null,
                    if (levelValue != -1) it.getInt(levelValue) else null,
                    if (pronounceValue != -1) it.getString(pronounceValue) else null,
                )
                listWord.add(wordObject)
                it.moveToNext()
            }
            it.close()
        }

        return listWord
    }

    fun getListExamples(): List<Example> {
        val listExample: ArrayList<Example> = ArrayList()

        if (db == null) db = openDb()

        val cursor: Cursor? = db?.rawQuery("Select * from examples", null)

        cursor?.let{
            it.moveToFirst()

            while (!it.isAfterLast) {
                val idValue = it.getColumnIndex("id")
                val engContentValue = it.getColumnIndex("e")
                val viContentValue = it.getColumnIndex("m")

                if (idValue != -1){
                    val exampleObject = Example(
                        it.getInt(idValue),
                        if (engContentValue != -1) it.getString(engContentValue) else null,
                        if (viContentValue != -1) it.getString(viContentValue) else null
                    )
                    listExample.add(exampleObject)
                }
                it.moveToNext()
            }
            it.close()
        }

        return listExample
    }
}