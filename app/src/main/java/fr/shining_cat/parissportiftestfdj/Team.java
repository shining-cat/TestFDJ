package fr.shining_cat.parissportiftestfdj;

public class Team {

    private String mTeamPictureUrl;
    private String mTeamName;
    private int mTeamId;

    public Team(int id, String name, String pictureUrl){
        mTeamPictureUrl = pictureUrl;
        mTeamName = name;
        mTeamId = id;
    }

    public String getTeamPictureUrl() {return mTeamPictureUrl;}
    public String getTeamName() {return mTeamName;}
    public int getTeamId() {return mTeamId;}

}
