package com.pinyougou.pojo;

import java.io.Serializable;

/**
 * @author Ricky
 * @version 1.0
 * @description TODO
 * @date 2018/12/5
 */
public class SearchParameter implements Serializable {
    private String searchStr;

    public String getSearchStr() {
        return searchStr;
    }

    public void setSearchStr(String searchStr) {
        this.searchStr = searchStr;
    }
}
