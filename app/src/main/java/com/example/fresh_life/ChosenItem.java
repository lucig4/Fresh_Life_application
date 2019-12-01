package com.example.fresh_life;

import android.os.Parcel;
import android.os.Parcelable;


public class ChosenItem implements Parcelable {


    public String category;
    public String item;
    public String price;
    public String comments;

    public ChosenItem(Item item, String comments){

        addItem(item);
        addComments(comments);
        addCategory(item);
        addPrice(item);
    }


    protected ChosenItem(Parcel in) {
        category = in.readString();
        item = in.readString();
        price = in.readString();
        comments = in.readString();
    }

    public static final Creator<ChosenItem> CREATOR = new Creator<ChosenItem>() {
        @Override
        public ChosenItem createFromParcel(Parcel in) {
            return new ChosenItem(in);
        }

        @Override
        public ChosenItem[] newArray(int size) {
            return new ChosenItem[size];
        }
    };

    public void addItem(Item item){
        this.item  = item.getItem();
    }
    public void addComments(String comments){
        this.comments = comments;
    }
    public void addCategory(Item item){
        this.category = item.getCategory();
    }
    public void addPrice(Item item){
        this.price = item.getPrice();
    }

    public String getItem() { return item; }
    public String getComments() {
        return comments;
    }
    public String getCategory() { return category;}
    public String getPrice() { return price;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(category);
        parcel.writeString(item);
        parcel.writeString(price);
        parcel.writeString(comments);

    }
}
