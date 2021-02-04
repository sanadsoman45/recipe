package Model;
public class ListItem {
    private String title;
    private String[] btn;
    public ListItem(String title,String[] btn) {
        this.title=title;
        this.btn=btn;
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