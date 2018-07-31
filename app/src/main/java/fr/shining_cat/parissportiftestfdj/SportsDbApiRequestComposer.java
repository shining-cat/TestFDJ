package fr.shining_cat.parissportiftestfdj;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okio.Utf8;

public abstract class SportsDbApiRequestComposer {


    /*

List all Teams in a League
https://www.thesportsdb.com/api/v1/json/1/search_all_teams.php?l=English%20Premier%20League
https://www.thesportsdb.com/api/v1/json/1/search_all_teams.php?s=Soccer&c=Spain

Team Details by Id
https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id=133604

List All players in a team by Team Id
https://www.thesportsdb.com/api/v1/json/1/lookup_all_players.php?id=133604

Player Details by Id
https://www.thesportsdb.com/api/v1/json/1/lookupplayer.php?id=34145937

 */


    private static final String LIST_ALL_TEAMS_IN_LEAGUE_BASE_URL = "https://www.thesportsdb.com/api/v1/json/1/search_all_teams.php?l=";
    private static final String TEAM_DETAILS_BY_ID_BASE_URL = "https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id=";
    private static final String ALL_PLAYERS_IN_TEAM_BY_TEAM_ID = "https://www.thesportsdb.com/api/v1/json/1/lookup_all_players.php?id=";
    private static final String PLAYER_DETAILS_BY_ID = "https://www.thesportsdb.com/api/v1/json/1/lookupplayer.php?id=";

    public static String composeListAllTeamsInLeagueUrl(String leagueName){
        String url = null;
        try {
            url = LIST_ALL_TEAMS_IN_LEAGUE_BASE_URL +  URLEncoder.encode(leagueName,"utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("API REQUEST COMPOSER", "composeListAllTeamsInLeagueUrl:: error while encoding request part : " + e.toString());
            e.printStackTrace();
        }
        return url;
    }

    public static String composeListAllPlayersFromTeamIdUrl(int teamId){
        String url = ALL_PLAYERS_IN_TEAM_BY_TEAM_ID +  String.valueOf(teamId);
        return url;
    }



}
