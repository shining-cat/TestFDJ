package fr.shining_cat.parissportiftestfdj;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TeamsListAdapter  extends RecyclerView.Adapter<TeamsListAdapter.TeamViewHolder>{

    private final String TAG = "LOGGING::" + this.getClass().getSimpleName();

    public class TeamViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        private ImageView mTeamPicture;

        TeamViewHolder(View itemView) {
            super(itemView);
            mTeamPicture = itemView.findViewById(R.id.team_picture);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClickOnTeam(mTeams.get(this.getAdapterPosition()));
        }
    }

    private Context mContext;
    private final LayoutInflater mInflater;
    private List<Team> mTeams; // Cached copy of teams list
    private TeamsListAdapterListener mListener;

    public TeamsListAdapter(Context context, TeamsListAdapterListener teamsListAdapterListener){
        mContext = context;
        mInflater = LayoutInflater.from(context);
        if (teamsListAdapterListener instanceof TeamsListAdapterListener) {
            mListener = teamsListAdapterListener;
        } else {
            throw new RuntimeException(teamsListAdapterListener.toString()+ " must implement PlayersListAdapterListener");
        }
    }

    @Override
    public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.team_item, parent, false);
        return new TeamViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TeamViewHolder holder, int position) {
        if(mTeams !=null){
            Team currentTeam = mTeams.get(holder.getAdapterPosition());
            //
            Picasso.get().
                    load(currentTeam.getTeamPictureUrl()).
                    placeholder(R.mipmap.team_placeholder).
                    fit().
                    centerInside().
                    into(holder.mTeamPicture);
        }else{
            //data not ready yet
            Log.d(TAG, "onBindViewHolder::data not available!");
        }
    }

    @Override
    public int getItemCount() {
        if(mTeams != null){
            return mTeams.size();
        }else {
            return 0;
        }
    }

////////////////////////////////////////
//update data content
    public void setTeams(List<Team> teams){
        mTeams = teams;
        Log.d(TAG, "setTeams::number of teams = " + mTeams.size());
        notifyDataSetChanged();
    }

////////////////////////////////////////
//Listener interface
    public interface TeamsListAdapterListener {
        void onClickOnTeam(Team clickedTeam);
    }
}
