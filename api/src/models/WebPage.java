package src.models;

public class WebPage {

    private String title;
    private String link;

    public WebPage(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return this.title;
    }

    public String getLink() {
        return this.link;
    }

    public String json() {
        return "{\"title\": \"" + this.title + "\", \"link\": \"" + this.link + "\"}";
    }

}
