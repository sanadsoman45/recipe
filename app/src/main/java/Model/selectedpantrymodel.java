package Model;

import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;

public class selectedpantrymodel {
    private String title;
    private String[] btn;
    private Drawable icon;
    public selectedpantrymodel(String title, String[] btn, Drawable icon) {
        this.title=title;
        this.btn=btn;
        this.icon = icon;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBtn(int i) {
        return btn[i];
    }

    public void setBtn(String[] btn) {
        this.btn = btn;
    }

    public int getbtn_size(){
        return btn.length;
    }
}
