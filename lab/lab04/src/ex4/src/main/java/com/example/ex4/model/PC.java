package com.example.ex4.model;

import com.example.ex4.R;

public class PC {
    private String id;
    private Integer status; // drawable id

    public PC(String id) {
        this.id = id;
        this.status = R.drawable.off;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
