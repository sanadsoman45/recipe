package Model;

import android.graphics.drawable.Drawable;

public class addtocartlist {

    private String[] textview;

    public addtocartlist(String[] textview) {
        this.textview=textview;
    }


    public String getBtn(int i) {
        return textview[i];
    }

    public void setBtn(String[] btn) {
        this.textview = btn;
    }

    public int getbtn_size(){
        return textview.length;
    }
}
