package Model;

public class searchmodel {

    private String title;


    private String section_name;

    public searchmodel(String title, String section_name) {
        this.title=title;
        this.section_name=section_name;
    }



    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getSection_name() {
        return section_name;
    }
}
