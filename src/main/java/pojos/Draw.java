package pojos;

import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class Draw {

    private boolean success;
    private String deck_id;
    private List<Map<?,?>> cards;

    public List<Map<?, ?>> getCards() {
        return cards;
    }

    int remaining;
    String error;

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public int getRemaining() {
        return remaining;
    }
}
