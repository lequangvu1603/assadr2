package fpt.vulq.ass2adr2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PersonalDevelopment.db";
    private static final int DATABASE_VERSION = 1;

    // Table for user authentication
    private static final String TABLE_USER = "User";
    private static final String COLUMN_USER_ID = "UserID";
    private static final String COLUMN_USER_EMAIL = "Email";
    private static final String COLUMN_USER_PASSWORD = "Password";

    // Table for gratitude entries
    private static final String TABLE_GRATITUDE = "Gratitude";
    private static final String COLUMN_GRATITUDE_ID = "GratitudeID";
    private static final String COLUMN_GRATITUDE_CONTENT = "Content";
    private static final String COLUMN_GRATITUDE_DATE = "Date";
    private static final String COLUMN_USER_ID_FK = "UserID";

    // Table for user personal information
    private static final String TABLE_PERSONAL_INFO = "PersonalInfo";
    private static final String COLUMN_INFO_ID = "InfoID";
    private static final String COLUMN_INFO_GENDER = "Gender";
    private static final String COLUMN_INFO_HEIGHT = "Height";
    private static final String COLUMN_INFO_WEIGHT = "Weight";
    private static final String COLUMN_INFO_BMI = "BMI";

    // Table for step count
    private static final String TABLE_STEP_COUNT = "StepCount";
    private static final String COLUMN_STEP_ID = "StepID";
    private static final String COLUMN_STEP_COUNT = "Count";
    private static final String COLUMN_STEP_DATE = "Date";
    private static final String COLUMN_STEP_GOAL = "Goal";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {
        // Create user table
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_EMAIL + " TEXT,"
                + COLUMN_USER_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);

        // Create gratitude table
        String CREATE_GRATITUDE_TABLE = "CREATE TABLE " + TABLE_GRATITUDE + "("
                + COLUMN_GRATITUDE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_GRATITUDE_CONTENT + " TEXT,"
                + COLUMN_GRATITUDE_DATE + " TEXT,"
                + COLUMN_USER_ID_FK + " INTEGER, FOREIGN KEY(" + COLUMN_USER_ID_FK + ") REFERENCES "
                + TABLE_USER + "(" + COLUMN_USER_ID + "))";
        db.execSQL(CREATE_GRATITUDE_TABLE);

        // Create personal info table
        String CREATE_PERSONAL_INFO_TABLE = "CREATE TABLE " + TABLE_PERSONAL_INFO + "("
                + COLUMN_INFO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_INFO_GENDER + " TEXT,"
                + COLUMN_INFO_HEIGHT + " REAL,"
                + COLUMN_INFO_WEIGHT + " REAL,"
                + COLUMN_INFO_BMI + " REAL,"
                + COLUMN_USER_ID_FK + " INTEGER, FOREIGN KEY(" + COLUMN_USER_ID_FK + ") REFERENCES "
                + TABLE_USER + "(" + COLUMN_USER_ID + "))";
        db.execSQL(CREATE_PERSONAL_INFO_TABLE);

        // Create step count table
        String CREATE_STEP_COUNT_TABLE = "CREATE TABLE " + TABLE_STEP_COUNT + "("
                + COLUMN_STEP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_STEP_COUNT + " INTEGER,"
                + COLUMN_STEP_DATE + " TEXT,"
                + COLUMN_STEP_GOAL + " INTEGER,"
                + COLUMN_USER_ID_FK + " INTEGER, FOREIGN KEY(" + COLUMN_USER_ID_FK + ") REFERENCES "
                + TABLE_USER + "(" + COLUMN_USER_ID + "))";
        db.execSQL(CREATE_STEP_COUNT_TABLE);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRATITUDE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSONAL_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEP_COUNT);
        onCreate(db);
    }
}
