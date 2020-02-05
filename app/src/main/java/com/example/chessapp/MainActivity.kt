package com.example.chessapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bugsnag.android.Bugsnag
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.chessapp.db.DatabaseHandler
import com.example.chessapp.models.Player
import com.example.chessapp.adapter.PlayerRecyclerAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() {
    private lateinit var mAdView: AdView
    private val mAppUnitId: String by lazy {

        "ca-app-pub-2407441983618355~4162136284"
    }

    var playerRecyclerAdapter: PlayerRecyclerAdapter? = null
    var fab: FloatingActionButton? = null
    var recyclerView: RecyclerView? = null
    var dbHandler: DatabaseHandler? = null
    var listPlayer: List<Player> = ArrayList<Player>()
    var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Bugsnag.init(this)
        Bugsnag.notify(RuntimeException("Test error"))
        mAdView = findViewById(R.id.adView)

        initializeBannerAd(mAppUnitId)

        loadBannerAd()

        val ttb = AnimationUtils.loadAnimation(this, R.anim.ttb)
        val recyclerview = findViewById(R.id.recycler_view) as RecyclerView
        recyclerview.startAnimation(ttb)
        initViews()
        initOperations()
        //initDB()
    }

    private fun initializeBannerAd(appUnitId: String) {

        MobileAds.initialize(this, appUnitId)

    }

    private fun loadBannerAd() {

        val adRequest = AdRequest.Builder()
            .addTestDevice("2AED00B50522F3E2626A4CDAB305B3D1")
            .build()

        mAdView.loadAd(adRequest)
    }

    fun initDB() {
        dbHandler = DatabaseHandler(this)
        listPlayer = (dbHandler as DatabaseHandler).getAllPlayers()
        playerRecyclerAdapter = PlayerRecyclerAdapter(playerList = listPlayer, context = applicationContext)
        (recyclerView as RecyclerView).adapter = playerRecyclerAdapter
    }

    fun initViews() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        fab = findViewById(R.id.fab) as FloatingActionButton
        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        playerRecyclerAdapter = PlayerRecyclerAdapter(playerList = listPlayer, context = applicationContext)
        linearLayoutManager = LinearLayoutManager(applicationContext)
        (recyclerView as RecyclerView).layoutManager = linearLayoutManager
    }

    fun initOperations() {
        fab?.setOnClickListener { view ->
            val i = Intent(applicationContext, AddOrEditActivity::class.java)
            i.putExtra("Mode", "A")
            startActivity(i)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_delete) {
            val dialog = AlertDialog.Builder(this).setTitle("Info").setMessage("Click 'YES' Delete All Players")
                .setPositiveButton("YES", { dialog, i ->
                    dbHandler!!.deleteAllPlayers()
                    initDB()
                    dialog.dismiss()
                })
                .setNegativeButton("NO", { dialog, i ->
                    dialog.dismiss()
                })
            dialog.show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        initDB()
    }
}
