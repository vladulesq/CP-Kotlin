package com.example.chessapp

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.chessapp.models.Player
import com.example.chessapp.db.DatabaseHandler
import kotlinx.android.synthetic.main.activity_add_edit.*

class AddOrEditActivity : AppCompatActivity() {

    var dbHandler: DatabaseHandler? = null
    var isEditMode = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initDB()
        initOperations()
    }

    private fun initDB() {
        dbHandler = DatabaseHandler(this)
        btn_delete.visibility = View.INVISIBLE
        if (intent != null && intent.getStringExtra("Mode") == "E") {
            isEditMode = true
            val player: Player = dbHandler!!.getPlayer(intent.getIntExtra("Id",0))
            input_name.setText(player.name)
            input_elo.setText(player.elo.toString())
            swt_won.isChecked = player.wonWC == "Y"
            btn_delete.visibility = View.VISIBLE
        }
    }

    private fun initOperations() {
        btn_save.setOnClickListener({
            var success: Boolean = false
            if (!isEditMode) {
                val player: Player = Player()
                player.name = input_name.text.toString()
                player.elo = input_elo.text.toString().toInt()
                if (swt_won.isChecked)
                    player.wonWC = "Y"
                else
                    player.wonWC = "N"
                success = dbHandler?.addPlayer(player) as Boolean
            } else {
                val player: Player = Player()
                player.id = intent.getIntExtra("Id", 0)
                player.name = input_name.text.toString()
                player.elo = input_elo.text.toString().toInt()
                if (swt_won.isChecked)
                    player.wonWC = "Y"
                else
                    player.wonWC = "N"
                success = dbHandler?.updatePlayer(player) as Boolean
            }

            if (success)
                finish()
        })

        btn_delete.setOnClickListener({
            val dialog = AlertDialog.Builder(this).setTitle("Info").setMessage("Click 'YES' to delete the Player.")
                .setPositiveButton("YES", { dialog, i ->
                    val success = dbHandler?.deletePlayer(intent.getIntExtra("Id", 0)) as Boolean
                    if (success)
                        finish()
                    dialog.dismiss()
                })
                .setNegativeButton("NO", { dialog, i ->
                    dialog.dismiss()
                })
            dialog.show()
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}