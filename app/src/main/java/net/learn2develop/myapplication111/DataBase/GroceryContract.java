package net.learn2develop.myapplication111.DataBase;

import android.provider.BaseColumns;

public class GroceryContract {
    private GroceryContract(){

    }

    public static final class GroceryEntry implements BaseColumns {

        public static final String TABLE_NAME = "groceryList";
        public static final String COLUMN_TIME = "name";
        public static final String COLUMN_DATE = "amount";
        public static final String COLUMN_PROFILE = "profile";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

}
