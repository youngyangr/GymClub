package cn.edu.bjtu.gymclub.model;

public class News {
    String title;
    int imageID;
    public News(String title,int image){
        this.title=title;
        this.imageID=image;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return imageID;
    }

    public void setImage(int image) {
        imageID = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
