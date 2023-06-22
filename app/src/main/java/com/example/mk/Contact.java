package com.example.mk;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Contact implements Parcelable{
    private String _id;
    private String name;
    private String phone_number;
    private String gender;
    private String address;
    private String description;

    public Contact(String name, String phone_number, String gender, String address, String description) {
        this.name = name;
        this.phone_number = phone_number;
        this.gender = gender;
        this.address = address;
        this.description = description;
    }

    protected Contact(Parcel in) {
        _id = in.readString();
        name = in.readString();
        phone_number = in.readString();
        gender = in.readString();
        address = in.readString();
        description = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String get_id(){
        return _id;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(name);
        dest.writeString(phone_number);
        dest.writeString(gender);
        dest.writeString(address);
        dest.writeString(description);
    }
}
