package com.borisalexj.dogphoto;

import android.content.ContentValues;

/**
 * Created by user on 8/12/2017.
 */

public class DogModel {

    private int _id;
    private String photo;
    private String date;
    private String address;
    private String poroda;
    private String lat;
    private String lng;
    private String size;
    private String mast;
    private String oshiynik;
    private String name;
    private String klipsa;
    private String prikmety;
    private String primitki;

    public int get_id() {
        return _id;
    }

    public String getPoroda() {
        return poroda;
    }

    public void setPoroda(String poroda) {
        this.poroda = poroda;
    }

    public DogModel(String photo, String date, String address, String poroda, String lat, String lng, String size, String mast, String oshiynik, String name, String klipsa, String prikmety, String primitki) {
        this.photo = photo;
        this.date = date;
        this.address = address;
        this.poroda = poroda;
        this.lat = lat;
        this.lng = lng;
        this.size = size;
        this.mast = mast;
        this.oshiynik = oshiynik;
        this.name = name;
        this.klipsa = klipsa;
        this.prikmety = prikmety;
        this.primitki = primitki;
    }

    public DogModel(int _id, String photo, String date, String address, String poroda, String lat, String lng, String size, String mast, String oshiynik, String name, String klipsa, String prikmety, String primitki) {
        this._id = _id;
        this.photo = photo;
        this.date = date;
        this.address = address;
        this.poroda = poroda;
        this.lat = lat;
        this.lng = lng;
        this.size = size;
        this.mast = mast;
        this.oshiynik = oshiynik;
        this.name = name;
        this.klipsa = klipsa;
        this.prikmety = prikmety;
        this.primitki = primitki;
    }

    public DogModel(int _id, String photo, String date, String address, String lat, String lng, String size, String mast, String oshiynik, String name, String klipsa, String prikmety, String primitki) {
        this._id = _id;
        this.photo = photo;
        this.date = date;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.size = size;
        this.mast = mast;
        this.oshiynik = oshiynik;
        this.name = name;
        this.klipsa = klipsa;
        this.prikmety = prikmety;
        this.primitki = primitki;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public DogModel() {
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getMast() {
        return mast;
    }

    public void setMast(String mast) {
        this.mast = mast;
    }

    public String getOshiynik() {
        return oshiynik;
    }

    public void setOshiynik(String oshiynik) {
        this.oshiynik = oshiynik;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKlipsa() {
        return klipsa;
    }

    public void setKlipsa(String klipsa) {
        this.klipsa = klipsa;
    }

    public String getPrikmety() {
        return prikmety;
    }

    public void setPrikmety(String prikmety) {
        this.prikmety = prikmety;
    }

    public String getPrimitki() {
        return primitki;
    }

    public void setPrimitki(String primitki) {
        this.primitki = primitki;
    }

    public ContentValues toDb() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DogDatabase.DatabaseContract.DataColumns.PHOTO, photo);
        contentValues.put(DogDatabase.DatabaseContract.DataColumns.DATE, date);
        contentValues.put(DogDatabase.DatabaseContract.DataColumns.ADRRESS, address);
        contentValues.put(DogDatabase.DatabaseContract.DataColumns.PORODA, poroda);
        contentValues.put(DogDatabase.DatabaseContract.DataColumns.LAT, lat);
        contentValues.put(DogDatabase.DatabaseContract.DataColumns.LNG, lng);
        contentValues.put(DogDatabase.DatabaseContract.DataColumns.SIZE, size);
        contentValues.put(DogDatabase.DatabaseContract.DataColumns.MAST, mast);
        contentValues.put(DogDatabase.DatabaseContract.DataColumns.OSHIYNIK, oshiynik);
        contentValues.put(DogDatabase.DatabaseContract.DataColumns.NAME, name);
        contentValues.put(DogDatabase.DatabaseContract.DataColumns.KLIPSA, size);
        contentValues.put(DogDatabase.DatabaseContract.DataColumns.PRIKMETY, klipsa);
        contentValues.put(DogDatabase.DatabaseContract.DataColumns.PRIMITKI, prikmety);
        return contentValues;
    }

    public DogModel(String photo, String date, String address, String lat, String lng, String size, String mast, String oshiynik, String name, String klipsa, String prikmety, String primitki) {
        this.photo = photo;
        this.date = date;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.size = size;
        this.mast = mast;
        this.oshiynik = oshiynik;
        this.name = name;
        this.klipsa = klipsa;
        this.prikmety = prikmety;
        this.primitki = primitki;
    }

    @Override
    public String toString() {
        return "DogModel{" +
                "_id=" + _id +
                ", photo='" + photo + '\'' +
                ", date='" + date + '\'' +
                ", address='" + address + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", size='" + size + '\'' +
                ", mast='" + mast + '\'' +
                ", oshiynik='" + oshiynik + '\'' +
                ", name='" + name + '\'' +
                ", klipsa='" + klipsa + '\'' +
                ", prikmety='" + prikmety + '\'' +
                ", primitki='" + primitki + '\'' +
                '}';
    }
}
