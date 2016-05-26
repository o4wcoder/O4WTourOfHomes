package com.fourthwardmobile.o4wtourofhomes.models;

/**
 * Created by Chris Hare on 5/25/2016.
 */
public class Sponsor {

    String name;
    String description;
    String backgroundImageUrl;
    String logoImageUrl;
    String website;

    public Sponsor() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public String getLogoImageUrl() {
        return logoImageUrl;
    }

    public void setLogoImageUrl(String logoImageUril) {
        this.logoImageUrl = logoImageUril;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
