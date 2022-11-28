package com.example.smartshopper.models;

import java.io.Serializable;

public class Tag implements Serializable {
    String tagText;
    Boolean tagChecked;
    String tagIcon;

    public Tag(String tagText, Boolean tagChecked, String tagIcon) {
        this.tagText = tagText;
        this.tagChecked = tagChecked;
        this.tagIcon = tagIcon;
    }

    public String getTagText() { return tagText; }

    public Boolean tagIsChecked() { return tagChecked; }

    public void setTagIsChecked(Boolean bool) {
        this.tagChecked = bool;
    }

    public String getTagIcon() { return tagIcon; }
}
