package com.arpadfodor.android.chain_reaction.presenter.task

import com.arpadfodor.android.chain_reaction.model.ai.PlayerLogic
import android.content.Context
import android.os.AsyncTask
import com.arpadfodor.android.chain_reaction.R
import org.deeplearning4j.util.ModelSerializer
import com.arpadfodor.android.chain_reaction.presenter.IMainView

import java.io.IOException

/**
 * Async task to load AI component
 */
class AILoaderTask
/**
 * GameLogicTask constructor
 *
 * @param   context         Context of the UI thread
 */
    (
    /**
     * The caller activity
     */
    val view: IMainView,
    /**
     * Context of the UI thread
     */
    val context: Context
) : AsyncTask<Void, Int, Boolean>() {

    /**
     * Loads and sets the Neural Network of PlayerLogic
     *
     * @param   params      none
     * @return  Boolean 	True when AI component loading is finished
     */
    override fun doInBackground(vararg params: Void): Boolean? {

        val inStream = context.resources.openRawResource(R.raw.player_neural_network)

        try {
            val network = ModelSerializer.restoreMultiLayerNetwork(inStream)
            PlayerLogic.SetNeuralNetwork(network)
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

        return true

    }

    /**
     * Accesses the UI thread - notifies the user about AI loading result
     *
     * @param   result      Result of doInBackground
     */
    override fun onPostExecute(result: Boolean) {
        view.loadedAI(result)
    }

    override fun onPreExecute() {
        view.notLoadedAI()
    }

    override fun onProgressUpdate(vararg values: Int?) {}

}
