package endPoints;

public enum ApiEndPoints {

    /**
     *  URLS' End Points
     *
     */
    NEW_DECK("/new"),
    SHUFFLE("/shuffle"),
    DRAW_CARD("/draw");


    private final String URL;
    ApiEndPoints(String url) {
        this.URL = url;
    }
    public String getEndPoint() {
        return this.URL;
    }


}
