package Adapter;
import android.annotation.SuppressLint;
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

import com.example.recipe.R;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import Model.ListItem;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private final Context con;
    private final List<ListItem> listitems;
    private int i=10;
    public MyAdapter(Context con, List<ListItem> listitems) {
        this.con = con;
        this.listitems = listitems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_ingredientsactivity,parent,false);
        return new ViewHolder(v);

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItem lst=listitems.get(position);
        holder.titletv.setText(lst.getTitle());
        FlexboxLayout layout = holder.flb;

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
                                create_Togglebtn(layout,lst,j);
                                layout.removeView(btnTag);
                            }
                        }
                    });
                }
                else{
                    create_Togglebtn(layout,lst,j);
                }


            }
        }
        else{
            for (int j = 0; j < lst.getbtn_size(); j++) {
                create_Togglebtn(layout,lst,j);
            }
        }


    }

    public void create_Togglebtn(FlexboxLayout layout,ListItem lst,int j){
        ToggleButton btnTag = new ToggleButton(con);
        btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        btnTag.setText(lst.getBtn(j));
        btnTag.setTextOn(lst.getBtn(j));
        btnTag.setTextOff(lst.getBtn(j));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        btnTag.setLayoutParams(lp);
        btnTag.setBackgroundDrawable(con.getDrawable(R.drawable.ic_toggle));
        btnTag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    Toast.makeText(con, btnTag.getText(), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(con,"Else Block", Toast.LENGTH_SHORT).show();
                }
            }
        });
        layout.addView(btnTag);
    }

    @Override
    public int getItemCount() {
        return listitems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titletv;
        private final FlexboxLayout flb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flb=itemView.findViewById(R.id.flexlay);
            titletv=itemView.findViewById(R.id.titletv);
        }
    }
}