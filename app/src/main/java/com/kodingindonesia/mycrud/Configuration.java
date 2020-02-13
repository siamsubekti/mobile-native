package com.kodingindonesia.mycrud;

/**
 * Created by muhammadyusuf on 01/19/2017.
 * kodingindonesia
 */

public class Configuration {

    //Dibawah ini merupakan Pengalamatan dimana Lokasi Skrip CRUD PHP disimpan
    //Pada tutorial Kali ini, karena kita membuat localhost maka alamatnya tertuju ke IP komputer
    //dimana File PHP tersebut berada
    //PENTING! JANGAN LUPA GANTI IP SESUAI DENGAN IP KOMPUTER DIMANA DATA PHP BERADA
    public static final String URL_ADD="https://simple-contact-crud.herokuapp.com/contact";
    public static final String URL_GET_ALL = "https://simple-contact-crud.herokuapp.com/contact";
    public static final String URL_GET_EMP = "https://simple-contact-crud.herokuapp.com/contact/";
    public static final String URL_UPDATE_EMP = "https://simple-contact-crud.herokuapp.com/contact/";
    public static final String URL_DELETE_EMP = "https://simple-contact-crud.herokuapp.com/contact/";

    //Dibawah ini merupakan Kunci yang akan digunakan untuk mengirim permintaan ke Skrip PHP
    public static final String KEY_EMP_FIRSTNAME = "firstName";
    public static final String KEY_EMP_LASTNAME = "lastName";
    public static final String KEY_EMP_AGE = "age"; //desg itu variabel untuk posisi
    public static final String KEY_EMP_PHOTO = "photo"; //salary itu variabel untuk gajih

    //JSON Tags
    public static final String TAG_JSON_ARRAY="data";
    public static final String TAG_ID = "id";
    public static final String TAG_FIRSTNAME = "firstName";
    public static final String TAG_LASTNAME = "lastName";
    public static final String TAG_AGE = "age";
    public static final String TAG_PHOTO = "photo";

    //ID karyawan
    //emp itu singkatan dari AllEmployeeImpl
    public static final String EMP_ID = "id";
}
