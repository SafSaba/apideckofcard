package pojos;

import lombok.Data;

import java.util.List;

@Data
public class Card {

    private String   code;
    private String image;
    private List<String> images;
    private String value;
    private  String suit;

    public String getCode() {
        return code;
    }

    public String getImage() {
        return image;
    }

    public List<String> getImages() {
        return images;
    }

    public String getValue() {
        return value;
    }

    public String getSuit() {
        return suit;
    }
    public Card(String code, String image, List<String> images, String value, String suit) {
        this.code = code;
        this.image = image;
        this.images = images;
        this.value = value;
        this.suit = suit;
    }
}
