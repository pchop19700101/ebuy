package cn.porkchop.ebuy.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable {
    private List<SearchItem> searchItemList;
    private int totalPage;
    private long recordCount;

    public List<SearchItem> getSearchItemList() {
        return searchItemList;
    }

    public void setSearchItemList(List<SearchItem> searchItemList) {
        this.searchItemList = searchItemList;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }
}
