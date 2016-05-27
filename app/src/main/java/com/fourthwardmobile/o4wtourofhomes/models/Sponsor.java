package com.fourthwardmobile.o4wtourofhomes.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Chris Hare on 5/25/2016.
 */
public class Sponsor implements Parcelable {

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

    protected Sponsor(Parcel in) {
        name = in.readString();
        description = in.readString();
        backgroundImageUrl = in.readString();
        logoImageUrl = in.readString();
        website = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(backgroundImageUrl);
        dest.writeString(logoImageUrl);
        dest.writeString(website);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Sponsor> CREATOR = new Parcelable.Creator<Sponsor>() {
        @Override
        public Sponsor createFromParcel(Parcel in) {
            return new Sponsor(in);
        }

        @Override
        public Sponsor[] newArray(int size) {
            return new Sponsor[size];
        }
    };
}
