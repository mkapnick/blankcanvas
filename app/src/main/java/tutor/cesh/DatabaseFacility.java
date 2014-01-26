package tutor.cesh;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Michael Kapnick on 1/25/14.
 * A class responsible for maintaining the database
 * and querying the database
 */
public class DatabaseFacility extends SQLiteOpenHelper
{

    private static final int    DATABASE_VERSION    = 1;
    private static final String DATABASE_NAME       = "tutorcesh.db";
    private ContentValues       contentValues;
    private SQLiteDatabase      db;

    /* User Table */
    private static final String USER                = "User";
    private static final String PASSWORD            = "password";
    private static final String EMAIL               = "email";
    private static final String FIRSTNAME           = "firstname";
    private static final String LASTNAME            = "lastname";
    private static final String ABOUT               = "about";
    private static final String PROFILE_PIC         = "profile_pic";
    private static final String COVER_PIC           = "cover_pic";

    /* Enroll Table */
    private static final String ENROLL              = "Enroll";
    private static final String MAJOR               = "major";
    private static final String YEAR                = "year";

    /* School Table */
    private static final String SCHOOL              = "School";
    private static final String SCHOOLNAME          = "name";
    private static final String DOMAIN              = "domain";
    private static final String CITY                = "city";
    private static final String STATE               = "state";

    /* Offer Table */
    private static final String OFFER               = "Offer";
    private static final String TIME                = "time";
    private static final String BUILDING            = "building";

    /* Course Table */
    private static final String COURSE              = "Course";
    private static final String CLASS_NUMBER        = "class_number";
    private static final String COURSE_NUMBER       = "course_number";
    private static final String DEPARTMENT          = "department";
    private static final String TITLE               = "title";

    /* Tutor Table */
    private static final String TUTOR               = "Tutor";
    private static final String RATE                = "rate";
    private static final String LOCATION            = "location";

