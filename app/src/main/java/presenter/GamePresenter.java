package presenter;

import model.GamePlay;
import presenter.task.GameLogicTask;

import java.util.ArrayList;

/**
 * presenter of a game play
 * Controls what and how to be displayed on the view
 * Mediator between model and view
 */
public class GamePresenter {

    /**
     * MVP model
     */
    private IGameModel model;

    /**
     * MVP view
     */
    private IGameView view;

    /**
     * AsyncTask to execute model computations
     */
    private GameLogicTask game_task;

    /**
     * Unit in milliseconds to refresh propagation state
     */
    private Boolean showPropagation;

    /**
     * GamePresenter constructor
     * Sets the view, the Players, the model, starts the game play
     *
     * @param   view 	        IGameView object that holds the GamePresenter
     * @param   height          height of the Playground
     * @param   width 	        width of the Playground
     * @param   players_input   ArrayList of Strings; "<Player type>-<Player name>" format
     */
    public GamePresenter(IGameView view, int height, int width, ArrayList<String> players_input, Boolean showPropagation){

        this.view = view;
        this.showPropagation = showPropagation;

        int number_of_players = players_input.size();

        ArrayList<String[]> players = new ArrayList<String[]>();

        for(int i = 0; i < number_of_players; i++){

            String[] input_data = players_input.get(i).split("-");

            String[] player_data = new String[3];
            player_data[0] = String.valueOf(i+1);
            player_data[1] = input_data[1];
            player_data[2] = input_data[0];

            players.add(player_data);

        }

        this.model = new GamePlay(this, height, width, players);

        game_task = new GameLogicTask(model, this, showPropagation);
        game_task.execute();

    }

    /**
     * Requests a step to the Field described with the input parameters in a new task
     * Refreshes the view if the step is valid
     *
     * @param   pos_y	   Y coordinate
     * @param   pos_x      X coordinate
     */
    public void StepRequest(int pos_y, int pos_x){

        game_task = new GameLogicTask(model, this, showPropagation);
        game_task.execute(pos_y, pos_x);

    }

    /**
     * Refreshes the Playground and tells the view to draw it
     *
     * @param   current_player_Id     Id of the current Player
     */
    public void RefreshPlayground(int current_player_Id, int propagation_depth){

        int[] dimension = model.GetDimension();
        int[][][] state_matrix = new int[dimension[0]][dimension[1]][];

        if(current_player_Id == 0 && !model.IsGameEnded()){

            view.ShowMessage("Invalid click!");
            return;

        }

        view.ShowCurrentPlayer(Math.abs(current_player_Id));

        if(model.IsGameEnded()){

            view.ShowResult("Player " + Math.abs(model.getActualPlayerId()) + " is the winner!");

        }

        if(propagation_depth >= 0 && propagation_depth < model.GetReactionPropagationDepth()){

            state_matrix = model.HistoryPlaygroundInfoAt(propagation_depth);

        }

        else{

            state_matrix = model.ActualPlaygroundInfo();
            int tmp = 6;

        }

        for(int actual_height = 0; actual_height < dimension[0]; actual_height++){

            for(int actual_width = 0; actual_width < dimension[1]; actual_width++){

                view.RefreshPlayground(actual_height, actual_width,
                        PlayerColor.GetColorById(state_matrix[actual_height][actual_width][0]),
                        state_matrix[actual_height][actual_width][1]);

            }

        }

    }

    /**
     * Stops immediately GamePresenter computations
     *
     * @return 	Boolean     True if stopped, false otherwise
     */
    public Boolean stopPresenter(){

        if(game_task != null){

            return game_task.cancel(true);

        }

        else{

            return true;

        }

    }

}