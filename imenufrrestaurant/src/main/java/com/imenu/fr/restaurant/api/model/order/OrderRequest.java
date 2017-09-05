package com.imenu.fr.restaurant.api.model.order;

/**
 * Created by android on 1/9/17.
 */

public class OrderRequest {
    private int store_id;
    private int status;
    private int limit;
    private int offset;

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
