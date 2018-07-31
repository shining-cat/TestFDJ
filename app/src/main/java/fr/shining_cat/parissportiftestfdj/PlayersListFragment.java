package fr.shining_cat.parissportiftestfdj;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PlayersListFragment extends Fragment {

    public static final String PLAYERS_LIST_FRAGMENT_TAG = "players_list_Fragment-tag";

    private final String TAG = "LOGGING::" + this.getClass().getSimpleName();

    private static final String ARG_TEAM_REQUESTED_ID   = "requested team id parameter";

    private PlayersListFragmentListener mListener;

    private View mRootView;
    private RecyclerView mPlayersListRecyclerView;
    private PlayersListAdapter mPlayersListAdapter;
    private ArrayList<Player> mPlayersList;
    private int mRequestedTeamId;
    private RequestQueue mRequestQueue;


    public PlayersListFragment() {}


    public static PlayersListFragment newInstance(int teamId) {
        PlayersListFragment fragment = new PlayersListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TEAM_REQUESTED_ID, teamId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRequestedTeamId = getArguments().getInt(ARG_TEAM_REQUESTED_ID);
        }
        mPlayersList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_players_list, container, false);
        showLoadingPlayersMessage(true);
        showEmptyListMessageOrNot(false);
        //
        mPlayersListRecyclerView = mRootView.findViewById(R.id.players_list_recyclerview);
        mPlayersListAdapter = new PlayersListAdapter(getActivity());
        mPlayersListRecyclerView.setAdapter(mPlayersListAdapter);
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(getActivity());
        mPlayersListRecyclerView.setHasFixedSize(true);
        mPlayersListRecyclerView.setLayoutManager(recyclerLayoutManager);
        //
        loadAndParsePlayersListJson();
        return mRootView;
    }



    private void loadAndParsePlayersListJson() {
        String requestedTeamPlayersListUrl = SportsDbApiRequestComposer.composeListAllPlayersFromTeamIdUrl(mRequestedTeamId);
        Log.d(TAG, "loadAndParseTeamsListJson::requestedTeamPlayersListUrl = " + requestedTeamPlayersListUrl);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                requestedTeamPlayersListUrl,
                null,
                onJsonPlayersListResponse,
                onJsonPlayersListError);

        mRequestQueue.add(request);
    }

    private Response.Listener<JSONObject> onJsonPlayersListResponse = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {

                JSONArray jsonArray = response.getJSONArray("player");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject onePlayer = jsonArray.getJSONObject(i);
                    //
                    int playerId = onePlayer.getInt("idPlayer");
                    String playerPictureUrl = onePlayer.getString("strCutout");
                    String playerName = onePlayer.getString("strPlayer");
                    String playerPosition = onePlayer.getString("strPosition");
                    String playerBirthDate = onePlayer.getString("dateBorn");
                    String playerTransferAmount = onePlayer.getString("strSigning");
                    mPlayersList.add(new Player(playerId, playerPictureUrl, playerName, playerPosition, playerBirthDate, playerTransferAmount));
                }
                if(mPlayersList.size() != 0) {
                    mPlayersListAdapter.setTeams(mPlayersList);
                    showLoadingPlayersMessage(false);
                    showEmptyListMessageOrNot(false);
                }else{
                    showLoadingPlayersMessage(false);
                    showEmptyListMessageOrNot(true);
                }

            } catch (JSONException e) {
                Log.e(TAG, "onJsonPlayersListResponse::onResponse:: There was an error receiving the json e = " + e.toString());
                e.printStackTrace();
                showLoadingPlayersMessage(false);
                showEmptyListMessageOrNot(true);
            }
        }
    };

    private Response.ErrorListener onJsonPlayersListError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "onJsonPlayersListError::onErrorResponse:: There was an error receiving the json e = " + error.toString());
            error.printStackTrace();
            mListener.onErrorLoadingPlayersDatas();
        }
    };


////////////////////////////////////////
//hiding or showing "loading players message"
    private void showLoadingPlayersMessage(boolean showIt){
        TextView loadingPlayers = mRootView.findViewById(R.id.loading_players_list_message);
        if(showIt){
            loadingPlayers.setVisibility(View.VISIBLE);
        }else{
            loadingPlayers.setVisibility(View.GONE);
        }
    }

////////////////////////////////////////
//hiding or showing "no players message"
    private void showEmptyListMessageOrNot(boolean showIt){
        TextView emptyListMessage = mRootView.findViewById(R.id.empty_players_list_message);
        if(showIt){
            emptyListMessage.setVisibility(View.VISIBLE);
        }else{
            emptyListMessage.setVisibility(View.GONE);
        }
    }

////////////////////////////////////////
//plugging interface listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PlayersListFragmentListener) {
            mListener = (PlayersListFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()+ " must implement PlayersListFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

////////////////////////////////////////
//Listener interface
    public interface PlayersListFragmentListener {
        void onErrorLoadingPlayersDatas();
    }

}
