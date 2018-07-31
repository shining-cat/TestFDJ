package fr.shining_cat.parissportiftestfdj;

public class Player {

    private String mPlayerPictureUrl;
    private String mPlayerName;
    private int mPlayerId;
    private String mPlayerPosition;
    private String mPlayerBirthDate;
    private String mPlayerTransferAmount;

    public Player(int id, String pictureUrl, String name, String position, String birthDate, String transferAmount){
        mPlayerPictureUrl = pictureUrl;
        mPlayerName = name;
        mPlayerId = id;
        mPlayerPosition = position;
        mPlayerBirthDate = birthDate;
        mPlayerTransferAmount = transferAmount;
    }

    public String getPlayerPictureUrl() {return mPlayerPictureUrl;}
    public String getPlayerName() {return mPlayerName;}
    public int getPlayerId() {return mPlayerId;}
    public String getPlayerPosition() {return mPlayerPosition;}
    public String getPlayerBirthDate() {return mPlayerBirthDate;}
    public String getPlayerTransferAmount() {return mPlayerTransferAmount;}

}