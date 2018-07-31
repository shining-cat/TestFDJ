package fr.shining_cat.parissportiftestfdj;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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


public class TeamsListFragment extends Fragment {

    public static final String TEAMS_LIST_FRAGMENT_TAG = "teams_list_Fragment-tag";

    private final String TAG = "LOGGING::" + this.getClass().getSimpleName();

    private static final String ARG_TEAM_SEARCH_REQUEST = "team search request parameter";

    private TeamsListFragmentListener mListener;

    private RequestQueue mRequestQueue;
    private String mRequestedTeamsList;
    private View mRootView;
    private RecyclerView mTeamsListRecyclerView;
    private TeamsListAdapter mTeamsListAdapter;
    private ArrayList<Team> mTeamsList;

    public TeamsListFragment() {}


    public static TeamsListFragment newInstance(String requestedTeamsList) {
        TeamsListFragment fragment = new TeamsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEAM_SEARCH_REQUEST, requestedTeamsList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRequestedTeamsList = getArguments().getString(ARG_TEAM_SEARCH_REQUEST);
        }
        mTeamsList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_teams_list, container, false);
        showLoadingTeamsMessage(true);
        showEmptyListMessageOrNot(false);
        //
        mTeamsListRecyclerView = mRootView.findViewById(R.id.teams_list_recyclerview);
        mTeamsListAdapter = new TeamsListAdapter(getActivity(), (TeamsListAdapter.TeamsListAdapterListener) getActivity());
        mTeamsListRecyclerView.setAdapter(mTeamsListAdapter);
        LinearLayoutManager recyclerLayoutManager = new GridLayoutManager(getActivity(), 2);
        mTeamsListRecyclerView.setHasFixedSize(true);
        mTeamsListRecyclerView.setLayoutManager(recyclerLayoutManager);
        //
        loadAndParseTeamsListJson();
        return mRootView;
    }

    private void loadAndParseTeamsListJson() {
        String requestedTeamPlayersListUrl = SportsDbApiRequestComposer.composeListAllTeamsInLeagueUrl(mRequestedTeamsList);
        Log.d(TAG, "loadAndParseTeamsListJson::requestedTeamPlayersListUrl = " + requestedTeamPlayersListUrl);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                                                        requestedTeamPlayersListUrl,
                                                        null,
                                                        onJsonTeamsListResponse,
                                                        onJsonTeamsListError);

        mRequestQueue.add(request);
    }

    private Response.Listener<JSONObject> onJsonTeamsListResponse = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {

                JSONArray jsonArray = response.getJSONArray("teams");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject oneTeam = jsonArray.getJSONObject(i);
                    //
                    int teamId = oneTeam.getInt("idTeam");
                    String teamName = oneTeam.getString("strTeam");
                    String teamPictureUrl = oneTeam.getString("strTeamBadge");
                    mTeamsList.add(new Team(teamId, teamName, teamPictureUrl));
                }
                if(mTeamsList.size() != 0) {
                    mTeamsListAdapter.setTeams(mTeamsList);
                    showLoadingTeamsMessage(false);
                    showEmptyListMessageOrNot(false);
                }else{
                    showLoadingTeamsMessage(false);
                    showEmptyListMessageOrNot(true);
                }

            } catch (JSONException e) {
                Log.e(TAG, "onJsonTeamsListResponse::onResponse:: There was an error receiving the json e = " + e.toString());
                e.printStackTrace();
                showLoadingTeamsMessage(false);
                showEmptyListMessageOrNot(true);
            }
        }
    };

    private Response.ErrorListener onJsonTeamsListError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "onJsonTeamsListError::onErrorResponse:: There was an error receiving the json e = " + error.toString());
            error.printStackTrace();
            mListener.onErrorLoadingTeamsDatas();
        }
    };


////////////////////////////////////////
//hiding or showing "loading teams message"
    private void showLoadingTeamsMessage(boolean showIt){
        TextView loadingTeams = mRootView.findViewById(R.id.loading_teams_list_message);
        if(showIt){
            loadingTeams.setVisibility(View.VISIBLE);
        }else{
            loadingTeams.setVisibility(View.GONE);
        }
    }

////////////////////////////////////////
//hiding or showing "no teams message"
    private void showEmptyListMessageOrNot(boolean showIt){
        TextView emptyListMessage = mRootView.findViewById(R.id.empty_teams_list_message);
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
        if (context instanceof TeamsListFragmentListener) {
            mListener = (TeamsListFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement TeamsListFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

////////////////////////////////////////
//Listener interface
    public interface TeamsListFragmentListener {
        void onErrorLoadingTeamsDatas();
    }
}
