    package com.example.a2048;

    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.DatabaseUtils;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;
    import android.util.Log;

    import androidx.annotation.Nullable;

    import static android.content.ContentValues.TAG;

    public class WordListOpenHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String WORD_LIST_TABLE = "word_entries";
        private static final String DATABASE_NAME = "wordlist";

        public static final String KEY_ID = "_id";
        public static final String PUNTUATION = "puntuation";
        public static final String USER_NAME = "definition";
        public static final String DATE = "date";

        private SQLiteDatabase mWritableDB;
        private SQLiteDatabase mReadableDB;

        private static final String WORD_LIST_TABLE_CREATE =
                "CREATE TABLE " + WORD_LIST_TABLE + " (" +
                        KEY_ID + " INTEGER PRIMARY KEY, " + PUNTUATION + " TEXT ,"+ USER_NAME +" TEXT, "+ DATE +" TEXT );";

        public WordListOpenHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(WORD_LIST_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(com.example.a2048.WordListOpenHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + WORD_LIST_TABLE);
            onCreate(db);
        }

        public WordItem query(int position){
            String query = "SELECT * FROM " + WORD_LIST_TABLE +
                    " ORDER BY " + PUNTUATION + " ASC " +
                    "LIMIT " + position + ",1";
            Cursor cursor= null;
            WordItem entry = new WordItem();

            try {
                if (mReadableDB == null) {
                    mReadableDB = getReadableDatabase();
                }
                cursor = mReadableDB.rawQuery(query, null);
                cursor.moveToFirst();
                entry.setmId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                entry.setPuntuation(cursor.getString(cursor.getColumnIndex(PUNTUATION)));
                entry.setUsername(cursor.getString(cursor.getColumnIndex(USER_NAME)));
                entry.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
            }catch (Exception e){
                Log.d("query: ", "EXCEPTION! " + e);
            }finally {
                cursor.close();
                return entry;
            }
        }

        public long insert(String puntuation, String username, String date){
            long newId = 0;
            ContentValues values = new ContentValues();
            values.put(PUNTUATION, puntuation);
            values.put(USER_NAME, username);
            values.put(DATE, date);

            try{
                if (mWritableDB == null) {
                    mWritableDB = getWritableDatabase();
                }
                newId = mWritableDB.insert(WORD_LIST_TABLE, null, values);
            }catch (Exception e){
                Log.d(TAG,"INSERT EXCEPTION! "+ e.getMessage());
            }finally {
                return  newId;
            }
        }

        public long count(){
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            return DatabaseUtils.queryNumEntries(mReadableDB, WORD_LIST_TABLE);
        }

        public int update(int id, String username){
            int nNumberOfRowsUpdated=-1;
            if (mWritableDB==null){
                mWritableDB= getWritableDatabase();
            }
            ContentValues values= new ContentValues();
            try {
                values.put(USER_NAME,username);
                nNumberOfRowsUpdated = mWritableDB.update(WORD_LIST_TABLE,
                        values, KEY_ID + " = ?",
                        new String[]{String.valueOf(id)});
            } catch (Exception e) {
                Log.d("update","UPDATE EXCEPTION:"+ e.getMessage());
            }
            return nNumberOfRowsUpdated;
        }

        public int delete(int id){
            int deleted = 0;
            try{
                if (mWritableDB == null) {
                    mWritableDB = getWritableDatabase();
                }
                deleted = mWritableDB.delete(WORD_LIST_TABLE,
                        KEY_ID + " = ? ", new String[]{String.valueOf(id)});

            }catch (Exception e){
                Log.d("delete",e.getMessage());
            }
            return deleted;
        }


        public Cursor searchForPuntuation(String puntuation, int radioStatus) {
            String[] columns = new String[]{PUNTUATION,USER_NAME,DATE};
            String searchString =  "%" + puntuation + "%";
            String where = null;
            if(radioStatus == 1){
                where = PUNTUATION + " LIKE ? ";
            }else if(radioStatus == 2){
                where = PUNTUATION + " >= ? ";
            }else if(radioStatus == 3){
                where = PUNTUATION + " <= ? ";
            }

            String[] whereArgs = new String[]{searchString};
            Cursor cursor= null;

            try{
                if (mReadableDB==null){
                    mReadableDB = getReadableDatabase();
                }
                cursor = mReadableDB.query(WORD_LIST_TABLE,columns,where,whereArgs,null,null,null);
            }catch (Exception e){
                Log.d("TAG","ERROR");
            }
            return cursor;
        }

        public Cursor searchForUsername(String username) {
            String[] columns = new String[]{PUNTUATION,USER_NAME,DATE};
            String searchString =  "%" + username + "%";
            String where = USER_NAME + " LIKE ? ";
            String[] whereArgs = new String[]{searchString};
            Cursor cursor= null;

            try{
                if (mReadableDB==null){
                    mReadableDB = getReadableDatabase();
                }
                cursor = mReadableDB.query(WORD_LIST_TABLE,columns,where,whereArgs,null,null,null);
            }catch (Exception e){
                Log.d("TAG","ERROR");
            }
            return cursor;
        }
    }
