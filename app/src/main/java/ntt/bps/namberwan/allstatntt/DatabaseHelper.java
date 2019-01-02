package ntt.bps.namberwan.allstatntt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zipo on 20/04/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bpsdb";
    private static final String TABLE_BERITA = "berita";
    private static final String TABLE_BRS = "brs";
    private static final String TABLE_TABEL = "tabel";
    private static final String TABLE_PUBLIKASI = "publikasi";
    private static final String TIME_STAMP = "time";

    private static final String BERITA_ID = "idberita";
    private static final String BERITA_JENIS = "jenis";
    private static final String BERITA_TANGGAL = "tanggal";
    private static final String BERITA_FOTO = "foto";
    private static final String BERITA_JUDUL = "judul";
    private static final String BERITA_RINCIAN = "rincian";
    private static final String BERITA_URL_SHARE = "urlshare";

    private static final String BRS_ID = "idbrs";
    private static final String BRS_JUDUL = "judul";
    private static final String BRS_ABSTRAK = "abstrak";
    private static final String BRS_TANGGAL = "tanggal";
    private static final String BRS_URL_PDF = "urlpdf";
    private static final String BRS_SUBJEK = "subjek";
    private static final String BRS_URL_SHARE = "urlshare";
    private static final String BRS_KATEGORI = "kategori";

    private static final String PUBLIKASI_ID = "idpublikasi";
    private static final String PUBLIKASI_JUDUL = "judul";
    private static final String PUBLIKASI_TANGGAL = "tanggal";
    private static final String PUBLIKASI_ISBN = "isbn";
    private static final String PUBLIKASI_ABSTRAK = "abstrak";
    private static final String PUBLIKASI_NOMOR_KATALOG = "nokatalog";
    private static final String PUBLIKASI_COVER = "cover";
    private static final String PUBLIKASI_URL_PDF = "urlpdf";

    private static final String TABEL_ID = "idtabel";
    private static final String TABEL_SUBJEK = "subjek";
    private static final String TABEL_TANGGAL = "tanggal";
    private static final String TABEL_JUDUL = "judul";
    private static final String TABEL_URL_EXCEL = "excel";
    private static final String TABEL_URL_SHARE = "urlshare";
    private static final String TABEL_HTML = "html";
    private static final String TABEL_KATEGORI = "kategori";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_BERITA + " (" + BERITA_ID + " int4 NOT NULL PRIMARY KEY, " + BERITA_JUDUL + " varchar(300), " +
                BERITA_JENIS + " varchar(30), " + BERITA_TANGGAL + " date, " + BERITA_RINCIAN + " text," +
                BERITA_FOTO + " text," + BERITA_URL_SHARE + " text," + TIME_STAMP + " integer" + ");");

        db.execSQL("CREATE TABLE " + TABLE_BRS + " (" + BRS_ID + " int4 NOT NULL PRIMARY KEY, " + BRS_JUDUL + " varchar(300), " +
                BRS_SUBJEK + " varchar(50), " + BRS_TANGGAL + " date, " + BRS_ABSTRAK + " text," +
                BRS_URL_PDF + " text," + BRS_URL_SHARE + " text," + BRS_KATEGORI + " int4," + TIME_STAMP + " integer" + ");");

        db.execSQL("CREATE TABLE " + TABLE_PUBLIKASI + " (" + PUBLIKASI_ID + " int4 NOT NULL PRIMARY KEY, " + PUBLIKASI_JUDUL + " varchar(300), " +
                PUBLIKASI_ISBN + " varchar(50), " + PUBLIKASI_NOMOR_KATALOG + " varchar(50), " + PUBLIKASI_TANGGAL + " date, " +
                PUBLIKASI_ABSTRAK + " text," + PUBLIKASI_URL_PDF + " text," + PUBLIKASI_COVER + " text," + TIME_STAMP + " integer" + ");");

        db.execSQL("CREATE TABLE " + TABLE_TABEL + " (" + TABEL_ID + " int4 NOT NULL PRIMARY KEY, " + TABEL_JUDUL + " varchar(300), " +
                TABEL_SUBJEK + " varchar(50), " + TABEL_TANGGAL + " date, " + TABEL_HTML + " text," +
                TABEL_URL_EXCEL + " text," + TABEL_URL_SHARE + " text," + TABEL_KATEGORI + " int4," + TIME_STAMP + " integer" + ");");
    }

    public void bookmarkBerita(String id, String jenis, String tanggal, String urlFoto, String judul, String rincian,
                               String urlShare, long time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BERITA_ID, id);
        contentValues.put(BERITA_JENIS, jenis);
        contentValues.put(BERITA_TANGGAL, tanggal);
        contentValues.put(BERITA_FOTO, urlFoto);
        contentValues.put(BERITA_JUDUL, judul);
        contentValues.put(BERITA_RINCIAN, rincian);
        contentValues.put(BERITA_URL_SHARE, urlShare);
        contentValues.put(TIME_STAMP, time);
        db.insert(TABLE_BERITA, null, contentValues);
    }

    public void bookmarkBrs(String id, String judul, String abstrak, String tanggalRilis, String urlPdf, String subjek,
                            String urlShare, int kategori, long time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BRS_ID, id);
        contentValues.put(BRS_SUBJEK, subjek);
        contentValues.put(BRS_TANGGAL, tanggalRilis);
        contentValues.put(BRS_JUDUL, judul);
        contentValues.put(BRS_ABSTRAK, abstrak);
        contentValues.put(BRS_URL_SHARE, urlShare);
        contentValues.put(BRS_URL_PDF, urlPdf);
        contentValues.put(BRS_KATEGORI, kategori);
        contentValues.put(TIME_STAMP, time);
        db.insert(TABLE_BRS, null, contentValues);
    }

    public void bookmarkPublikasi(String id, String judul, String tanggal, String isbn, String abstrak,
                                  String nomerKatalog, String urlCover, String urlPdf, long time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PUBLIKASI_ID, id);
        contentValues.put(PUBLIKASI_ISBN, isbn);
        contentValues.put(PUBLIKASI_NOMOR_KATALOG, nomerKatalog);
        contentValues.put(PUBLIKASI_TANGGAL, tanggal);
        contentValues.put(PUBLIKASI_JUDUL, judul);
        contentValues.put(PUBLIKASI_ABSTRAK, abstrak);
        contentValues.put(PUBLIKASI_COVER, urlCover);
        contentValues.put(PUBLIKASI_URL_PDF, urlPdf);
        contentValues.put(TIME_STAMP, time);
        db.insert(TABLE_PUBLIKASI, null, contentValues);
    }

    public void bookmarkTabel(int id, String subjek, String tanggal, String judul, String excel, String urlShare,
                              String html, int kategori, long time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABEL_ID, id);
        contentValues.put(TABEL_SUBJEK, subjek);
        contentValues.put(TABEL_TANGGAL, tanggal);
        contentValues.put(TABEL_JUDUL, judul);
        contentValues.put(TABEL_HTML, html);
        contentValues.put(TABEL_URL_SHARE, urlShare);
        contentValues.put(TABEL_URL_EXCEL, excel);
        contentValues.put(TABEL_KATEGORI, kategori);
        contentValues.put(TIME_STAMP, time);
        db.insert(TABLE_TABEL, null, contentValues);
    }

    public void unbookmarkBerita(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BERITA, BERITA_ID + "=?", new String[]{id + ""});
    }

    public void unbookmarkBrs(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BRS, BRS_ID + "=?", new String[]{id+""});
    }

    public void unbookmarkPublikasi(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PUBLIKASI, PUBLIKASI_ID + "=?", new String[]{id+""});
    }

    public void unbookmarkTabel(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TABEL, TABEL_ID + "=?", new String[]{id+""});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean isBeritaBookmarked(String id){
        String sql = "SELECT * FROM " + TABLE_BERITA + " WHERE " + BERITA_ID + " = " + "'" + id + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        boolean isBookmarked = false;
        if (cursor!=null){
            while (cursor.moveToNext()){
                isBookmarked = true;
            }
            cursor.close();
        }
        return isBookmarked;
    }

    public boolean isBrsBookmarked(String id){
        String sql = "SELECT * FROM " + TABLE_BRS + " WHERE " + BRS_ID + " = " + "'" + id + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        boolean isBookmarked = false;
        if (cursor!=null){
            while (cursor.moveToNext()){
                isBookmarked = true;
            }
            cursor.close();
        }
        return isBookmarked;
    }

    public boolean isPublikasiBookmarked(String id){
        String sql = "SELECT * FROM " + TABLE_PUBLIKASI + " WHERE " + PUBLIKASI_ID + " = " + "'" + id + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        boolean isBookmarked = false;
        if (cursor!=null){
            while (cursor.moveToNext()){
                isBookmarked = true;
            }
            cursor.close();
        }
        return isBookmarked;
    }

    public boolean isTabelBookmarked(int id){
        String sql = "SELECT * FROM " + TABLE_TABEL + " WHERE " + TABEL_ID + " = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        boolean isBookmarked = false;
        if (cursor!=null){
            while (cursor.moveToNext()){
                isBookmarked = true;
            }
            cursor.close();
        }
        return isBookmarked;
    }

    public JSONArray getBookmarkBeritaJSONArray(){
        String sql = "SELECT * FROM " + TABLE_BERITA + " ORDER BY " + TIME_STAMP + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        JSONArray jsonArray = new JSONArray();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor!=null){
            while (cursor.moveToNext()){
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id",cursor.getInt(0));
                    jsonObject.put("judul",cursor.getString(1));
                    jsonObject.put("jenis",cursor.getString(2));
                    jsonObject.put("tanggal",cursor.getString(3));
                    jsonObject.put("rincian",cursor.getString(4));
                    jsonObject.put("foto",cursor.getString(5));
                    jsonObject.put("share",cursor.getString(6));
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        }
        return jsonArray;
    }


    public JSONArray getBookmarkBrsJSONArray(){
        String sql = "SELECT * FROM " + TABLE_BRS+ " ORDER BY " + TIME_STAMP + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        JSONArray jsonArray = new JSONArray();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor!=null){
            while (cursor.moveToNext()){
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id",cursor.getInt(0));
                    jsonObject.put("judul",cursor.getString(1));
                    jsonObject.put("subjek",cursor.getString(2));
                    jsonObject.put("tgl_rilis",cursor.getString(3));
                    jsonObject.put("abstrak",cursor.getString(4));
                    jsonObject.put("pdf",cursor.getString(5));
                    jsonObject.put("url_share",cursor.getString(6));
                    jsonObject.put("kategori",cursor.getInt(7));
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        }
        return jsonArray;
    }

    public JSONArray getBookmarkPublikasiJSONArray(){
        String sql = "SELECT * FROM " + TABLE_PUBLIKASI + " ORDER BY " + TIME_STAMP + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        JSONArray jsonArray = new JSONArray();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor!=null){
            while (cursor.moveToNext()){
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id",cursor.getInt(0));
                    jsonObject.put("judul",cursor.getString(1));
                    jsonObject.put("isbn",cursor.getString(2));
                    jsonObject.put("nomer katalog",cursor.getString(3));
                    jsonObject.put("tgl_rilis",cursor.getString(4));
                    jsonObject.put("abstrak",cursor.getString(5));
                    jsonObject.put("pdf",cursor.getInt(6));
                    jsonObject.put("file_cover",cursor.getString(7));
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        }
        return jsonArray;
    }


    public JSONArray getBookmarkTabelJSONArray(){
        String sql = "SELECT * FROM " + TABLE_TABEL + " ORDER BY " + TIME_STAMP + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        JSONArray jsonArray = new JSONArray();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor!=null){
            while (cursor.moveToNext()){
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id",cursor.getInt(0));
                    jsonObject.put("judul",cursor.getString(1));
                    jsonObject.put("subjek",cursor.getString(2));
                    jsonObject.put("tgl_update",cursor.getString(3));
                    jsonObject.put("html",cursor.getInt(4));
                    jsonObject.put("excel",cursor.getString(5));
                    jsonObject.put("url_share",cursor.getString(6));
                    jsonObject.put("kategori",cursor.getInt(7));
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        }
        return jsonArray;
    }
}
