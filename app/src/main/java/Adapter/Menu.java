package Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe.Datatransferinterface;
import com.example.recipe.R;
import com.example.recipe.showrecipes_normal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Model.menumodel;


public class Menu extends RecyclerView.Adapter<Menu.ViewHolder> {

    private final Context con;
    private ArrayList<menumodel> listitems;
    private DatabaseHandler dbh;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    ArrayList<String> arr_recipe_names;


    public Menu(Context con, ArrayList<menumodel> listitems) {
        this.con = con;
        this.listitems = listitems;
        arr_recipe_names=new ArrayList<>();
        this.dbh=new DatabaseHandler(con);
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
    }

    @NonNull
    @Override
    public Menu.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.menucards,parent,false);
        return new Menu.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Menu.ViewHolder holder, int position) {
        menumodel lst=listitems.get(position);
        Log.d("menutag",lst.getRecipe_name());
        Log.d("msgarr1","Arraylist is:"+lst.getRecipe_name());
        Log.d("msgarr1","Arraylist is:"+arr_recipe_names.contains(lst.getRecipe_name()));
        holder.textview.setText(lst.getRecipe_name());
        Glide.with(con).load(lst.getImage_menufrag()).into(holder.imgview);
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(con, showrecipes_normal.class);
                i.putExtra("recipe_name",lst.getRecipe_name());
                con.startActivity(i);
            }
        });

       if(!firebaseUser.isAnonymous()){
           if(dbh.getIngredient_fav(lst.getRecipe_name(),firebaseUser.getUid())){
               holder.fav_btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(con,R.color.heartpink)));
           }
           else{
               holder.fav_btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(con,R.color.black)));
           }
           holder.fav_btn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(!dbh.getIngredient_fav(lst.getRecipe_name(),firebaseUser.getUid())){
                       dbh.addtofav(new menumodel(firebaseUser.getUid(),lst.getRecipe_name(),lst.getIngredients(),lst.getInstruction(),lst.getCook_time(),lst.getPrep_time(),lst.getImage_menufrag(),lst.getTotal_time()));
                       holder.fav_btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(con,R.color.heartpink)));
                       Toast.makeText(con, "Addded to Fav", Toast.LENGTH_SHORT).show();
                   }
                   else{
                       holder.fav_btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(con,R.color.black)));
                       dbh.deleteingredient_fav(firebaseUser.getUid(),lst.getRecipe_name());
                       Toast.makeText(con, "Deleted from fav", Toast.LENGTH_SHORT).show();
                   }
               }
           });
       }
       else{
           holder.fav_btn.setVisibility(View.GONE);
       }
        arr_recipe_names.add(lst.getRecipe_name());

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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgview=itemView.findViewById(R.id.roundedimageview);
            textview=itemView.findViewById(R.id.titletv);
            fav_btn=itemView.findViewById(R.id.favourites);
            cardview=itemView.findViewById(R.id.cardview);
        }
    }
}
