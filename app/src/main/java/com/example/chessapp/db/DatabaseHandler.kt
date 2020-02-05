package com.example.chessapp.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.chessapp.models.Player
import java.util.*

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DatabaseHandler.DB_NAME, null, DatabaseHandler.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $NAME TEXT,$ELO INTEGER,$WONWC TEXT);"
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun addPlayer(player: Player): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, player.name)
        values.put(ELO, player.elo)
        values.put(WONWC, player.wonWC)
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        Log.v("InsertedId", "$_success")
        return (Integer.parseInt("$_success") != -1)
    }

    fun getPlayer(_id: Int): Player {
        val player = Player()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.moveToFirst()
        player.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
        player.name = cursor.getString(cursor.getColumnIndex(NAME))
        player.elo = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ELO)))
        player.wonWC = cursor.getString(cursor.getColumnIndex(WONWC))
        cursor.close()
        return player
    }

    fun getAllPlayers(): List<Player> {
        val playersList = ArrayList<Player>()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val player = Player()
                    player.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    player.name = cursor.getString(cursor.getColumnIndex(NAME))
                    player.elo = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ELO)))
                    player.wonWC = cursor.getString(cursor.getColumnIndex(WONWC))
                    playersList.add(player)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return playersList
    }

    fun updatePlayer(player: Player): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, player.name)
        values.put(ELO, player.elo)
        values.put(WONWC, player.wonWC)
        val _success = db.update(TABLE_NAME, values, ID + "=?", arrayOf(player.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deletePlayer(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, ID + "=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteAllPlayers(): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, null, null).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    companion object {
        private val DB_VERSION = 1
        private val DB_NAME = "MyTasks"
        private val TABLE_NAME = "Tasks"
        private val ID = "Id"
        private val NAME = "Name"
        private val ELO = "Elo"
        private val WONWC = "Won"
    }
}