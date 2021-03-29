package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe.R;
import com.example.recipe.showrecipes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import Model.favourites;
import Model.searchfavouritesmodel;
import Model.searchmodel;

public class searchFavourites extends RecyclerView.Adapter<searchFavourites.ViewHolder> {

    private final Context con;
    private ArrayList<searchfavouritesmodel> listitems;
    private DatabaseHandler dbh;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public searchFavourites(Context con, ArrayList<searchfavouritesmodel> listitems) {
        this.con = con;
        this.listitems = listitems;
        this.dbh=new DatabaseHandler(con);
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
    }

    @NonNull
    @Override
    public searchFavourites.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.favourites,parent,false);
        return new searchFavourites.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull searchFavourites.ViewHolder holder, int position) {
        searchfavouritesmodel fav=listitems.get(position);
        holder.textview.setText(fav.getRecipe_name1());
        Glide.with(con).load(fav.getImage_url()).into(holder.imgview);

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(con, showrecipes.class);
                i.putExtra("recipe_name",fav.getRecipe_name1());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                con.startActivity(i);
            }
        });

        holder.fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbh.getIngredient_fav(fav.getRecipe_name1(),firebaseUser.getUid())){
                    dbh.deleteingredient_fav(firebaseUser.getUid(),fav.getRecipe_name1());
                    holder.rlv.removeView(holder.cardview);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listitems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgview;
        TextView textview;
        Button fav_btn;
        CardView cardview;
        RelativeLayout rlv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rlv=itemView.findViewById(R.id.relativecard1);
            imgview=itemView.findViewById(R.id.roundedimageview);
            textview=itemView.findViewById(R.id.titletv);
            fav_btn=itemView.findViewById(R.id.favourites);
            cardview=itemView.findViewById(R.id.cardview);
        }
    }
}
