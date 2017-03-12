package com.whatsapp.integration.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class MessageInfo implements Parcelable {
    private final String message;
    private final String date;

    public MessageInfo(String message, String date) {
        this.message = message;
        this.date = date;
    }

    private MessageInfo(Parcel in) {
        message = in.readString();
        date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeString(date);
    }

    public static final Parcelable.Creator<MessageInfo> CREATOR = new Parcelable.Creator<MessageInfo>() {
        public MessageInfo createFromParcel(Parcel in) {
            return new MessageInfo(in);
        }

        public MessageInfo[] newArray(int size) {
            return new MessageInfo[size];
        }
    };
}
