package co.edu.unal.enterpriseslist.data;

import android.content.ContentValues;
import android.database.Cursor;
import co.edu.unal.enterpriseslist.data.EnterprisesContract.EnterpriseEntry;
import java.util.UUID;

public class Enterprise {
    private String id;
    private String name;
    private String webURL;
    private String phoneContact;
    private String mailContact;
    private String products;
    private int consulting;
    private int softDevelop;
    private int softFabric;
    public String avatarUri;


    public Enterprise(String name,String webURL,String phoneContact,
            String mailContact, String products, int consulting,
            int softDevelop, int softFabric, String avatarUri){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.webURL = webURL;
        this.phoneContact = phoneContact;
        this.mailContact = mailContact;
        this.products = products;
        this.consulting = consulting;
        this.softDevelop = softDevelop;
        this.softFabric = softFabric;
        this.avatarUri = avatarUri;
    }

    public Enterprise(Cursor cursor){
        id = cursor.getString(cursor.getColumnIndex(EnterpriseEntry.ID));
        name = cursor.getString(cursor.getColumnIndex(EnterpriseEntry.NAME));
        webURL = cursor.getString(cursor.getColumnIndex(EnterpriseEntry.WEBURL));
        phoneContact = cursor.getString(cursor.getColumnIndex(EnterpriseEntry.PHONECONTACT));
        mailContact = cursor.getString(cursor.getColumnIndex(EnterpriseEntry.MAILCONTACT));
        products = cursor.getString(cursor.getColumnIndex(EnterpriseEntry.PRODUCTS));
        consulting = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EnterpriseEntry.CONSULTING)));
        softDevelop = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EnterpriseEntry.SOFTDEVELOP)));
        softFabric = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EnterpriseEntry.SOFTFABRIC)));
        avatarUri = cursor.getString(cursor.getColumnIndex(EnterpriseEntry.AVATAR_URI));
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return  name;
    }

    public String getWebURL(){
        return webURL;
    }

    public String getMailContact() {
        return mailContact;
    }

    public String getPhoneContact() {
        return phoneContact;
    }

    public String getProducts() {
        return products;
    }

    public int isConsulting() {
        return consulting;
    }

    public int isSoftDevelop() {
        return softDevelop;
    }

    public int isSoftFabric() {
        return softFabric;
    }

    public String getAvatarUri() {return avatarUri;
    }

    //translates Enterprise
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(EnterprisesContract.EnterpriseEntry.ID, id);
        values.put(EnterprisesContract.EnterpriseEntry.NAME, name);
        values.put(EnterprisesContract.EnterpriseEntry.WEBURL, webURL);
        values.put(EnterprisesContract.EnterpriseEntry.MAILCONTACT, mailContact);
        values.put(EnterprisesContract.EnterpriseEntry.PHONECONTACT, phoneContact);
        values.put(EnterprisesContract.EnterpriseEntry.PRODUCTS, products);
        values.put(EnterprisesContract.EnterpriseEntry.CONSULTING, consulting);
        values.put(EnterprisesContract.EnterpriseEntry.SOFTDEVELOP, softDevelop);
        values.put(EnterprisesContract.EnterpriseEntry.SOFTFABRIC, softFabric);
        values.put(EnterprisesContract.EnterpriseEntry.AVATAR_URI, avatarUri);
        return values;
    }
}
