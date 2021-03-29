package Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe.Datainterface_shoppinglist;
import com.example.recipe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import Model.Ingredients;
import Model.addtocart;
import Model.addtocartlist;
import Utils.Util;

public class cartitems extends RecyclerView.Adapter<cartitems.ViewHolder> {

    private final Context con;
    private ArrayList<addtocartlist> listitems;
    private DatabaseHandler dbh;
    Datainterface_shoppinglist dtf;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public cartitems(Context con, ArrayList<addtocartlist> listitems, Datainterface_shoppinglist dtfshoppinglist) {
        this.con = con;
        this.listitems = listitems;
        this.dbh=new DatabaseHandler(con);
        this.dtf=dtfshoppinglist;
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
    }

    @NonNull
    @Override
    public cartitems.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.shoppingcart,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull cartitems.ViewHolder holder, int position) {
        addtocartlist addtocart=listitems.get(position);
        Log.d("msgtag","Size is:"+addtocart.getbtn_size());
        for(int i=0;i<addtocart.getbtn_size();i++){
            RelativeLayout sub_relative_item_lay= new RelativeLayout(con);
            RelativeLayout.LayoutParams sub_item_params= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            sub_item_params.setMargins(15, 10, 15, 0);

            CheckBox sub_item_name= new CheckBox(con);
            sub_item_name.setText(addtocart.getBtn(i));

            Log.d("msgtag","string is:"+sub_item_name.getText());
            RelativeLayout.LayoutParams sub_item_name_params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            sub_item_name.setId(R.id.item_name);



            Button sub_item_delete_btn= new Button(con);
            RelativeLayout.LayoutParams sub_item_delete_btn_params= new RelativeLayout.LayoutParams(70, 70);
            sub_item_delete_btn.setId(R.id.delete_btn);

            View sub_item_line= new View(con);
            RelativeLayout.LayoutParams sub_item_line_params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            sub_item_line.setId(R.id.item_view_line);

            sub_item_name_params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            sub_item_name.setLayoutParams(sub_item_name_params);



            sub_item_delete_btn_params.setMargins(10, 0, 10, 0);
            sub_item_delete_btn_params.addRule(RelativeLayout.ALIGN_PARENT_END);
            sub_item_delete_btn.setBackground(ContextCompat.getDrawable(con, R.drawable.ic_baseline_delete_24));
            sub_item_delete_btn.setLayoutParams(sub_item_delete_btn_params);

            sub_item_line_params.addRule(RelativeLayout.BELOW, sub_item_name.getId());
            sub_item_line_params.setMargins(0, 5, 0, 0);
            sub_item_line.setBackgroundColor(Color.parseColor("#979797"));
            sub_item_line.setLayoutParams(sub_item_line_params);

            sub_relative_item_lay.addView(sub_item_name);

            sub_relative_item_lay.addView(sub_item_delete_btn);
            sub_relative_item_lay.addView(sub_item_line);
            if(dbh.get_ingredient_type(sub_item_name.getText().toString(), firebaseUser.getUid()).equals("tobuy")){
                Log.d("msgtag","ans is:"+sub_relative_item_lay.getParent()+" "+sub_item_name.getText().toString());
                holder.whatsinmylist.addView(sub_relative_item_lay);
                sub_item_name.setPaintFlags(sub_item_name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                Log.d("msgtag","ans1 is:"+sub_relative_item_lay.getParent().toString()+" "+sub_item_name.getText().toString());
            }
            else if(dbh.get_ingredient_type(sub_item_name.getText().toString(), firebaseUser.getUid()).equals("purchased")){
                Log.d("msgtag","ans is:"+sub_relative_item_lay.getParent()+" "+sub_item_name.getText().toString());
                holder.whatigot.addView(sub_relative_item_lay);
                sub_item_name.setPaintFlags(sub_item_name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                Log.d("msgtag","ans1 is:"+sub_relative_item_lay.getParent().toString()+" "+sub_item_name.getText().toString());
            }

            sub_item_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sub_item_name.setChecked(false);
                    if (dbh.get_ingredient_type(sub_item_name.getText().toString(), firebaseUser.getUid()).equals("tobuy")) {
                        dbh.update("purchased", sub_item_name.getText().toString(),firebaseUser.getUid());
                        Log.d("msgtag", "value is:" + dbh.get_ingredient_type(sub_item_name.getText().toString(),firebaseUser.getUid()));
                        Log.d("msgtag","ans is:"+sub_relative_item_lay.getParent().toString());
                        Log.d("msgtag","ans is:"+sub_relative_item_lay.getParent()+" "+sub_item_name.getText().toString());
                        holder.whatsinmylist.removeView(sub_relative_item_lay);
                        holder.whatigot.addView(sub_relative_item_lay);
                        sub_item_name.setPaintFlags(sub_item_name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        Log.d("msgtag","ans1 is:"+sub_relative_item_lay.getParent().toString()+" "+sub_item_name.getText().toString());
                    }
                    else if (dbh.get_ingredient_type(sub_item_name.getText().toString(), firebaseUser.getUid()).equals("purchased")) {
                        dbh.update("tobuy", sub_item_name.getText().toString(), firebaseUser.getUid());
                        Log.d("msgtag", "value is:" + dbh.get_ingredient_type(sub_item_name.getText().toString(), firebaseUser.getUid()));
                        Log.d("msgtag","ans is:"+sub_relative_item_lay.getParent()+" "+sub_item_name.getText().toString());
                        holder.whatigot.removeView(sub_relative_item_lay);
                        holder.whatsinmylist.addView(sub_relative_item_lay);
                        sub_item_name.setPaintFlags(sub_item_name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        Log.d("msgtag","ans1 is:"+sub_relative_item_lay.getParent().toString()+" "+sub_item_name.getText().toString());
                    }

                }
            });

            sub_item_delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(dbh.get_ingredient_type(sub_item_name.getText().toString(), firebaseUser.getUid()).equals("tobuy")) {
                        holder.whatsinmylist.removeView(sub_relative_item_lay);
                    }
                    else if(dbh.get_ingredient_type(sub_item_name.getText().toString(), firebaseUser.getUid()).equals("purchased")){
                        holder.whatigot.removeView(sub_relative_item_lay);
                    }

                    dbh.deleteingredient_cart(new addtocart(String.valueOf(sub_item_name.getText()),firebaseUser.getUid()));
                    dtf.setcount(dbh.get_count_ingredients_cart(firebaseUser.getUid()));
                    Log.d("msgtag","count from database is:"+dbh.get_count_ingredients_cart(firebaseUser.getUid()));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return listitems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout whatsinmylist,whatigot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            whatsinmylist=itemView.findViewById(R.id.linear_main);
            whatigot=itemView.findViewById(R.id.linear_whatigot);

        }
    }
}
