package com.fourthwardmobile.o4wtourofhomes.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Chris Hare on 5/7/2016.
 */
public class Home implements Parcelable {

    private String address;
    private String owners;
    private String homeType;
    private String yearBuilt;
    private String section;
    private String description;
    private String imageUrl;
    private String latitude;
    private String longitude;

    public Home() {

    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwners() {
        return owners;
    }

    public void setOwners(String owners) {
        this.owners = owners;
    }

    public String getHomeType() {
        return homeType;
    }

    public void setHomeType(String homeType) {
        this.homeType = homeType;
    }

    public String getYearBuilt() {
        return yearBuilt;
    }

    public void setYearBuilt(String yearBuilt) {
        this.yearBuilt = yearBuilt;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LatLng getLocation() {
        return new LatLng(Double.valueOf(latitude),Double.valueOf(longitude));
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    protected Home(Parcel in) {
        address = in.readString();
        owners = in.readString();
        homeType = in.readString();
        yearBuilt = in.readString();
        section = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        latitude = in.readString();
        longitude = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(owners);
        dest.writeString(homeType);
        dest.writeString(yearBuilt);
        dest.writeString(section);
        dest.writeString(description);
        dest.writeString(imageUrl);
        dest.writeString(latitude);
        dest.writeString(longitude);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Home> CREATOR = new Parcelable.Creator<Home>() {
        @Override
        public Home createFromParcel(Parcel in) {
            return new Home(in);
        }

        @Override
        public Home[] newArray(int size) {
            return new Home[size];
        }
    };
}
