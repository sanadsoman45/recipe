package Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import Model.Ingredients;
import Model.addtocart;
import Model.searchmodel;


public class searchadapter extends RecyclerView.Adapter<searchadapter.ViewHolder> {

    private final Context con;
    private ArrayList<searchmodel> listitems;
    private DatabaseHandler dbh;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private  static  final  String sharedprefmsg="myprefsfile";

    public searchadapter(Context con, ArrayList<searchmodel> listitems) {
        this.con = con;
        this.listitems = listitems;
        this.dbh=new DatabaseHandler(con);
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.search,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

         searchmodel lst=listitems.get(position);

        SharedPreferences shpret=con.getSharedPreferences(sharedprefmsg,0);
        String message=shpret.getString("fragment_name","notfound");
        Log.d("msgfrag","message is:"+message);

        if(shpret.contains("fragment_name"))
        {
            Log.d("item","item is:"+lst.getTitle());
            holder.tv.setText(lst.getTitle());
//            String message=shpret.getString("fragment_name","notfound");
            Log.d("msgtag",message);
            Log.d("msgtag","message is:"+message);

            if(message.equals("pantry")){
                Log.d("msgfrag1","inside pantry");

               if(dbh.getIngredient(lst.getTitle(),firebaseUser.getUid())){
                   holder.img_btn.setBackground(ContextCompat.getDrawable(con, R.drawable.ic_baseline_delete_24));
                   holder.img_btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(con,R.color.black)));
               }
               else{
                   holder.img_btn.setBackground(ContextCompat.getDrawable(con, R.drawable.ic_add));
                   holder.img_btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(con,R.color.colorgreen)));
               }



                holder.img_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(dbh.getIngredient(lst.getTitle(),firebaseUser.getUid())){
                            Toast.makeText(con, "Deleted is:"+lst.getTitle(), Toast.LENGTH_SHORT).show();
                            remove_ing(lst.getTitle(),firebaseUser.getUid());
                            holder.img_btn.setBackground(ContextCompat.getDrawable(con, R.drawable.ic_add));
                            holder.img_btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(con,R.color.colorgreen)));
                        }
                        else {
                            Toast.makeText(con, "Added is:"+lst.getTitle(), Toast.LENGTH_SHORT).show();
                            add_ing(lst.getTitle(),lst.getSection_name(),firebaseUser.getUid());
                            holder.img_btn.setBackground(ContextCompat.getDrawable(con, R.drawable.ic_baseline_delete_24));
                            holder.img_btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(con,R.color.black)));
                        }
                        Toast.makeText(con, "status of: "+lst.getTitle()+" is:"+dbh.getIngredient(lst.getTitle(),firebaseUser.getUid()), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            else if(message.equals("shoppinglist")){
                Log.d("msgfrag1","inside shopping list");
                if(dbh.getIngredient_cart(lst.getTitle(),firebaseUser.getUid())){
                   holder.img_btn.setBackground(ContextCompat.getDrawable(con, R.drawable.ic_baseline_delete_24));
                   holder.img_btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(con,R.color.black)));
               }
               else{
                   holder.img_btn.setBackground(ContextCompat.getDrawable(con, R.drawable.ic_add));
                   holder.img_btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(con,R.color.colorgreen)));
               }

                holder.img_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(dbh.getIngredient_cart(lst.getTitle(),firebaseUser.getUid())){
                            Log.d("condition","inside if");
                            holder.img_btn.setBackground(ContextCompat.getDrawable(con, R.drawable.ic_baseline_delete_24));
                            holder.img_btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(con,R.color.black)));
                            Log.d("msgdelete","Item before deletion is:"+dbh.getIngredient_cart(lst.getTitle(),firebaseUser.getUid()));
                            Toast.makeText(con, "Deleted is:"+lst.getTitle(), Toast.LENGTH_SHORT).show();
                            remove_ing_cart(lst.getTitle(),firebaseUser.getUid());
                            Log.d("msgdelete","Deleted item is:"+dbh.getIngredient_cart(lst.getTitle(),firebaseUser.getUid()));
                            holder.img_btn.setBackground(ContextCompat.getDrawable(con, R.drawable.ic_add));
                            holder.img_btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(con,R.color.colorgreen)));
                        }

                        else{
                            Log.d("condition","inside else");
                            holder.img_btn.setBackground(ContextCompat.getDrawable(con, R.drawable.ic_add));
                            holder.img_btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(con,R.color.colorgreen)));
                            Toast.makeText(con, "Added is:"+lst.getTitle(), Toast.LENGTH_SHORT).show();
                            add_ing_cart(lst.getTitle(),firebaseUser.getUid());
                            holder.img_btn.setBackground(ContextCompat.getDrawable(con, R.drawable.ic_baseline_delete_24));
                            holder.img_btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(con,R.color.black)));
                        }

                    }
                });

            }
        }

    }

    public void remove_ing_cart(String title,String user_id){
        dbh.deleteingredient_cart(new addtocart(title,user_id));
    }

    public void add_ing_cart(String title,String user_id){
        dbh.add_Ingredients_cart(new addtocart(title,"tobuy",user_id));
    }

    public void remove_ing(String title,String user_id){
        dbh.deleteingredient(new Ingredients(title,user_id));
    }

    public void add_ing(String title,String section_name,String user_id){
        dbh.addingredients(new Ingredients(title,section_name,user_id));
    }

    @Override
    public int getItemCount() {
        return listitems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final Button img_btn;
        private final TextView tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_btn=itemView.findViewById(R.id.imgview1);
            tv=itemView.findViewById(R.id.textview);
        }
    }
}
