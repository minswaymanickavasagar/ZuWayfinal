package com.ZuWay.model;

import java.util.List;

public class SubTitleModel {

    private String home_title, home_title_id, home_title_scid;
    List<SubTitleGridModel> homegrid_title;

    public SubTitleModel() {
    }

    public String getHome_title_id() {
        return home_title_id;
    }

    public void setHome_title_id(String home_title_id) {
        this.home_title_id = home_title_id;
    }

    public String getHome_title_scid() {
        return home_title_scid;
    }

    public void setHome_title_scid(String home_title_scid) {
        this.home_title_scid = home_title_scid;
    }

    public String getHome_title() {
        return home_title;
    }

    public void setHome_title(String home_title) {
        this.home_title = home_title;
    }

    public List<SubTitleGridModel> getHomegrid_title() {
        return homegrid_title;
    }

    public void setHomegrid_title(List<SubTitleGridModel> homegrid_title) {
        this.homegrid_title = homegrid_title;
    }
}
