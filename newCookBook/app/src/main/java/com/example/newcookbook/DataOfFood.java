package com.example.newcookbook;

public class DataOfFood {
    private String Name; //שם
    private String Preparation; //אופן ההכנה
    private String Grocery; // מצרכים
    private String Pic;// תמונה
    private String Url ;//האם יש תמונה
    private String Link;

   /*public DataOfFood(String name, String preparation, String grocery, String pic,String url) {
        Name = name;
        Preparation = preparation;
        Grocery = grocery;
        Pic = pic;
        Url =url;
    }*/

    public DataOfFood(String name, String preparation, String grocery,String url) {
        Name = name;
        Preparation = preparation;
        Grocery = grocery;
        Url=url;
    }

    public DataOfFood(String name, String link, String url) {
        Name = name;
        Link=link;
        Url=url;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPreparation() {
        return Preparation;
    }

    public void setPreparation(String preparation) {
        Preparation = preparation;
    }

    public String getGrocery() {
        return Grocery;
    }

    public void setGrocery(String grocery) {
        Grocery = grocery;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}

