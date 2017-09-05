package fr.h3lp.jcamhi.ft_hangouts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.res.ResourcesCompat;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcamhi on 9/5/17.
 */

public class ContactDAO {
    private SQLiteDatabase _database;
    private MySQLiteHelper _dbHelper;
    private Context _context;
    private String[] allColumns = {
            MySQLiteHelper.COL_ID,
            MySQLiteHelper.COL_NOM,
            MySQLiteHelper.COL_PRENOM,
            MySQLiteHelper.COL_NUMERO
    };

    public ContactDAO(Context context) {
        this._context = context;
        this._dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        this._database = _dbHelper.getWritableDatabase();
    }

    public void close() {
        this._dbHelper.close();
    }

    public Contact createContact(String nom, String prenom, String numero) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COL_NOM, nom);
        values.put(MySQLiteHelper.COL_PRENOM, prenom);
        values.put(MySQLiteHelper.COL_NUMERO, numero);

        long insertId = this._database.insert(MySQLiteHelper.TABLE_CONTACTS, null, values);
        Cursor cursor = this._database.query(MySQLiteHelper.TABLE_CONTACTS, allColumns, MySQLiteHelper.COL_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Contact ret = this.cursorToContact(cursor);
        cursor.close();
        return (ret);
    }

    public void deleteContact(Contact contact) {
        long id = contact.get_id();
        _database.delete(MySQLiteHelper.TABLE_CONTACTS, MySQLiteHelper.COL_ID + " = " + id, null);
    }

    public List<Contact> getAllContacts() {
        List<Contact> ret = new ArrayList<Contact>();

        Cursor cursor = this._database.query(MySQLiteHelper.TABLE_CONTACTS, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Contact cont = this.cursorToContact(cursor);
            ret.add(cont);
            cursor.moveToNext();
        }
        cursor.close();
        return ret;
    }

    public Contact getContact(long id) {
        Cursor cursor = this._database.query(MySQLiteHelper.TABLE_CONTACTS, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        Contact ret = this.cursorToContact(cursor);
        cursor.close();
        return ret;
    }

    private Contact cursorToContact(Cursor cursor){
        Contact contact = new Contact(
                cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteHelper.COL_ID)),
                cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COL_NOM)),
                cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COL_PRENOM)),
                cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COL_NUMERO)),
                ResourcesCompat.getDrawable(this._context.getResources(), R.mipmap.ic_person, null));
        return (contact);
    }
}