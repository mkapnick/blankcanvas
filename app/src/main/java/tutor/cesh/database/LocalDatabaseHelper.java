package tutor.cesh.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import java.io.ByteArrayOutputStream;

/***
 * Builds/updates/maintains a local sqlite database for our app
 * This class is used but is not maintained
 *
 * @version v1.0
 * @author  Michael Kapnick
 */
public class LocalDatabaseHelper extends SQLiteOpenHelper
{
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "local_blankcanvas_development";

    // Contacts table name
    private static final String TABLE_USER = "user";


    // Contacts Table Columns names
    private static final String ID                  = "id";
    private static final String STUDENT_COVER_IMAGE = "student_cover_image";
    private static final String TUTOR_COVER_IMAGE   = "tutor_cover_image";
    private static long  studentCoverImageId        = -1; //sentinal value
    private static long  tutorCoverImageId          = -1; //sentinal value


    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USER + "("
            + ID + " INTEGER PRIMARY KEY," + STUDENT_COVER_IMAGE + " BLOB,"
            + TUTOR_COVER_IMAGE + " BLOB" + ")";

    /**
     *
     * @param context
     */
    public LocalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     *
     * @param db
     */
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     *
     * @param studentCoverImage
     * @param id
     * @return
     */
    private void saveStudentCoverImage(Bitmap studentCoverImage, long id)
    {
        ContentValues           contentValues;
        ByteArrayOutputStream   bos;
        byte[]                  bArray;

        bos     = new ByteArrayOutputStream();
        studentCoverImage.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bArray  = bos.toByteArray();

        contentValues = new ContentValues();

        contentValues.put(ID, id);
        contentValues.put(STUDENT_COVER_IMAGE, bArray);

        //replace
        id                  = this.getWritableDatabase().replace(TABLE_USER, null, contentValues);
        studentCoverImageId = id;
    }

    /**
     *
     * @param studentCoverImage
     * @return
     */
    private void saveStudentCoverImage(Bitmap studentCoverImage)
    {

        ContentValues           contentValues;
        ByteArrayOutputStream   bos;
        byte[]                  bArray;
        long                    id;

        bos     = new ByteArrayOutputStream();
        studentCoverImage.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bArray  = bos.toByteArray();

        contentValues = new ContentValues();

        contentValues.put(STUDENT_COVER_IMAGE, bArray);
        //insert
        id = this.getWritableDatabase().insert(TABLE_USER, null, contentValues);

        studentCoverImageId = id;
    }

    /**
     * @param image
     * @return
     */
    public long saveStudentCoverImageRecord(Bitmap image){
        if(studentCoverImageId == -1)
            saveStudentCoverImage(image);
        else
            saveStudentCoverImage(image, studentCoverImageId);

        return studentCoverImageId;
    }

    /**
     *
     * @param tutorCoverImage
     * @param id
     * @return
     */
    private void saveTutorCoverImage(Bitmap tutorCoverImage, long id)
    {

        ContentValues           contentValues;
        ByteArrayOutputStream   bos;
        byte[]                  bArray;

        bos     = new ByteArrayOutputStream();
        tutorCoverImage.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bArray  = bos.toByteArray();

        contentValues = new ContentValues();

        contentValues.put(ID, id);
        contentValues.put(TUTOR_COVER_IMAGE, bArray);
        id = this.getWritableDatabase().replace(TABLE_USER, null, contentValues); //replace

        tutorCoverImageId = id;
    }

    /**
     *
     * @param tutorCoverImage
     * @return
     */
    private void saveTutorCoverImage(Bitmap tutorCoverImage)
    {

        ContentValues           contentValues;
        ByteArrayOutputStream   bos;
        byte[]                  bArray;
        long                    id;

        bos     = new ByteArrayOutputStream();
        tutorCoverImage.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bArray  = bos.toByteArray();

        contentValues = new ContentValues();

        contentValues.put(TUTOR_COVER_IMAGE, bArray);
        id = this.getWritableDatabase().insert(TABLE_USER, null, contentValues); //insert

        tutorCoverImageId = id;
    }

    /**
     * @param image
     * @return
     */
    public long saveTutorCoverImageRecord(Bitmap image){
        if(tutorCoverImageId == -1)
            saveTutorCoverImage(image);
        else
            saveTutorCoverImage(image, studentCoverImageId);

        return studentCoverImageId;
    }

    /**
     * @param tutorProfileImage
     * @param id
     */
    public void saveTutorProfileImageRecord(Bitmap tutorProfileImage, long id)
    {

    }

    /**
     * @param studentProfileImage
     * @param id
     */
    public void saveStudentProfileImageRecord(Bitmap studentProfileImage, long id)
    {

    }
}