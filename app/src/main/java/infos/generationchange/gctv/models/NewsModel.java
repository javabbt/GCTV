package infos.generationchange.gctv.models;

public class NewsModel extends MainModel{

    private String thumbNail;
    private String   Description;
    private String youtubeLink;

    public NewsModel(String thumbNail, String description , String youtubeLink) {
        this.thumbNail = thumbNail;
        Description = description;
        this.youtubeLink = youtubeLink;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }
}
