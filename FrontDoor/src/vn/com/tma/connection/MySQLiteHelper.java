package vn.com.tma.connection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import vn.com.tma.model.AcceptRuleVO;
import vn.com.tma.model.IgnoreRuleVO;
import vn.com.tma.model.StateSensorVO;
import vn.com.tma.util.TimeConvertUtil;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final int HAVE_OLD_STATE = 2;

    private static final int NEW_STATE = 1;

    // -----------------------DATABASE------------------------
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MyDB_Minh3";

    // -----------------------TABLE------------------------
    // State table
    private static final String TABLE_STATE = "states";

    // state Table Columns names
    private static final String KEY_ID = "id";

    private static final String KEY_DATE = "date";

    private static final String KEY_TIME = "time";

    private static final String KEY_STATE = "state";

    private static final String[] COLUMNS = { KEY_ID, KEY_DATE, KEY_TIME, KEY_STATE };

    // ignore table name
    private static final String TABLE_IGNORE = "ignores";

    private static final String KEY_OPTION = "option";

    // ignore Table Columns names
    private static final String[] COLUMNS_IGNORE = { KEY_ID, KEY_OPTION, KEY_TIME };

    // Notification Rule table

    private static final String TABLE_NOTIFICATION_RULE = "notification_rule";

    // Notification Rule Table Columns names
    private static final String KEY_NAME = "name";

    private static final String KEY_FROM = "m_from";

    private static final String KEY_TO = "m_to";

    private static final String KEY_IS_CHECKED = "isChecked";

    private static final String[] COLUMNS_NOTIFICATION_RULE = { KEY_ID, KEY_NAME, KEY_FROM, KEY_TO, KEY_IS_CHECKED };

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create state table
        String CREATE_STATE_TABLE = "CREATE TABLE  " + TABLE_STATE + " (" + "id INTEGER PRIMARY KEY , " + "date DATE, " + "time TEXT, " + "state TEXT " + ")";

        // create state table
        db.execSQL(CREATE_STATE_TABLE);

        // SQL statement to create state table
        String CREATE_IGNORE_TABLE = "CREATE TABLE  " + TABLE_IGNORE + " (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "option TEXT, " + "time TEXT )";

        // create state table
        db.execSQL(CREATE_IGNORE_TABLE);

        // SQL statement to create state table
        String CREATE_NOTIFICATION_RULE_TABLE = "CREATE TABLE  " + TABLE_NOTIFICATION_RULE + " (" + "id INTEGER PRIMARY KEY , " + "name TEXT, " + "m_from TEXT," + " m_to TEXT,"
                + " isChecked INTEGER )";

        // create state table
        db.execSQL(CREATE_NOTIFICATION_RULE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older state table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATE);

        // Drop older state table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IGNORE);

        // Drop older state table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION_RULE);

        // create fresh state table
        this.onCreate(db);

    }

    public void resetIDOnTable() {
        String sqlDeleteTable = "DELETE FROM " + TABLE_IGNORE;
        String sql = "DELETE FROM sqlite_sequence WHERE name = 'ignores'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sqlDeleteTable);
        db.execSQL(sql);

    }

    public void addOnlyNewState(StateSensorVO newStateFromService) {
        StateSensorVO newestStateInDB = getNewerDayStateInDB();
        SimpleDateFormat parserDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat parserTime = new SimpleDateFormat("HH:mm");

        try {
            // Date
            Date dateOnDB = parserDate.parse(newestStateInDB.getDate());
            Date dateCurently = parserDate.parse(newStateFromService.getDate());
            // Time
            Date timeNew = parserTime.parse(newStateFromService.getTime());
            Date timeOld = parserTime.parse(newestStateInDB.getTime());
            Log.d("DateOnDB", parserDate.format(dateOnDB));
            Log.d("DateCurrently", parserDate.format(dateCurently));
            if (dateOnDB.compareTo(dateCurently) < 0 || (dateOnDB.compareTo(dateCurently) == 0 && timeOld.compareTo(timeNew) < 0)) {
                addState(newStateFromService);
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public int addState(StateSensorVO state) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("addState", state.toString());
        // 1. get reference to writable DB
        try {

            // 2. create ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put(KEY_ID, state.getId());
            values.put(KEY_DATE, state.getDate());
            values.put(KEY_TIME, state.getTime());
            values.put(KEY_STATE, state.getState());

            // 3. insert
            int id = (int) db.insert(TABLE_STATE, null, values);
            return id;

        } catch (Exception e) {
            e.getMessage();
        } finally {
            // 4. close
            db.close();
        }
        return 0;
    }

    public int addIgnore(IgnoreRuleVO ignore) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("addIgnore", ignore.toString());
        // 1. get reference to writable DB
        try {
            // 2. create ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put(KEY_OPTION, ignore.getOption());
            values.put(KEY_TIME, ignore.getTime());

            // 3. insert
            int id = (int) db.insert(TABLE_IGNORE, null, values);
            return id;
        } catch (Exception e) {
            e.getMessage();
        } finally {
            // 4. close
            db.close();
        }
        return 0;
    }

    public int addNotificationRule(AcceptRuleVO notificationRule) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("addNotificationRule", notificationRule.toString());
        // 1. get reference to writable DB
        try {

            // 2. create ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put(KEY_ID, notificationRule.getId());
            values.put(KEY_NAME, notificationRule.getName());
            values.put(KEY_FROM, notificationRule.getFrom());
            values.put(KEY_TO, notificationRule.getTo());
            values.put(KEY_IS_CHECKED, notificationRule.isChecked() ? 1 : 0);

            // 3. insert
            int id = (int) db.insert(TABLE_NOTIFICATION_RULE, null, values);
            return id;

        } catch (Exception e) {
            e.getMessage();
        } finally {
            // 4. close
            db.close();
        }
        return 0;
    }

    public StateSensorVO getNewerDayStateInDB() {
        int id = getIdNewestState();
        StateSensorVO state = getState(id);
        return state;
    }

    public int getIdNewestState() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // 1. build the query
            String query = "SELECT MAX(id) FROM " + TABLE_STATE + " ORDER BY " + KEY_ID + " DESC";
            // 2. get reference to writable DB
            Cursor cursor = db.rawQuery(query, null);
            int id = 0;
            // 3. go over each row, build state and add it to list
            if (cursor.moveToFirst()) {
                do {
                    id = Integer.parseInt(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            return id;

        } catch (Exception e) {
            e.getMessage();
        } finally {
            db.close();
        }
        return 0;
    }

    public StateSensorVO getState(int id) {

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            // 2. build query
            Cursor cursor = db.query(TABLE_STATE, COLUMNS, " id = ?", new String[] { String.valueOf(id) }, null, null, null, null);

            // 3. if we got results get the first one
            if (cursor != null)
                cursor.moveToFirst();

            // 4. build state object
            StateSensorVO state = new StateSensorVO();
            state.setId(Integer.parseInt(cursor.getString(0)));
            state.setDate(cursor.getString(1));
            state.setTime(cursor.getString(2));
            state.setState(cursor.getString(3));

            Log.d("getState(" + id + ")", state.toString());

            // 5. return state
            return state;
        } catch (Exception e) {
            e.getMessage();
        } finally {
            db.close();
        }
        return null;
    }

    // Get All state
    public List<StateSensorVO> getAllState() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            List<StateSensorVO> stateList = new ArrayList<StateSensorVO>();

            // 1. build the query
            String query = "SELECT  * FROM " + TABLE_STATE + " ORDER BY " + KEY_ID + " DESC";

            // 2. get reference to writable DB
            Cursor cursor = db.rawQuery(query, null);

            // 3. go over each row, build state and add it to list
            StateSensorVO state = null;
            if (cursor.moveToFirst()) {
                do {
                    state = new StateSensorVO();
                    state.setId(Integer.parseInt(cursor.getString(0)));
                    state.setDate(cursor.getString(1));
                    state.setTime(cursor.getString(2));
                    state.setState(cursor.getString(3));

                    // Add state to list state
                    stateList.add(state);
                } while (cursor.moveToNext());
            }

            Log.d("getAllState()", stateList.toString());

            // return state
            return stateList;
        } catch (Exception e) {
            e.getMessage();
        } finally {
            db.close();
        }
        return null;
    }

    // Get All state
    public List<IgnoreRuleVO> getAllIgnore() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            List<IgnoreRuleVO> ignoreList = new ArrayList<IgnoreRuleVO>();

            // 1. build the query
            String query = "SELECT  * FROM " + TABLE_IGNORE + " ORDER BY " + KEY_ID + " DESC";

            // 2. get reference to writable DB
            Cursor cursor = db.rawQuery(query, null);

            // 3. go over each row, build state and add it to list
            IgnoreRuleVO ignore = null;
            if (cursor.moveToFirst()) {
                do {
                    ignore = new IgnoreRuleVO();
                    ignore.setId(Integer.parseInt(cursor.getString(0)));
                    ignore.setOption(cursor.getString(1));
                    ignore.setTime(cursor.getString(2));

                    // Add state to list state
                    ignoreList.add(ignore);
                } while (cursor.moveToNext());
            }

            Log.d("getAllIgnore()", ignoreList.toString());

            // return state
            return ignoreList;
        } catch (Exception e) {
            e.getMessage();
        } finally {
            db.close();
        }
        return null;
    }

    // Get All state
    public List<AcceptRuleVO> getAllNotificationRule() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            List<AcceptRuleVO> notificationList = new ArrayList<AcceptRuleVO>();

            // 1. build the query
            String query = "SELECT  * FROM " + TABLE_NOTIFICATION_RULE + " ORDER BY " + KEY_ID + " ASC";

            // 2. get reference to writable DB
            Cursor cursor = db.rawQuery(query, null);

            // 3. go over each row, build state and add it to list
            AcceptRuleVO notificationRule = null;
            if (cursor.moveToFirst()) {
                do {
                    notificationRule = new AcceptRuleVO();
                    notificationRule.setId(Integer.parseInt(cursor.getString(0)));
                    notificationRule.setName(cursor.getString(1));
                    notificationRule.setFrom(cursor.getString(2));
                    notificationRule.setTo(cursor.getString(3));
                    notificationRule.setChecked(cursor.getInt(4) == 1 ? true : false);

                    // Add state to list state
                    notificationList.add(notificationRule);
                } while (cursor.moveToNext());
            }

            Log.d("getAllNotificationRule()", notificationList.toString());

            // return state
            return notificationList;
        } catch (Exception e) {
            e.getMessage();
        } finally {
            db.close();
        }
        return null;
    }

    // Updating single state
    public int updateState(StateSensorVO state) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // 2. create ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put("date", state.getDate());
            values.put("time", state.getTime());
            values.put("state", state.getState());

            // 3. updating row
            int i = db.update(TABLE_STATE, values, KEY_ID + " = ?", new String[] { String.valueOf(state.getId()) });
            return i;

        } catch (Exception e) {
            e.getMessage();
        } finally {
            // 4. close
            db.close();
        }
        return 0;
    }

    // Updating single state
    public int updateNotification(AcceptRuleVO notificationRule) {
        Log.d("Update_Notification_Rule", notificationRule.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // 2. create ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put(KEY_ID, notificationRule.getId());
            values.put(KEY_NAME, notificationRule.getName());
            values.put(KEY_FROM, notificationRule.getFrom());
            values.put(KEY_TO, notificationRule.getTo());
            values.put(KEY_IS_CHECKED, notificationRule.isChecked() ? 1 : 0);

            // 3. updating row
            int i = db.update(TABLE_NOTIFICATION_RULE, values, KEY_ID + " = ?", new String[] { String.valueOf(notificationRule.getId()) });
            return i;

        } catch (Exception e) {
            e.getMessage();
        } finally {
            // 4. close
            db.close();
        }
        return 0;
    }

    // Deleting single state
    public int deleteState(StateSensorVO state) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // 2. delete
            int id = db.delete(TABLE_STATE, KEY_ID + " = ?", new String[] { String.valueOf(state.getId()) });
            // 3. close
            db.close();

            Log.d("deleteState", state.toString());

            return id;
        } catch (Exception e) {
        } finally {
            db.close();
        }
        return 0;
    }

    // Deleting single state
    public int deleteIgnore(IgnoreRuleVO ignore) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // 2. delete
            int id = db.delete(TABLE_IGNORE, KEY_ID + " = ?", new String[] { String.valueOf(ignore.getId()) });
            // 3. close
            db.close();

            Log.d("deleteIgnore", ignore.toString());

            return id;
        } catch (Exception e) {
            e.getMessage();
        } finally {
            db.close();
        }
        return 0;
    }

    // Clean all state
    public void cleanAllState() {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ListIterator<StateSensorVO> stateLite = getAllState().listIterator();
            while (stateLite.hasNext()) {
                deleteState(stateLite.next());
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            // 3. close
            db.close();
        }
    }

    // Clean all state
    public void cleanAllIgnore() {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ListIterator<IgnoreRuleVO> ignoreLite = getAllIgnore().listIterator();
            while (ignoreLite.hasNext()) {
                deleteIgnore(ignoreLite.next());
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            // 3. close
            db.close();
        }
    }

    public void addListState(List<StateSensorVO> listState) {
        List<StateSensorVO> listAllState = getAllState();
        if (listAllState.isEmpty()) {
            chooseAddOptions(NEW_STATE, listState);
        } else {
            chooseAddOptions(HAVE_OLD_STATE, listState);
        }
    }

    public void chooseAddOptions(int key, List<StateSensorVO> listState) {
        switch (key) {
        case NEW_STATE:
            try {
                int i = 0;
                while (i < listState.size()) {
                    addState(listState.get(i));
                    i++;
                }
            } catch (Exception e) {
                e.getMessage();
            }
            break;

        case HAVE_OLD_STATE:
            try {
                int i = listState.size() - 1;
                while (i >= 0) {
                    if (compareDate(listState.get(i))) {
                        addOnlyNewState(listState.get(i));
                        i--;
                    } else {
                        break;
                    }
                }
            } catch (Exception e) {
                e.getMessage();
            }
            break;

        default:
            break;
        }
    }

    public boolean compareDate(StateSensorVO state) throws ParseException {
        SimpleDateFormat parserDate = new SimpleDateFormat("yyyy-MM-dd");

        Date currentdayInDB = parserDate.parse(getNewerDayStateInDB().getDate());
        long currentTimeInDB = TimeConvertUtil.getSecondTime(getNewerDayStateInDB().getTime());

        Date dateItem = parserDate.parse(state.getDate().toString());
        long timeItem = TimeConvertUtil.getSecondTime(state.getTime());
        if (currentdayInDB.compareTo(dateItem) > 0 || (currentdayInDB.compareTo(dateItem) == 0 && currentTimeInDB > timeItem)
                || (currentdayInDB.compareTo(dateItem) == 0 && currentTimeInDB == timeItem)) {
            return false;
        } else {
            return true;
        }

    }
}