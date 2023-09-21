package com.ZuWay.model;

import java.util.List;

public class HomeTitleModel {

private  String home_title_id;
    private String home_title;
    List<HomeTitleGridModel> homegrid_title;

    public HomeTitleModel() {
    }


    public String getHome_title_id() {
        return home_title_id;
    }

    public void setHome_title_id(String home_title_id) {
        this.home_title_id = home_title_id;
    }

    public String getHome_title() {
        return home_title;
    }

    public void setHome_title(String home_title) {
        this.home_title = home_title;
    }

    public List<HomeTitleGridModel> getHomegrid_title() {
        return homegrid_title;
    }

    public void setHomegrid_title(List<HomeTitleGridModel> homegrid_title) {
        this.homegrid_title = homegrid_title;
    }
}
