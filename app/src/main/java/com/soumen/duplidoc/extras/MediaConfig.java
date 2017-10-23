package com.soumen.duplidoc.extras;

/**
 * Created by Soumen on 10/22/2017.
 */

public class MediaConfig {

    private String[] columns;
    private String orderBy;

    public MediaConfig(String[] columns, String orderBy) {
        this.columns = columns;
        this.orderBy = orderBy;
    }

    public MediaConfig getMediaConfig() {
        return this;
    }

    public String[] getColumns() {
        return columns;
    }

    public String getOrderBy() {
        return orderBy;
    }
}