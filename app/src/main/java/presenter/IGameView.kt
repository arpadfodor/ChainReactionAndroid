package presenter

/**
 * MVP view
 * GamePresenter calls the view via this interface
 */
interface IGameView {

    /**
     * Draws the selected Playground Field
     *
     * @param    pos_y       Y coordinate
     * @param    pos_x       X coordinate
     * @param    color       Color of the Field
     * @param    number      elements of the Field
     * @param    gifEnabled  whether moving image is enabled or not
     * @return   boolean     True if succeed, false otherwise
     */
    fun refreshPlayground(pos_y: Int, pos_x: Int, color: Int, number: Int, gifEnabled: Boolean): Boolean

    /**
     * Shows whose turn is now
     *
     * @param    Id          Id of the current Player
     * @return    boolean     True if succeed, false otherwise
     */
    fun showCurrentPlayer(Id: Int): Boolean

    /**
     * Shows a message from the Presenter
     *
     * @param    msg         Message
     * @return    boolean     True if succeed, false otherwise
     */
    fun showMessage(msg: String): Boolean

    /**
     * Shows the result of the game play, displays GameOverFragment, writes the database
     *
     * @param     winnerId          Id of the winner
     * @param     playersData       Players data. [i] is the Player index, [][0] is Player Id, [][1] is the average step time of Player, [][2] is the number of rounds of Player, [][3] is the type of the Player (1:human, 2:AI)
     * @param     humanVsAiGame     True is human and AI played in the game, false otherwise
     * @return    boolean           True if succeed, false otherwise
     */
    fun showResult(winnerId: Int, playersData: Array<IntArray>, humanVsAiGame: Boolean): Boolean

    /**
     * Shows the start text from the Presenter
     *
     * @param       Id          Id of the current Player
     * @return      boolean     True if succeed, false otherwise
     */
    fun showStart(Id: Int): Boolean

    /**
     * Refresh the progress bar state with the given value
     *
     * @param    value       The progress bar state value - between 0 and 100
     */
    fun refreshProgressBar(value: Int)

}