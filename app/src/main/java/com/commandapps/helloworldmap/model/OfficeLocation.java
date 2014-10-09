package com.commandapps.helloworldmap.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Michael on 10/8/2014.
 */
public class OfficeLocation implements Parcelable {

    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip_postal_code() {
        return zip_postal_code;
    }

    public String getPhone() {
        return phone;
    }

    public String getFax() {
        return fax;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getOffice_image() {
        return office_image;
    }

    private String address2;
    private String city;
    private String state;
    private String zip_postal_code;
    private String phone;
    private String fax;
    private String latitude;
    private String longitude;
    private String office_image;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.address2);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.zip_postal_code);
        dest.writeString(this.phone);
        dest.writeString(this.fax);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.office_image);
    }

    public OfficeLocation() {
    }

    private OfficeLocation(Parcel in) {
        this.name = in.readString();
        this.address = in.readString();
        this.address2 = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.zip_postal_code = in.readString();
        this.phone = in.readString();
        this.fax = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.office_image = in.readString();
    }

    public static final Parcelable.Creator<OfficeLocation> CREATOR = new Parcelable.Creator<OfficeLocation>() {
        public OfficeLocation createFromParcel(Parcel source) {
            return new OfficeLocation(source);
        }

        public OfficeLocation[] newArray(int size) {
            return new OfficeLocation[size];
        }
    };
}