    /**
     * Creates the database by passing super the database name
     * and the database version
     * @param context The context of the application
     */
    public DatabaseFacility(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        System.out.println("Inside Database Facility Constructor");

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        System.out.println("Inside onCreate method in Database Facility");
        sqLiteDatabase.execSQL("CREATE TABLE " + USER + "( "
                                + EMAIL         + " TEXT PRIMARY KEY, "
                                + PASSWORD      + " TEXT, "
                                + FIRSTNAME     + " TEXT, "
                                + LASTNAME      + " TEXT, "
                                + ABOUT         + " TEXT, "
                                + PROFILE_PIC   + " BLOB, "
                                + COVER_PIC     + " BLOB )"
                              );

        sqLiteDatabase.execSQL("CREATE TABLE " + SCHOOL + "( "
                                + DOMAIN        + " TEXT PRIMARY KEY, "
                                + SCHOOLNAME    + " TEXT, "
                                + CITY          + " TEXT, "
                                + STATE         + " TEXT "
                              );

        sqLiteDatabase.execSQL("CREATE TABLE " + COURSE + "( "

                                + CLASS_NUMBER  + " TEXT PRIMARY KEY, "
                                + COURSE_NUMBER + " TEXT, "
                                + DEPARTMENT    + " TEXT, "
                                + TITLE         + " TEXT"

                              );

        sqLiteDatabase.execSQL("CREATE TABLE " + ENROLL + "( "
                                + EMAIL         + " TEXT, "
                                + DOMAIN        + " TEXT, "
                                + MAJOR         + " TEXT, "
                                + YEAR          + " TEXT, "
                                +                 "PRIMARY KEY (" + EMAIL + ", " + DOMAIN + "), "

                                +                 "FOREIGN KEY (" + EMAIL + ") " + "REFERENCES "
                                +                  USER + "(" + EMAIL + "), "

                                +                 "FOREIGN KEY (" + DOMAIN + ") " + "REFERENCES "
                                +                  SCHOOL + "(" + DOMAIN + ")"

                              );

        sqLiteDatabase.execSQL("CREATE TABLE " + OFFER + "( "
                                + TIME          + " TEXT, "
                                + BUILDING      + " TEXT, "
                                + DOMAIN        + " TEXT, "
                                + CLASS_NUMBER  + " TEXT, "

                                +                 "PRIMARY KEY (" + DOMAIN + ", " + CLASS_NUMBER
                                +                 "), "

                                +                 "FOREIGN KEY (" + DOMAIN + ") " + "REFERENCES"
                                +                  SCHOOL + "(" + DOMAIN + "), "

                                +                 "FOREIGN KEY (" + CLASS_NUMBER + ") " + "REFERENCES "
                                +                 COURSE + "(" + CLASS_NUMBER + ")"

                              );

        sqLiteDatabase.execSQL("CREATE TABLE " + TUTOR + "( "
                                + RATE          + "TEXT, "
                                + LOCATION      + "TEXT, "
                                + EMAIL         + "TEXT, "
                                + CLASS_NUMBER  + "TEXT, "

                                +                 "PRIMARY KEY (" + EMAIL + ", " + CLASS_NUMBER
                                +                  "), "

                                +                 "FOREIGN KEY (" + EMAIL + ") " + "REFERENCES "
                                +                  USER + "(" + EMAIL + "), "

                                +                 "FOREIGN KEY (" + CLASS_NUMBER + ") " + "REFERENCES "
                                +                  COURSE + "(" + CLASS_NUMBER + ")"

                              );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ENROLL);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SCHOOL);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OFFER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TUTOR);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + COURSE);

        onCreate(sqLiteDatabase);

    }

    /**
     * Sets the current database object
     * @param db The current database object
     */
    public void setDatabase(SQLiteDatabase db)
    {
        this.db = db;
    }

    /**
     * Insert data into the User table
     * @param email The user's email
     * @param password User's password
     * @param firstName User's first name
     * @param lastName User's last name
     * @param about User's about info
     */

    public void insertUserRecord(String email, String password, String firstName, String lastName, String about)
    {
        contentValues = new ContentValues();
        contentValues.put(EMAIL, email);
        contentValues.put(PASSWORD, password);
        contentValues.put(FIRSTNAME, firstName);
        contentValues.put(LASTNAME, lastName);
        contentValues.put(ABOUT, about);

        this.db.insert(USER, null, contentValues);
    }

    /**
     * Insert data into the School table
     * @param name School name user attends
     * @param domain domain name of school
     * @param city City of school
     * @param state State of school
     */
    public void insertSchoolRecord(String name, String domain, String city, String state)
    {
        contentValues = new ContentValues();
        contentValues.put(SCHOOLNAME, name);
        contentValues.put(DOMAIN, domain);
        contentValues.put(CITY, city);
        contentValues.put(STATE, state);

        this.db.insert(SCHOOL, null, contentValues);

    }

    /**
     * Insert data into the Course table
     * @param classNumber A particular class number
     * @param courseNumber A particular course number
     * @param department The department the class is offered in
     * @param title The title of the class
     */
    public void insertCourseRecord(String classNumber, String courseNumber, String department,
                                   String title)
    {
        contentValues = new ContentValues();
        contentValues.put(CLASS_NUMBER, classNumber);
        contentValues.put(COURSE_NUMBER, courseNumber);
        contentValues.put(DEPARTMENT, department);
        contentValues.put(TITLE, title);

        this.db.insert(COURSE, null, contentValues);

    }

    /**
     * Insert data into the Enroll table
     * @param email User's email address
     * @param domain School's domain name
     * @param major User's major
     * @param year Year of graduation
     */
    public void insertEnrollRecord(String email, String domain, String major, String year)
    {
        contentValues = new ContentValues();
        contentValues.put(EMAIL, email);
        contentValues.put(DOMAIN, domain);
        contentValues.put(MAJOR, major);
        contentValues.put(YEAR, year);

        this.db.insert(ENROLL, null, contentValues);

    }


    /**
     * Insert data into the Tutor table
     * @param email User's email address
     * @param classNumber The class number for a particular class
     * @param rate Tutoring rate for one session
     * @param location Location of tutoring session
     */
    public void insertTutorRescord(String email, String classNumber, String rate, String location)
    {
        contentValues = new ContentValues();
        contentValues.put(EMAIL, email);
        contentValues.put(CLASS_NUMBER, classNumber);
        contentValues.put(RATE, rate);
        contentValues.put(LOCATION, location);

        this.db.insert(ENROLL, null, contentValues);

    }

    /**
     * Insert data into the Offer table
     * @param domain The domain name of the school
     * @param classNumber The class number
     * @param building The building the class is located in
     * @param time The time of the class
     */
    public void insertOfferRecord(String domain, String classNumber, String building, String time)
    {
        contentValues = new ContentValues();
        contentValues.put(DOMAIN, domain);
        contentValues.put(CLASS_NUMBER, classNumber);
        contentValues.put(BUILDING, building);
        contentValues.put(TIME, time);

        this.db.insert(OFFER, null, contentValues);

    }

    public boolean validateUser(String email, String password)
    {
        String  query;
        String  dbEmail;
        String  dbPassword;
        Cursor  cursor;
        boolean bool;

        query   = "SELECT * FROM USER WHERE email = " + email + " AND password = " + password;
        cursor  = this.db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            dbEmail     = cursor.getString(1);
            dbPassword  = cursor.getString(2);

            if(dbEmail.equalsIgnoreCase(email) && dbPassword.equalsIgnoreCase(password))
                bool = true;
            else
                bool = false;
        }
        else
        {
            bool = false;
        }

        return bool;
    }

    public void executeQuery(String query)
    {
        Cursor cursor;
        cursor = this.db.rawQuery(query, null);


    }
}
