package view

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer
import com.google.android.gms.ads.AdView
import hu.bme.aut.android.chainreaction.R
import model.db.DbDefaults
import model.db.challenge.ChallengeDatabase
import model.db.challenge.ChallengeLevel
import presenter.LevelSlidePagerAdapter
import presenter.AdPresenter
import presenter.IStartChallengeView

/**
 * Activity of starting a challenge game play
 */
class StartChallengeActivity : AppCompatActivity(), IStartChallengeView {

    companion object {
        private const val CHALLENGE_GAME = 2

        private const val NORMAL_MODE = 1
        private const val DYNAMIC_MODE = 2
    }

    private var gameType = CHALLENGE_GAME
    private var gameMode = NORMAL_MODE

    /**
     * The pager adapter
     */
    private lateinit var pagerAdapter: LevelSlidePagerAdapter

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous and next fragments
     */
    private lateinit var mPager: ViewPager

    /**
     * The pager titles
     */
    private lateinit var mPagerTitles: TabLayout

    /**
     * Advertisement of the activity
     */
    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_start_challenge)

        // The pager adapter, which provides the pages to the view pager widget
        pagerAdapter = LevelSlidePagerAdapter(this, this, supportFragmentManager)
        //Instantiate a ViewPager
        mPager = findViewById(R.id.levelsPager)
        //bring to front to make snack bar visible even when ad is shown
        mPager.bringToFront()
        //Instantiate the titles shown in the ViewPager
        mPagerTitles = findViewById(R.id.levelPagerTitles)
        mPagerTitles.setupWithViewPager(mPager)

        mAdView = findViewById(R.id.startChallengeAdView)
        //loading the advertisement
        AdPresenter.loadAd(mAdView)

    }

    /**
     * Reads the challenge database, saves it into a list, then passes it to the view
     */
    override fun challengeDatabaseReader() {

        val dbChallenge = Room.databaseBuilder(applicationContext, ChallengeDatabase::class.java, "db_challenge").build()
        var challengeLevels : MutableList<ChallengeLevel>

        Thread {

            challengeLevels = dbChallenge.challengeLevelsDAO().getAll().toMutableList()

            if(challengeLevels.isEmpty()){
                val defaultCampaignStates = DbDefaults.challengeDatabaseDefaults()
                for(level in defaultCampaignStates){
                    dbChallenge.challengeLevelsDAO().insert(level)
                }
                challengeLevels = dbChallenge.challengeLevelsDAO().getAll().toMutableList()
            }

            dbChallenge.close()

            runOnUiThread {
                pagerAdapter.setChallengeLevels(challengeLevels)
                mPager.adapter = pagerAdapter
                mPager.setPageTransformer(true, CubeOutTransformer())
            }

        }.start()

    }

    /**
     * Called when leaving the activity - stops the presenter calculations too
     */
    override fun onPause() {
        mAdView.pause()
        super.onPause()
    }

    /**
     * Called when returning to the activity
     */
    override fun onResume() {
        super.onResume()
        mAdView.resume()
    }

    /**
     * Called before the activity is destroyed
     */
    override fun onDestroy() {
        mAdView.destroy()
        super.onDestroy()
    }

}