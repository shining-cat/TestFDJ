package fr.shining_cat.parissportiftestfdj;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import static android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT;

public class MainActivity   extends AppCompatActivity
                            implements  TeamsListAdapter.TeamsListAdapterListener,
                                        TeamsListFragment.TeamsListFragmentListener,
                                        PlayersListFragment.PlayersListFragmentListener {

    private final String TAG = "LOGGING::" + this.getClass().getSimpleName();

    private final static String CURRENT_SCREEN_EMPTY        = "current screen is empty";
    private final static String CURRENT_SCREEN_TEAMS_LIST   = "current screen is teams list";
    private final static String CURRENT_SCREEN_PLAYERS_LIST = "current screen is players list";

    private SearchView mSearchView;
    private ImageView mCloseSearchButton;
    private String mTeamsListRequested;
    private String mCurrentScreen;


    //PB : the leagues' names seem to me to be a bit different from what the user would look for
    //and the API does not handle real search methods, but relies only on exact matches...
    //(example : "FRENCH LIGUE 1", or "SPANISH LA LIGA" instead of the mockup examples "ligue 1" or "liga")
    //But the searchview auto-suggest functionality needs a data provider to work and cannot be simply plugged to the result of a JSON request
    //in our case, the sportDB API could return a list of all leagues strings but we would have to insert them in a DB and provide them through a provider...
    //this seems a bit out of the scope of this exercise

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchView = this.findViewById(R.id.searchView);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setIconified(false);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint(getString(R.string.search_view_hint));
        mSearchView.setOnQueryTextListener(onSearchClickListener);
        //
        mCloseSearchButton = mSearchView.findViewById(R.id.search_close_btn);
        updateSearchViewDeleteButton(false);
        //
        mCurrentScreen = CURRENT_SCREEN_EMPTY;
    }

    private SearchView.OnQueryTextListener onSearchClickListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            mTeamsListRequested = query;
            Log.d(TAG, "onSearchClickListener::onClick::mTeamsListRequested = " + mTeamsListRequested);
            hideSoftKeyboard();
            displayTeamsList();
            return true;
        }
        @Override
        public boolean onQueryTextChange(String newText) {
            if(!newText.isEmpty())updateSearchViewDeleteButton(true);
            return true;
        }
    };

    private View.OnClickListener onCloseBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            reset();
        }
    };

    private void updateSearchViewDeleteButton(boolean showBtn){
        if(!showBtn) {
            mCloseSearchButton.setEnabled(false);
            mCloseSearchButton.setVisibility(View.GONE);
        }else{
            mCloseSearchButton.setEnabled(true);
            mCloseSearchButton.setVisibility(View.VISIBLE);
            mCloseSearchButton.setOnClickListener(onCloseBtnListener);
        }
    }

////////////////////////////////////////
//3 MAIN SCREENS
    private void reset(){
        mCurrentScreen = CURRENT_SCREEN_EMPTY;
        for (Fragment fragment:getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        mSearchView.setQuery("", false);
        mSearchView.setQueryHint(getString(R.string.search_view_hint));
        updateSearchViewDeleteButton(false);
        mSearchView.requestFocus();
        showSoftKeyboard();
    }

    private void displayTeamsList(){
        mCurrentScreen = CURRENT_SCREEN_TEAMS_LIST;
        //
        this.setTitle(getString(R.string.app_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mSearchView.setVisibility(View.VISIBLE);
        //
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TeamsListFragment teamsListFragment = TeamsListFragment.newInstance(mTeamsListRequested);
        fragmentTransaction.replace(R.id.main_activity_fragments_holder, teamsListFragment, TeamsListFragment.TEAMS_LIST_FRAGMENT_TAG);
        fragmentTransaction.commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void displayPlayersList(int teamId, String teamName){
        mCurrentScreen = CURRENT_SCREEN_PLAYERS_LIST;
        //
        this.setTitle(teamName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSearchView.setVisibility(View.GONE);
        //
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PlayersListFragment playersListFragment = PlayersListFragment.newInstance(teamId);
        fragmentTransaction.replace(R.id.main_activity_fragments_holder, playersListFragment, PlayersListFragment.PLAYERS_LIST_FRAGMENT_TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

////////////////////////////////////////
//BACK NAVIGATIION
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case android.R.id.home:
                overrideBackNavigations();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        overrideBackNavigations();
    }

    private void overrideBackNavigations(){
        if(mCurrentScreen.equals(CURRENT_SCREEN_PLAYERS_LIST)){
            displayTeamsList();
        }else if(mCurrentScreen.equals(CURRENT_SCREEN_TEAMS_LIST)){
            reset();
        }else{
            super.onBackPressed();
        }
    }

////////////////////////////////////////
//display an alert dialog with error notification
    private void displayError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error_title);
        builder.setMessage(R.string.error_retrieving_infos_message);
        builder.setNegativeButton(R.string.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

////////////////////////////////////////
//TeamsListAdapter callbacks
    @Override
    public void onClickOnTeam(Team clickedTeam) {
        int requestedTeamId = clickedTeam.getTeamId();
        String requestedTeamName = clickedTeam.getTeamName();
        Log.d(TAG, "onClickOnTeam::requestedTeamId = " + requestedTeamId + " / requestedTeamName = " + requestedTeamName);
        displayPlayersList(requestedTeamId, requestedTeamName);
    }

    @Override
    public void onErrorLoadingTeamsDatas() {
        displayError();
    }

////////////////////////////////////////
//TeamsListAdapter callbacks
    @Override
    public void onErrorLoadingPlayersDatas() {
        displayError();
    }

////////////////////////////////////////
//hide / show soft keyboard
    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View focusedView = getCurrentFocus();
            if(focusedView!=null) {
                inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
            }
        }
    }

    private void showSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View focusedView = getCurrentFocus();
            if(focusedView!=null) {
                inputMethodManager.showSoftInput(focusedView, SHOW_IMPLICIT);
            }
        }
    }

}
