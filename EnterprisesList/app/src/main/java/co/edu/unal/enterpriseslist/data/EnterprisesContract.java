package co.edu.unal.enterpriseslist.data;

import android.provider.BaseColumns;

public class EnterprisesContract {
    //saves table column  names
    public static abstract class EnterpriseEntry implements BaseColumns {
        public static final String TABLE_NAME = "enterprise";

        public static final String ID = "id";

        public static final String NAME = "name";
        public static final String WEBURL = "webURL";
        public static final String PHONECONTACT = "phoneContact";
        public static final String MAILCONTACT = "mailContact";
        public static final String PRODUCTS = "products";
        public static final String CONSULTING = "consulting";
        public static final String SOFTDEVELOP = "softdevelop";
        public static final String SOFTFABRIC = "softfabric";
        public static final String AVATAR_URI = "avatarUri";
    }
}
