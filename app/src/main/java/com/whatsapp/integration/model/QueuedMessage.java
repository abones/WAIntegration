package com.whatsapp.integration.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class QueuedMessage implements Parcelable {
    private final String message;
    private final String recipient;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeString(recipient);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QueuedMessage> CREATOR = new Creator<QueuedMessage>() {
        @Override
        public QueuedMessage createFromParcel(Parcel in) {
            return new QueuedMessage(in);
        }

        @Override
        public QueuedMessage[] newArray(int size) {
            return new QueuedMessage[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public QueuedMessage(String message, String recipient) {
        this.message = message;
        this.recipient = recipient;
    }

    public QueuedMessage(Parcel in) {
        this.message = in.readString();
        this.recipient = in.readString();
    }

    public String getRecipient() {
        return recipient;
    }
}
