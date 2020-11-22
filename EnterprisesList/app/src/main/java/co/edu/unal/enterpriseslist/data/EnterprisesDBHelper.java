package co.edu.unal.enterpriseslist.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import co.edu.unal.enterpriseslist.data.EnterprisesContract.EnterpriseEntry;

public class EnterprisesDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Enterprise.db"; //file where db is stored

    public EnterprisesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Comandos SQL
        sqLiteDatabase.execSQL("CREATE TABLE " + EnterpriseEntry.TABLE_NAME + " ("
                + EnterpriseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EnterpriseEntry.ID + " TEXT NOT NULL,"
                + EnterpriseEntry.NAME + " TEXT NOT NULL,"
                + EnterpriseEntry.WEBURL + " TEXT NOT NULL,"
                + EnterpriseEntry.MAILCONTACT + " TEXT NOT NULL,"
                + EnterpriseEntry.PHONECONTACT + " TEXT NOT NULL,"
                + EnterpriseEntry.PRODUCTS + " TEXT NOT NULL,"
                + EnterpriseEntry.CONSULTING  + " INTEGER,"
                + EnterpriseEntry.SOFTDEVELOP  + " INTEGER,"
                + EnterpriseEntry.SOFTFABRIC  + " INTEGER,"
                + EnterpriseEntry.AVATAR_URI + " TEXT,"
                + "UNIQUE (" + EnterpriseEntry.ID + "))");

        // Insertar datos ficticios para prueba inicial
        mockData(sqLiteDatabase);


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hay operaciones
    }


    //stores enterprises in db
    public long saveLawyer(Enterprise enterprise) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                EnterpriseEntry.TABLE_NAME,
                null,
                enterprise.toContentValues());

    }
    //false test data
    private void mockData(SQLiteDatabase sqLiteDatabase) {
        mockEnterprise(sqLiteDatabase, new Enterprise("BugSoft", "bugsoft.com",
                "300 200 1111", "bugs@falso.com","Debbuger",
                0,0,0, "bug.jpg"));
        mockEnterprise(sqLiteDatabase, new Enterprise("Rareware", "rare.com",
                "300 200 2222", "pqr@rare.com","games",
                0,1,1, "rare.jpg"));
    }

    public long mockEnterprise(SQLiteDatabase db, Enterprise enterprise) {
        return db.insert(
                EnterpriseEntry.TABLE_NAME,
                null,
                enterprise.toContentValues());
    }
//query
    public Cursor getAllEnterprises() {
        return getReadableDatabase()
                .query(
                        EnterpriseEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }

    public Cursor getEnterpriseById(String enterpriseId) {
        Cursor c = getReadableDatabase().query(
                EnterpriseEntry.TABLE_NAME,
                null,
                EnterpriseEntry.ID + " LIKE ?",
                new String[]{enterpriseId},
                null,
                null,
                null);
        return c;
    }
    public Cursor getEnterpriseByName(String enterpriseName) {
        Cursor c = getReadableDatabase().query(
                EnterpriseEntry.TABLE_NAME,
                null,
                EnterpriseEntry.NAME + " LIKE ?",
                new String[]{"%"+enterpriseName+"%"},
                null,
                null,
                null);
        return c;
    }
    public int updateEnterprise(Enterprise enterprise, String enterpriseId) {
        return getWritableDatabase().update(
                EnterpriseEntry.TABLE_NAME,
                enterprise.toContentValues(),
                EnterpriseEntry.ID + " LIKE ?",
                new String[]{enterpriseId}
        );
    }
    public int deleteEnterprise(String enterpriseId) {
        return getWritableDatabase().delete(
                EnterpriseEntry.TABLE_NAME,
                EnterpriseEntry.ID + " LIKE ?",
                new String[]{enterpriseId});
    }


}
