package nl.jesper.songshare.dto.requests.get;

/**
 * GET request in JSON format with search queries/parameters to get a song listing.
 */
public class SearchSongsRequest {
    private int page = 0;
    private int size = 10;
    private String search = "";
    private String sort = "uploadTimeStamp";
    private String order = "desc";

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    //    private String songTitle;
//    private String artistName;
//
//    public String getSongTitle() {
//        return songTitle;
//    }
//
//    public void setSongTitle(String songTitle) {
//        this.songTitle = songTitle;
//    }
//
//    public String getArtistName() {
//        return artistName;
//    }
//
//    public void setArtistName(String artistName) {
//        this.artistName = artistName;
//    }
}
