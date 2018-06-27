package com.example.ben.siirler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mine BAYRAKDAR on 30.12.2017.
 */

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NANE = "Siirler";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLO_SIIRLER = "siirler";
    private static final String ROW_ID = "id";
    private static final String ROW_AD = "ad";
    private static final String ROW_SAIR = "sair";
    private static final String ROW_ICERIK = "icerik";

    private static final String TABLO_FAVORILER = "favoriler";
    private static final String FAVORI_ID = "favid";
    private static final String FAVORI_AD = "favad";
    private static final String FAVORI_SAIR = "favsair";

    public Database(Context context) {
        super(context, DATABASE_NANE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLO_SIIRLER + "("
                + ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ROW_AD + " TEXT, "
                + ROW_SAIR + " TEXT, "
                + ROW_ICERIK + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLO_FAVORILER + "("
                + FAVORI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FAVORI_AD + " TEXT, "
                + FAVORI_SAIR + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLO_SIIRLER);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLO_FAVORILER);
        onCreate(db);
    }


    public void SiirEkle(String ad, String sair, String icerik){
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(ROW_AD, ad);
            cv.put(ROW_SAIR, sair);
            cv.put(ROW_ICERIK, icerik);
            db.insert(TABLO_SIIRLER, null, cv);
        }
        catch (Exception e){

        }
        db.close();
    }

    public List<String> SiirListele(){
        List<String> siirler = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String[] sutunlar = {ROW_ID, ROW_AD, ROW_SAIR, ROW_ICERIK};
            Cursor cursor = db.query(TABLO_SIIRLER, sutunlar, null, null, null, null, null);
            while (cursor.moveToNext()){
                siirler.add(cursor.getInt(0) + " - "
                        + cursor.getString(1) + " - "
                        + cursor.getString(2) + " - "
                        + cursor.getString(3));
            }
        }
        catch (Exception e){

        }
        db.close();
        return siirler;
    }

    public void SiirSil(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String where = ROW_ID + " = " + id;
            db.delete(TABLO_SIIRLER, where, null);
        }
        catch (Exception e){

        }
        db.close();
    }

    public void SiirDuzenle(int id, String ad, String sair, String icerik){
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            ContentValues cv = new ContentValues();
            cv.put(ROW_AD, ad);
            cv.put(ROW_SAIR, sair);
            cv.put(ROW_ICERIK, icerik);
            String where = ROW_ID + " = '" + id + "'";
            db.update(TABLO_SIIRLER,cv, where, null);
        }
        catch (Exception e){

        }
        db.close();
    }



    public void FavoriEkle(String favad, String favsair){
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(FAVORI_AD, favad);
            cv.put(FAVORI_SAIR, favsair);
            db.insert(TABLO_FAVORILER, null, cv);
        }
        catch (Exception e){

        }
        db.close();
    }

    public void FavoriSil(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String where = FAVORI_ID + " = " + id;
            db.delete(TABLO_FAVORILER, where, null);
        }
        catch (Exception e){

        }
        db.close();
    }

    public List<String> FavoriListele(){
        List<String> favoriler = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String[] sutunlar = {FAVORI_ID, FAVORI_AD, FAVORI_SAIR};
            Cursor cursor = db.query(TABLO_FAVORILER, sutunlar, null, null, null, null, null);
            while (cursor.moveToNext()){
                favoriler.add(cursor.getInt(0) + " - "
                        + cursor.getString(1) + " ( "
                        + cursor.getString(2) + " ) ");
            }
        }
        catch (Exception e){

        }
        db.close();
        return favoriler;
    }
}