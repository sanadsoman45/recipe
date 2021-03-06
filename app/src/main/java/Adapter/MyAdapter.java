package Adapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe.Datatransferinterface;
import com.example.recipe.R;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import Model.Ingredients;
import Model.ListItem;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private final Context con;
    private List<ListItem> listitems;
    private DatabaseHandler dbh;
    Datatransferinterface dtf;

    public MyAdapter(Context con, List<ListItem> listitems,Datatransferinterface dtf) {
        this.con = con;
        this.listitems = listitems;
        this.dtf=dtf;
        dbh=new DatabaseHandler(con);
        dtf.setcount(dbh.get_count_ingredients());
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        dbh=new DatabaseHandler(con);
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_ingredientsactivity,parent,false);
        return new MyAdapter.ViewHolder(v);

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItem lst=listitems.get(position);
        holder.titletv.setText(lst.getTitle());
        FlexboxLayout layout = holder.flb;
        TextView counttext=holder.counttextview;


        if(lst.getbtn_size()>10) {
            for(int j=0;j<=10;j++){
                if(j==10){
                    Button btnTag=new Button(con);
                    btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    btnTag.setText("+"+(lst.getbtn_size()-10)+"more");
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(10, 10, 10, 10);
                    btnTag.setLayoutParams(lp);
                    btnTag.setBackground(con.getDrawable(R.drawable.ic_normalbutton));
                    layout.addView(btnTag);
                    btnTag.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for(int j=10;j<lst.getbtn_size();j++){
                                Log.d("msginside","Value of j is:"+j);
                                create_Togglebtn(layout,lst,j,counttext);
                                layout.removeView(btnTag);
                            }
                        }
                    });
                }
                else{
                    create_Togglebtn(layout,lst,j,counttext);
                }


            }
        }
        else{
            for (int j = 0; j < lst.getbtn_size(); j++) {
                create_Togglebtn(layout,lst,j,counttext);
            }
        }


    }

    public void create_Togglebtn(FlexboxLayout layout,ListItem lst,int j,TextView count_text){
        ToggleButton btnTag = new ToggleButton(con);
        btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        btnTag.setText(lst.getBtn(j));
        btnTag.setTextOn(lst.getBtn(j));
        btnTag.setTextOff(lst.getBtn(j));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        btnTag.setLayoutParams(lp);
        btnTag.setBackgroundDrawable(con.getDrawable(R.drawable.ic_toggle));
        count_text.setText(dbh.get_ing_section_count(lst.getTitle())+"/"+lst.getbtn_size()+" ingredients");
        if(dbh.getIngredient(String.valueOf(btnTag.getText())) ){
            Log.d("ingmsg","Value is:"+dbh.getIngredient(String.valueOf(btnTag.getText())));
            count_text.setText(dbh.get_ing_section_count(lst.getTitle())+"/"+lst.getbtn_size()+" ingredients");
            btnTag.setChecked(true);
        }

        btnTag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    dbh.addingredients(new Ingredients(String.valueOf(btnTag.getText()),lst.getTitle()));
//                    Toast.makeText(con, btnTag.getText()+" "+dbh.get_count_ingredients(), Toast.LENGTH_SHORT).show();
                }
                else{
                    dbh.deleteingredient(new Ingredients(String.valueOf(btnTag.getText())));
//                    Toast.makeText(con,"Else Block", Toast.LENGTH_SHORT).show();
                }
                count_text.setText(dbh.get_ing_section_count(lst.getTitle())+"/"+lst.getbtn_size()+" ingredients");
                dtf.setcount(dbh.get_count_ingredients());
            }
        });

        layout.addView(btnTag);
    }



    @Override
    public int getItemCount() {
        return listitems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titletv,counttextview;
        private final FlexboxLayout flb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            counttextview=itemView.findViewById(R.id.counttextview);
            flb=itemView.findViewById(R.id.flexlay);
            titletv=itemView.findViewById(R.id.titletv);
        }
    }
}