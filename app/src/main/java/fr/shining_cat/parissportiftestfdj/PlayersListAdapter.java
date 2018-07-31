package fr.shining_cat.parissportiftestfdj;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PlayersListAdapter extends RecyclerView.Adapter<PlayersListAdapter.TeamViewHolder>{

    private final String TAG = "LOGGING::" + this.getClass().getSimpleName();

    public class TeamViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPlayerPicture;
        private TextView mPlayerName;
        private TextView mPlayerPosition;
        private TextView mPlayerBirhtDate;
        private TextView mPlayerTransferAmount;

        TeamViewHolder(View itemView) {
            super(itemView);
            mPlayerPicture = itemView.findViewById(R.id.player_picture);
            mPlayerName = itemView.findViewById(R.id.player_name);
            mPlayerPosition = itemView.findViewById(R.id.player_position);
            mPlayerBirhtDate = itemView.findViewById(R.id.player_birthdate);
            mPlayerTransferAmount = itemView.findViewById(R.id.player_transfer_amount);
        }


    }

    private Context mContext;
    private final LayoutInflater mInflater;
    private List<Player> mPlayers; // Cached copy of teams list

    public PlayersListAdapter(Context context){
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.player_item, parent, false);
        return new TeamViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TeamViewHolder holder, int position) {
        if(mPlayers !=null){
            Player currentPlayer = mPlayers.get(holder.getAdapterPosition());
            //
            Picasso.get().
                    load(currentPlayer.getPlayerPictureUrl()).
                    placeholder(R.mipmap.face_placeholder).
                    fit().
                    centerInside().
                    into(holder.mPlayerPicture);
            holder.mPlayerName.setText(currentPlayer.getPlayerName());
            holder.mPlayerPosition.setText(currentPlayer.getPlayerPosition());
            holder.mPlayerBirhtDate.setText(currentPlayer.getPlayerBirthDate());
            holder.mPlayerTransferAmount.setText(currentPlayer.getPlayerTransferAmount());
        }else{
            //data not ready yet
            Log.d(TAG, "onBindViewHolder::data not available!");
        }
    }

    @Override
    public int getItemCount() {
        if(mPlayers != null){
            return mPlayers.size();
        }else {
            return 0;
        }
    }

////////////////////////////////////////
//update data content
    public void setTeams(List<Player> players){
        mPlayers = players;
        Log.d(TAG, "setTeams::number of teams = " + mPlayers.size());
        notifyDataSetChanged();
    }


}
