package pojos;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.Getter;


@Data
public class Deck {
    private boolean success;
    private String deck_id;
    private  int remaining;
    private  boolean shuffled;

}
