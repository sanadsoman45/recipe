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
import com.example.recipe.Favouriteinterface;
import com.example.recipe.R;
import com.example.recipe.showrecipes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import Model.favourites;
import Model.menumodel;

public class Favouritesadapter extends RecyclerView.Adapter<Favouritesadapter.ViewHolder> {

    private final Context con;
    private ArrayList<favourites> listitems;
    private DatabaseHandler dbh;
    Favouriteinterface favinterf;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public Favouritesadapter(Context con, ArrayList<favourites> listitems,Favouriteinterface favinterf) {
        this.con = con;
        this.favinterf=favinterf;
        this.listitems = listitems;
        this.dbh=new DatabaseHandler(con);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.favourites,parent,false);
        return new Favouritesadapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        favourites fav=listitems.get(position);
        holder.textview.setText(fav.getRecipe_name());
        Glide.with(con).load(fav.getImage_menufrag()).into(holder.imgview);
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(con, showrecipes.class);
                i.putExtra("recipe_name",fav.getRecipe_name());
                con.startActivity(i);
            }
        });
        holder.fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbh.getIngredient_fav(fav.getRecipe_name(),firebaseUser.getUid())){
                    dbh.deleteingredient_fav(firebaseUser.getUid(),fav.getRecipe_name());
                    holder.rlv.removeView(holder.cardview);
                    favinterf.setcount(dbh.fav_table_count(firebaseUser.getUid()));
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
