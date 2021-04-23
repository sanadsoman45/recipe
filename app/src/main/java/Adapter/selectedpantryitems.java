package Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe.Datatransferinterface;
import com.example.recipe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.collection.LLRBNode;

import java.util.ArrayList;
import java.util.List;

import Model.Ingredients;
import Model.ListItem;
import Model.addtocart;
import Model.selectedpantrymodel;
import Utils.Util;
import de.hdodenhof.circleimageview.CircleImageView;


public class selectedpantryitems extends RecyclerView.Adapter<selectedpantryitems.ViewHolder>{

    private final Context con;
    private ArrayList<selectedpantrymodel> listitems;
    private DatabaseHandler dbh;
    Datatransferinterface dtf;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public selectedpantryitems(Context con, ArrayList<selectedpantrymodel> listitems, Datatransferinterface dtf) {
        this.con = con;
        this.listitems = listitems;
        this.dbh=new DatabaseHandler(con);
        this.dtf=dtf;
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
        dtf.setcount(dbh.get_count_ingredients(firebaseUser.getUid()));
    }

    @NonNull
    @Override
    public selectedpantryitems.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.pantry_items,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull selectedpantryitems.ViewHolder holder, int position) {

        selectedpantrymodel lst=listitems.get(position);
        holder.round_img.setImageDrawable(lst.getIcon());
        holder.tv.setText(lst.getTitle());
            for(int i=0;i<lst.getbtn_size();i++){
                Log.d("msgtag","Array is:"+lst.getarr());
                Log.d("msgtag","list size is:"+lst.getbtn_size()+""+i+"element is:"+lst.getBtn(i));
                RelativeLayout sub_relative_item_lay= new RelativeLayout(con);
                RelativeLayout.LayoutParams sub_item_params= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                sub_item_params.setMargins(15, 10, 15, 0);
                holder.linearlay.addView(sub_relative_item_lay,sub_item_params);

                TextView sub_item_name= new TextView(con);
                sub_item_name.setText(lst.getBtn(i));
                Log.d("msgtag","string is:"+sub_item_name.getText());
                RelativeLayout.LayoutParams sub_item_name_params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                sub_item_name.setId(R.id.item_name);

                Button sub_item_shopping_btn= new Button(con);
                RelativeLayout.LayoutParams sub_item_shopping_btn_params= new RelativeLayout.LayoutParams(70, 70);
                sub_item_shopping_btn.setId(R.id.shopping_btn);

                Button sub_item_delete_btn= new Button(con);
                RelativeLayout.LayoutParams sub_item_delete_btn_params= new RelativeLayout.LayoutParams(70, 70);
                sub_item_delete_btn.setId(R.id.delete_btn);

                View sub_item_line= new View(con);
                RelativeLayout.LayoutParams sub_item_line_params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                sub_item_line.setId(R.id.item_view_line);

                sub_item_name_params.addRule(RelativeLayout.START_OF, sub_item_shopping_btn.getId());
                sub_item_name_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                sub_item_name_params.setMargins(10, 10, 10, 10);
                sub_item_name.setLayoutParams(sub_item_name_params);

                sub_item_shopping_btn_params.addRule(RelativeLayout.START_OF, sub_item_delete_btn.getId());
                sub_item_shopping_btn.setBackground(ContextCompat.getDrawable(con, R.drawable.ic_addto_cart));
                sub_item_shopping_btn.setLayoutParams(sub_item_shopping_btn_params);

                sub_item_delete_btn_params.setMargins(10, 0, 10, 0);
                sub_item_delete_btn_params.addRule(RelativeLayout.ALIGN_PARENT_END);
                sub_item_delete_btn.setBackground(ContextCompat.getDrawable(con, R.drawable.ic_baseline_delete_24));
                sub_item_delete_btn.setLayoutParams(sub_item_delete_btn_params);

                sub_item_line_params.addRule(RelativeLayout.BELOW, sub_item_name.getId());
                sub_item_line_params.setMargins(0, 5, 0, 0);
                sub_item_line.setBackgroundColor(Color.parseColor("#979797"));
                sub_item_line.setLayoutParams(sub_item_line_params);

                if(dbh.getIngredient(lst.getBtn(i),firebaseUser.getUid())){
                    sub_relative_item_lay.addView(sub_item_name);
                    sub_relative_item_lay.addView(sub_item_shopping_btn);
                    sub_relative_item_lay.addView(sub_item_delete_btn);
                    sub_relative_item_lay.addView(sub_item_line);
                }

                if(dbh.getIngredient_cart(lst.getBtn(i),firebaseUser.getUid())){
                    sub_item_shopping_btn.setBackground(ContextCompat.getDrawable(con, R.drawable.ic_addedtocart));
                    sub_item_shopping_btn.setLayoutParams(sub_item_shopping_btn_params);
                }

                sub_item_delete_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ing_name=String.valueOf(sub_item_name.getText());
                        String ing_section=String.valueOf(holder.tv.getText());

                        if(dbh.getIngredient(ing_name,firebaseUser.getUid())){
                            Log.d("deleteddata","parent from first if before delete is:"+sub_relative_item_lay.getParent());
                            dbh.deleteingredient(new Ingredients(String.valueOf(sub_item_name.getText()),firebaseUser.getUid()));
                            holder.linearlay.removeView(sub_relative_item_lay);
                            Log.d("deletedata","parent from first if after delete for "+ing_name+"is:"+sub_relative_item_lay.getParent());
                        }



                        if(dbh.get_ing_name(ing_section,firebaseUser.getUid())<=0){
                            Log.d("delete","count is:"+dbh.get_ing_name(ing_section,firebaseUser.getUid()));
                            Log.d("deleteddata","parent from second if before delete for:"+ ing_name+" is:"+sub_relative_item_lay.getParent());
                            holder.linear_main_lay.removeAllViews();
                            Log.d("deletedata","parent from second if after delete for:"+ing_name+"is:"+sub_relative_item_lay.getParent());
                        }


                        dtf.setcount(dbh.get_count_ingredients(firebaseUser.getUid()));
                    }
                });

                sub_item_shopping_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!dbh.getIngredient_cart(String.valueOf(sub_item_name.getText()),firebaseUser.getUid())){
                            Log.d("msgtag","hello");
                            sub_item_shopping_btn.setBackground(ContextCompat.getDrawable(con, R.drawable.ic_addedtocart));
                            dbh.add_Ingredients_cart(new addtocart(sub_item_name.getText().toString(),"tobuy",firebaseUser.getUid()));
                            sub_item_shopping_btn.setLayoutParams(sub_item_shopping_btn_params);
                        }
                        else{
                            Log.d("msgtag","hello from else");
                            dbh.deleteingredient_cart(new addtocart(sub_item_name.getText().toString(),firebaseUser.getUid()));
                            sub_item_shopping_btn.setBackground(ContextCompat.getDrawable(con, R.drawable.ic_addto_cart));
                            sub_item_shopping_btn.setLayoutParams(sub_item_shopping_btn_params);
                        }
                    }
                });


            }

    }

    @Override
    public int getItemCount() {
        return listitems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView round_img;
        private final TextView tv;
        private LinearLayout linearlay,linear_main_lay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            round_img=itemView.findViewById(R.id.roundedimageview);
            tv=itemView.findViewById(R.id.titletv);
            linear_main_lay=itemView.findViewById(R.id.linear_main);
            linearlay= itemView.findViewById(R.id.linear_layout);
        }
    }

}
