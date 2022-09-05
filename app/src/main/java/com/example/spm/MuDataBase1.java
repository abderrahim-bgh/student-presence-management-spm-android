package com.example.spm;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.spm.classes.attendance;
import com.example.spm.classes.group;
import com.example.spm.classes.student;
import com.example.spm.classes.studentOfSeance;
import com.example.spm.classes.year;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.List;

public class MuDataBase1 extends SQLiteOpenHelper {
    private Context context;

    private SQLiteDatabase datab;

    private static final String dataBase_name="tp_project";
    private static final int dataBase_vertion=8;
    private static final String TABLE_NAME="years";
    private static final String TABLE_NAME2="classes";
    private static final String TABLE_NAME3="groups";
    private static final String TABLE_NAME4="seance";
    private static final String ID="id_y";
    private static final String ID2="id_C";
    private static final String ID3="id_G";
    private static final String year="year";
    private static final String gp="gp";
    private static final String classe="class";
    private static final String group="grp";

    private static final String STUDENTS_TABLE_NAME = "STUDENTS_TABLE" ;
    private static final String STUDENT_ID = "STUDENT_ID" ;
    private static final String STUDENT_N = "STUDENT_NMBR" ;
    private static final String STUDENT_MAT = "STUDENT_MAT" ;
    private static final String STUDENT_FAM_NAME = "STUDENT_FAMILY_NAME" ;
    private static final String STUDENT_comment = "comment" ;
    private static final String STUDENT_NAME = "STUDENT_NAME" ;
    private static final String STUDENT_GROUP_TD_ID = "STUDENT_GROUP_TD_ID" ;
    private static final String STUDENT_GROUP_TP_ID = "STUDENT_GROUP_IP_ID" ;

    private static final String ID4="id_se";
    private static final String Date="date";
    private static final String hour_big="h_big";
    private static final String hour_end="h_end";
    private static final String SEANCE_NAME="SEANCE_NAME";
    private static final String S_comment="comment";
    private static final String SEANCE_CALLED="CALLED";

    private static final String ATTENDANCE_TABLE_NAME = "ATTENDANCE_TABLE_NAME" ;
    private static final String ATTENDANCE_ID = "ATTENDANCE_ID" ;
    private static final String ATTENDANCE_STATE = "ATTENDANCE_STATE" ;




        SQLiteDatabase database;
    public MuDataBase1(@Nullable Context context) {
        super(context, dataBase_name, null, dataBase_vertion);
        this.context= context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+TABLE_NAME+" ( "+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                year+" TEXT )";

        String query2 = "CREATE TABLE "+TABLE_NAME2+" ( "+year+" TEXT  , "+
                classe+" TEXT,"+ID2+" INTEGER PRIMARY KEY AUTOINCREMENT)";

        String quer3 = "CREATE TABLE "+TABLE_NAME3+" ( "+ID2+" TEXT  , "+
                group+" TEXT,"+ID3+" INTEGER PRIMARY KEY AUTOINCREMENT,"+gp+" TEXT )";


        String quer4 = "CREATE TABLE "+TABLE_NAME4+" ("+ID4+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +ID3+" TEXT  , "+
                Date+" TEXT,"+hour_big+" TEXT,"+hour_end+" TEXT,"
                +SEANCE_CALLED+" TEXT," // so we can know if it called or not yet
                +SEANCE_NAME +" TEXT ,"
                +S_comment+" TEXT )";


        db.execSQL("CREATE TABLE IF NOT EXISTS " + STUDENTS_TABLE_NAME +"( "
                + STUDENT_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + STUDENT_MAT +" INTEGER UNIQUE , "
                + STUDENT_FAM_NAME +" TEXT NOT NULL , "
                + STUDENT_comment +" TEXT, "
                +STUDENT_NAME +" TEXT NOT NULL , "
                +STUDENT_GROUP_TD_ID +" INTEGER , "
                + STUDENT_GROUP_TP_ID+ " INTEGER )" );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + ATTENDANCE_TABLE_NAME +"( "
                + ATTENDANCE_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + STUDENT_ID +" INTEGER , "
                + ATTENDANCE_STATE +" TEXT NOT NULL , "
                + ID4+ " INTEGER , "
                + ID3+ " INTEGER )" );




        db.execSQL(query);
        db.execSQL(query2);
        db.execSQL(quer3);
        db.execSQL(quer4);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME3);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME4);
        db.execSQL("DROP TABLE IF EXISTS "+ STUDENTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ ATTENDANCE_TABLE_NAME);

        onCreate(db);
    }


    void addYear(String y){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(year,y);
        long result= db.insert(TABLE_NAME, null, cv);
        if (result == -1){
            Toast.makeText(context, "Faild", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(context, "Added Successfuly", Toast.LENGTH_SHORT).show();
    }


    public void addClass(String yr, String c) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        if (!c.isEmpty()) {
            cv.put(year, yr);
            cv.put(classe, c);
            long result = db.insert(TABLE_NAME2, null, cv);
            if (result == -1) {
                Toast.makeText(context, "Faild", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(context, "Added Successfuly", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(context, "faild", Toast.LENGTH_SHORT).show();
    }

    public void addGrp( String td ,  String id) {


        SQLiteDatabase db = this.getWritableDatabase();
        long result=-1;
        ContentValues cv= new ContentValues();

        int i=0;
        int m=1;
        while (i<Integer.parseInt(td)){
            cv.put(ID2,id);
            cv.put(group,"group td "+m);
            cv.put(gp,"td");
            result= db.insert(TABLE_NAME3, null, cv);
            m++;
            i++;}


        if (result == -1){
            Toast.makeText(context, "err", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(context, "Added Successfuly", Toast.LENGTH_SHORT).show();
    }

    public void addGrptp( String tp, String id) {


        SQLiteDatabase db = this.getWritableDatabase();
        long result=-1;
        ContentValues cv= new ContentValues();
        int i=1;
        int j=0;

        while ( j<Integer.parseInt(tp)){
            cv.put(ID2,id);
            cv.put(group,"group tp "+i);
            cv.put(gp,"tp");
            result= db.insert(TABLE_NAME3, null, cv);
            j++;
            i++;
        }

        if (result == -1){
            Toast.makeText(context, "err", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(context, "Added Successfuly", Toast.LENGTH_SHORT).show();
    }



    public void addSeance(String idG, String t1, String t2, String dt, String name) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        if (!idG.isEmpty()) {
            cv.put(ID3, idG);
            cv.put(hour_big, t1);
            cv.put(hour_end, t2);
            cv.put(Date, dt);
            cv.put(SEANCE_NAME,name);
            cv.put(SEANCE_CALLED,"NO");
            long result = db.insert(TABLE_NAME4, null, cv);
            if (result == -1) {
                Toast.makeText(context, "Faild", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(context, "Added Successfuly", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(context, "faild", Toast.LENGTH_SHORT).show();
    }



    public Cursor readAllYears(){

        String query=" SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db= this.getWritableDatabase();

        Cursor cursor= null;

        if (db != null){
          cursor=  db.rawQuery(query, null);

        }
        return cursor;
    }


    public Cursor readAllClasses(){

        String query=" SELECT * FROM "+TABLE_NAME2;
        SQLiteDatabase db= this.getWritableDatabase();

        Cursor cursor= null;

        if (db != null){
            cursor=  db.rawQuery(query, null);

        }
        return cursor;
    }

    public Cursor readAllGroups() {
        String query=" SELECT * FROM "+TABLE_NAME3;
        SQLiteDatabase db= this.getWritableDatabase();

        Cursor cursor= null;

        if (db != null){
            cursor=  db.rawQuery(query, null);

        }
        return cursor;
    }
    public Cursor readAllATTENDANCE() {
        String query=" SELECT * FROM "+ATTENDANCE_TABLE_NAME;
        SQLiteDatabase db= this.getWritableDatabase();

        Cursor cursor= null;

        if (db != null){
            cursor=  db.rawQuery(query, null);

        }
        return cursor;

    }
    public Cursor readAllseance() {
        String query=" SELECT * FROM "+TABLE_NAME4;
        SQLiteDatabase db= this.getWritableDatabase();

        Cursor cursor= null;

        if (db != null){
            cursor=  db.rawQuery(query, null);

        }
        return cursor;

    }
    public Cursor readAllstudent() {
        String query=" SELECT * FROM "+STUDENTS_TABLE_NAME;
        SQLiteDatabase db= this.getWritableDatabase();

        Cursor cursor= null;

        if (db != null){
            cursor=  db.rawQuery(query, null);

        }
        return cursor;

    }

    public void updateData(String row_id, String cla){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(classe,cla);
        long result=  db.update(TABLE_NAME2,cv,"id_C=?",new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "faild to update", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(context, "update Successfuly", Toast.LENGTH_SHORT).show();
    }

    public void updateDatag(String row_id, String cla){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(group,cla);
        long result=  db.update(TABLE_NAME3,cv,"id_G=?",new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "faild to update", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(context, "update Successfuly", Toast.LENGTH_SHORT).show();
    }

    public void deleteSeance(String id){
        datab = this.getWritableDatabase();

        long b = datab.delete(TABLE_NAME4,ID4+"=?",new  String[]{id});
        if (b == -1){
            Toast.makeText(context, "faild to delete  ", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(context, "Delete Successfuly", Toast.LENGTH_SHORT).show();

    }
    public boolean insertStudent(student s, String type, String gId){
        datab = this.getWritableDatabase();
        long d=1;

        student a = studentByMatExist(s.getMat());
        ContentValues values = new ContentValues();
        boolean b = false;
       // db = new MuDataBase1(StudentsActivity.this);
        if (a.getId() != -1){

            if (type.equals("td") && a.getId_G_TD() != 0 ){
                Toast.makeText(context, "This student is already registered a group td", Toast.LENGTH_SHORT).show();
            }else if (type.equals("td") && a.getId_G_TD() == 0 ){
                b = updateStudent_group(a.getId(),Integer.parseInt(gId),a.getId_G_TP());
            }
            else if (type.equals("tp") && a.getId_G_TP() != 0 ){
                Toast.makeText(context, "This student is already registered a group tp", Toast.LENGTH_SHORT).show();
            }  else if (type.equals("tp") && a.getId_G_TP() == 0 ){
                // student s = new student(-1,n,mat,famname,name,a.getId_G_TD(),Integer.parseInt(gId));
                b = updateStudent_group(a.getId(),a.getId_G_TD(),Integer.parseInt(gId));

            }

        } else {

            if (type.equals("td") ){
                //   values.put(YEAR_ID,y.getId());
                //   values.put(STUDENT_N,s.getNmbr());
                values.put(STUDENT_MAT,s.getMat());
                values.put(STUDENT_FAM_NAME,s.getFamName());
                values.put(STUDENT_NAME,s.getName());
                //  values.put(STUDENT_CLASS_ID,s.getClassID());
                values.put(STUDENT_GROUP_TD_ID,Integer.parseInt(gId));
                values.put(STUDENT_GROUP_TP_ID,0);

            }  else if (type.equals("tp")){
                //   values.put(YEAR_ID,y.getId());
                //   values.put(STUDENT_N,s.getNmbr());
                values.put(STUDENT_MAT,s.getMat());
                values.put(STUDENT_FAM_NAME,s.getFamName());
                values.put(STUDENT_NAME,s.getName());
                //  values.put(STUDENT_CLASS_ID,s.getClassID());
                values.put(STUDENT_GROUP_TP_ID,Integer.parseInt(gId));
                values.put(STUDENT_GROUP_TD_ID,0);
            }

            d = datab.insert(STUDENTS_TABLE_NAME,null,values);

        }


        if (d==1){
            return false;
        } else {
            return true;
        }
    }


    @SuppressLint("Range")
    public List<student> getstudent(int idGroup, String type){

        List<student> gList = new ArrayList<>();
        datab = this.getReadableDatabase();
        Cursor cursor = null;
        if (datab != null && type.equals("td")){
            //   cursor=  datab.rawQuery(queryS, null);
            cursor = datab.query(STUDENTS_TABLE_NAME,
                    null,
                    STUDENT_GROUP_TD_ID + " = ? " ,
                    new String[]{String.valueOf(idGroup)},
                    null,
                    null,
                    STUDENT_FAM_NAME
            );

        }  else if (datab != null && type.equals("tp")){
            //   cursor=  datab.rawQuery(queryS, null);
            cursor = datab.query(STUDENTS_TABLE_NAME,
                    null,
                    STUDENT_GROUP_TP_ID + " = ?" ,
                    new String[]{String.valueOf(idGroup)},
                    null,
                    null,
                    STUDENT_FAM_NAME
            );

        }


        if (cursor.moveToFirst()) {
            do {
                String name = null, fam=null;
                int id , g_td_id, g_tp_id,mat , nmbr;
                id = cursor.getInt(cursor.getColumnIndex(STUDENT_ID));
              //  nmbr = cursor.getInt(cursor.getColumnIndex(STUDENT_N));
                name = cursor.getString(cursor.getColumnIndex(STUDENT_NAME));
                fam = cursor.getString(cursor.getColumnIndex(STUDENT_FAM_NAME));
                mat = cursor.getInt(cursor.getColumnIndex(STUDENT_MAT));
                // cId = cursor.getInt(cursor.getColumnIndex(STUDENT_CLASS_ID));
                 g_td_id = cursor.getInt(cursor.getColumnIndex(STUDENT_GROUP_TD_ID));

                 g_tp_id = cursor.getInt(cursor.getColumnIndex(STUDENT_GROUP_TP_ID));



                student gr = new student(id,mat,fam,name,g_td_id,g_tp_id);
                gList.add(gr);
            }while (cursor.moveToNext());
        }


        cursor.close();

        return gList;

    }

    @SuppressLint("Range")
    public List<studentOfSeance> getstudentOfseance(int idGroup, String type){

        List<studentOfSeance> gList = new ArrayList<>();
        datab = this.getReadableDatabase();
        Cursor cursor = null;
        if (datab != null && type.equals("td")){
            //   cursor=  datab.rawQuery(queryS, null);
            cursor = datab.query(STUDENTS_TABLE_NAME,
                    null,
                    STUDENT_GROUP_TD_ID + " = ? " ,
                    new String[]{String.valueOf(idGroup)},
                    null,
                    null,
                    STUDENT_FAM_NAME
            );

        }  else if (datab != null && type.equals("tp")){
            //   cursor=  datab.rawQuery(queryS, null);
            cursor = datab.query(STUDENTS_TABLE_NAME,
                    null,
                    STUDENT_GROUP_TP_ID + " = ?" ,
                    new String[]{String.valueOf(idGroup)},
                    null,
                    null,
                    STUDENT_FAM_NAME
            );

        }


        if (cursor.moveToFirst()) {
            do {
                String name = null, fam=null , state;
                int id , g_td_id, g_tp_id,mat , nmbr;
                id = cursor.getInt(cursor.getColumnIndex(STUDENT_ID));
                //  nmbr = cursor.getInt(cursor.getColumnIndex(STUDENT_N));
                name = cursor.getString(cursor.getColumnIndex(STUDENT_NAME));
                fam = cursor.getString(cursor.getColumnIndex(STUDENT_FAM_NAME));
                mat = cursor.getInt(cursor.getColumnIndex(STUDENT_MAT));
                // cId = cursor.getInt(cursor.getColumnIndex(STUDENT_CLASS_ID));
                g_td_id = cursor.getInt(cursor.getColumnIndex(STUDENT_GROUP_TD_ID));

                g_tp_id = cursor.getInt(cursor.getColumnIndex(STUDENT_GROUP_TP_ID));


                studentOfSeance gr = new studentOfSeance(id,mat,fam,name,g_td_id,g_tp_id,"NOT YET");
                gList.add(gr);
                // Toast.makeText(c,"class added ->"+cla.getName()+" / "+cla.getYear(),Toast.LENGTH_SHORT).show();
            }while (cursor.moveToNext());
        }


        cursor.close();

        return gList;

    }


    @SuppressLint("Range")
    public student studentByMatExist(int matrecul){

        student a = new student();
        datab = this.getReadableDatabase();
        Cursor cursor = null;
        if (datab != null){
            //   cursor=  datab.rawQuery(queryS, null);
            cursor = datab.query(STUDENTS_TABLE_NAME,
                    null,
                    STUDENT_MAT + " = ?" ,
                    new String[]{String.valueOf(matrecul)},
                    null,
                    null,
                    STUDENT_ID
            );

        }

        if (cursor.moveToFirst()) {
            a.setId(cursor.getInt(cursor.getColumnIndex(STUDENT_ID)));
        //    a.setNmbr(cursor.getInt(cursor.getColumnIndex(STUDENT_N)));
            a.setMat( cursor.getInt(cursor.getColumnIndex(STUDENT_MAT)));
            a.setFamName( cursor.getString(cursor.getColumnIndex(STUDENT_FAM_NAME)));
            a.setName(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
            a.setId_G_TD(cursor.getInt(cursor.getColumnIndex(STUDENT_GROUP_TD_ID)));
            a.setId_G_TP(cursor.getInt(cursor.getColumnIndex(STUDENT_GROUP_TP_ID)));
        } else {
            a.setId(-1);
        }

        cursor.close();

        return a;

    }

    public boolean updateStudent_group(int id,int groupTd, int groupTp){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(STUDENT_GROUP_TD_ID,groupTd);
        cv.put(STUDENT_GROUP_TP_ID,groupTp);

        long result=  db.update(STUDENTS_TABLE_NAME,cv,""+STUDENT_ID+" = ?",new String[]{String.valueOf(id)});
        if (result == -1){
            return false;
        } else {
        return true;}
    }

    // add one group tp cause u r using the nmbr of td or tp groups to be created

    public boolean insertGroup(group g){
        datab = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //   values.put(YEAR_ID,y.getId());
        values.put(group,g.getName());
        values.put(gp,g.getType());
        values.put(ID2, String.valueOf(g.getIdC()));
        long b = datab.insert(TABLE_NAME3,null,values);

        if (b==1){
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteGroups(String id, String type){
        datab = this.getWritableDatabase();
        deleteStudentsOfGroup(Integer.parseInt(id),type);
        int b = datab.delete(TABLE_NAME3,ID3+"=?",new  String[]{id});

        if(b==1){return  true;}
        else {return false;}

    }

    public boolean deleteGroupsofclass(String id){
        datab = this.getWritableDatabase();
        int b = datab.delete(TABLE_NAME3,ID2+"=?",new  String[]{id});

        if(b==1){return  true;}
        else {return false;}

    }


    public boolean deleteClass(String id){
        datab = this.getWritableDatabase();
        int b = datab.delete(TABLE_NAME2,ID2+"= ?",new String[]{id});

        if(b==0){ return true;}
        else { return false;}
    }



    @SuppressLint("Range")
    public List<group> getgroups(String idClass){

        List<group> gList = new ArrayList<>();
        datab = this.getReadableDatabase();
        Cursor cursor = null;
        if (datab != null){
            //   cursor=  datab.rawQuery(queryS, null);
            cursor = datab.query(TABLE_NAME3,
                    null,
                    ID2+" = ? ",
                    new String[]{ idClass},
                    null,
                    null,
                    null
            );

        }
        if (cursor.moveToFirst()) {
            do {
                String y = null;
                String typ = null ;
                int id ,cId ;
                id = cursor.getInt(cursor.getColumnIndex(ID3));
                y = cursor.getString(cursor.getColumnIndex(group));
                cId = cursor.getInt(cursor.getColumnIndex(ID2));
                typ = cursor.getString(cursor.getColumnIndex(gp));
                group gr = new group(String.valueOf(id),y,typ,cId);
                gList.add(gr);
                // Toast.makeText(c,"class added ->"+cla.getName()+" / "+cla.getYear(),Toast.LENGTH_SHORT).show();
            }while (cursor.moveToNext());
        }


        cursor.close();

        return gList;
    }

    public boolean deleteStudentsOfGroup(int gId, String type){
        datab = this.getWritableDatabase();
        boolean b = false;
        List<student> sList = getstudent(gId,type);
        for (int i =0; i<sList.size();i++){
            student s = sList.get(i);
            if (type.equals("td") && s.getId_G_TP()==0){
              b =  deleteStudent(String.valueOf(s.getId()));
            } else if (type.equals("tp") && s.getId_G_TD()==0){
                b =  deleteStudent(String.valueOf(s.getId()));
            } else if (type.equals("td") && s.getId_G_TP()!=0 ) {
                updateStudent_group(s.getId(),0,s.getId_G_TP());
                Toast.makeText(context, "student exist in a tp group", Toast.LENGTH_SHORT).show();
            } else if (type.equals("tp") && s.getId_G_TD()!=0 ) {
                updateStudent_group(s.getId(),s.getId_G_TD(),0);
                Toast.makeText(context, "student exist in a td group", Toast.LENGTH_SHORT).show();
            }

        }

        if(b){return  true;}
        else {return false;}

    }

    public boolean deleteStudent(String id){
        datab = this.getWritableDatabase();
        int b = datab.delete(STUDENTS_TABLE_NAME,STUDENT_ID+"=?",new  String[]{id});

        if(b==1){return  true;}
        else {return false;}

    }

    @SuppressLint("Range")
    public List<group> getGroupsByType(int idClass, String type){

        List<group> gList = new ArrayList<>();
        datab = this.getReadableDatabase();
        Cursor cursor = null;
        if (datab != null && type.equals("td")){
            //   cursor=  datab.rawQuery(queryS, null);
            cursor = datab.query(TABLE_NAME3,
                    null,
                    ID2 + " = ? AND "+gp + " = ? ",
                    new String[]{String.valueOf(idClass),"td"},
                    null,
                    null,
                    group
            );

        }  else if (datab != null && type.equals("tp")){
            //   cursor=  datab.rawQuery(queryS, null);
            cursor = datab.query(TABLE_NAME3,
                    null,
                    ID2 + " = ? AND "+gp + " = ? ",
                    new String[]{String.valueOf(idClass),"tp"},
                    null,
                    null,
                    group
            );

        }


        if (cursor.moveToFirst()) {
            do {
                String name = null, classe=null, t = null ;
                int id ;
                id = cursor.getInt(cursor.getColumnIndex(ID3));
                //  nmbr = cursor.getInt(cursor.getColumnIndex(STUDENT_N));
                name = cursor.getString(cursor.getColumnIndex(group));
                classe = cursor.getString(cursor.getColumnIndex(ID2));
                t = cursor.getString(cursor.getColumnIndex(gp));



                group gr = new group(String.valueOf(id),name,t,Integer.parseInt(classe));
                gList.add(gr);
                // Toast.makeText(c,"class added ->"+cla.getName()+" / "+cla.getYear(),Toast.LENGTH_SHORT).show();
            }while (cursor.moveToNext());
        }


        cursor.close();

        return gList;

    }


    public boolean startAppele(attendance a){
        datab = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //   values.put(YEAR_ID,y.getId());
        values.put(STUDENT_ID,a.getStudentId());
        values.put(ID4,a.getSeanceId());
        values.put(ATTENDANCE_STATE,a.getState());
        values.put(ID3,a.getIdGroup());
        long b = datab.insert(ATTENDANCE_TABLE_NAME,null,values);

        if (b==1){
            return false;
        } else {
            return true;
        }
    }

    public boolean updateSeanceCall(int id, boolean called){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        if (called) {
            cv.put(SEANCE_CALLED, "CALLED");
        } else {
            cv.put(SEANCE_CALLED,"NO");
        }
        long result=  db.update(TABLE_NAME4,cv,""+ID4+" = ?",new String[]{String.valueOf(id)});
        if (result == -1){
            return false;
        } else {
            return true;}
    }




    @SuppressLint("Range")
    public attendance getStudentAttendance(String idseance, int studentId){

        List<studentOfSeance> gList = new ArrayList<>();
        attendance gr = new attendance();

        datab = this.getReadableDatabase();
        Cursor cursor = null;
        if (datab != null){
            //   cursor=  datab.rawQuery(queryS, null);
            cursor = datab.query(ATTENDANCE_TABLE_NAME,
                    null,
                    STUDENT_ID + " = ? AND "+ID4 + " = ? ",
                    new String[]{String.valueOf(studentId),idseance},
                    null,
                    null,
                    null
            );

        }


        if (cursor.moveToFirst()) {

                int id , idSeance, idStudent;
                id = cursor.getInt(cursor.getColumnIndex(STUDENT_ID));
                idSeance = cursor.getInt(cursor.getColumnIndex(ID4));
                idStudent = cursor.getInt(cursor.getColumnIndex(STUDENT_ID));

                String state = cursor.getString(cursor.getColumnIndex(ATTENDANCE_STATE));


                gr.setId(id);
                gr.setState(state);
                gr.setSeanceId(idSeance);
                gr.setStudentId(idStudent);
              //  gList.add(gr);
                // Toast.makeText(c,"class added ->"+cla.getName()+" / "+cla.getYear(),Toast.LENGTH_SHORT).show();
        }


        cursor.close();

        return gr;

    }


    @SuppressLint("Range")
    public List<attendance> calculAbsence(String idGroup, int studentId,String stat){

        List<attendance> absenceList = new ArrayList<>();

        datab = this.getReadableDatabase();
        Cursor cursor = null;
        if (datab != null){
            //   cursor=  datab.rawQuery(queryS, null);
            cursor = datab.query(ATTENDANCE_TABLE_NAME,
                    null,
                    STUDENT_ID + " = ? AND "+ID3 + " = ? AND "+ATTENDANCE_STATE+" = ? " ,
                    new String[]{String.valueOf(studentId), idGroup,stat},
                    null,
                    null,
                    null
            );

        }

        if (cursor.moveToFirst()) {
            do {
                String state = null ;
                int id ,idSeance,idStudent,idG ;
                id = cursor.getInt(cursor.getColumnIndex(ATTENDANCE_ID));
                //  nmbr = cursor.getInt(cursor.getColumnIndex(STUDENT_N));
                state= cursor.getString(cursor.getColumnIndex(ATTENDANCE_STATE));
                idSeance = cursor.getInt(cursor.getColumnIndex(ID4));
                idStudent = cursor.getInt(cursor.getColumnIndex(STUDENT_ID));
                idG = cursor.getInt(cursor.getColumnIndex(ID3));

                    attendance at = new attendance(id, idStudent, idSeance, state, idG);
                    absenceList.add(at);
                // Toast.makeText(c,"class added ->"+cla.getName()+" / "+cla.getYear(),Toast.LENGTH_SHORT).show();
            }while (cursor.moveToNext());
        }




        cursor.close();

        return absenceList;

    }
    @SuppressLint("Range")
    public List<attendance> calculAbsence4seance(String idGroup, int studentId, String stat){

        List<attendance> absenceList = new ArrayList<>();

        datab = this.getReadableDatabase();
        Cursor cursor = null;
        if (datab != null){
            //   cursor=  datab.rawQuery(queryS, null);
            cursor = datab.query(ATTENDANCE_TABLE_NAME,
                    null,
                    STUDENT_ID + " = ? AND "+ID3 + " = ? AND "+ATTENDANCE_STATE+" = ? " ,
                    new String[]{String.valueOf(studentId), idGroup,stat},
                    null,
                    null,
                    null
            );

        }


                if (cursor.moveToPosition(0)) {
                    String state = null;
                    int id, idSeance, idStudent, idG;
                    id = cursor.getInt(cursor.getColumnIndex(ATTENDANCE_ID));
                    //  nmbr = cursor.getInt(cursor.getColumnIndex(STUDENT_N));
                    state = cursor.getString(cursor.getColumnIndex(ATTENDANCE_STATE));
                    idSeance = cursor.getInt(cursor.getColumnIndex(ID4));
                    idStudent = cursor.getInt(cursor.getColumnIndex(STUDENT_ID));
                    idG = cursor.getInt(cursor.getColumnIndex(ID3));

                    attendance at = new attendance(id, idStudent, idSeance, state, idG);
                    absenceList.add(at);
                }
            if (cursor.moveToPosition(1)) {
                String state = null;
                int id, idSeance, idStudent, idG;
                id = cursor.getInt(cursor.getColumnIndex(ATTENDANCE_ID));
                //  nmbr = cursor.getInt(cursor.getColumnIndex(STUDENT_N));
                state = cursor.getString(cursor.getColumnIndex(ATTENDANCE_STATE));
                idSeance = cursor.getInt(cursor.getColumnIndex(ID4));
                idStudent = cursor.getInt(cursor.getColumnIndex(STUDENT_ID));
                idG = cursor.getInt(cursor.getColumnIndex(ID3));

                attendance at = new attendance(id, idStudent, idSeance, state, idG);
                absenceList.add(at);
            }
                // Toast.makeText(c,"class added ->"+cla.getName()+" / "+cla.getYear(),Toast.LENGTH_SHORT).show();





        cursor.close();

        return absenceList;

    }

    // hadhi kima kant mdyora
    /**
    public void updatestate(String row_id, String atd){
        datab =this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(ATTENDANCE_STATE,atd);
        long result=  datab.update(ATTENDANCE_TABLE_NAME,cv,"ATTENDANCE_ID =?",new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "faild to update", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(context, "update Successfuly", Toast.LENGTH_SHORT).show();
    }

     */

    // this is fixed
    public void updatestate(String row_id, String atd,String seanceId){
        datab =this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(ATTENDANCE_STATE,atd);
        long result=  datab.update(ATTENDANCE_TABLE_NAME,cv," STUDENT_ID = ? AND "+ID4+" = ?",new String[]{row_id, seanceId});
        if (result == -1){
            Toast.makeText(context, "faild to update", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(context, "update Successfuly", Toast.LENGTH_SHORT).show();
    }

    public void comment_student(int id,String comment){
        datab =this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(STUDENT_comment,comment);
        long result=  datab.update(STUDENTS_TABLE_NAME,cv," STUDENT_ID = ? ",new String[]{String.valueOf(id)});
        if (result == -1){
            Toast.makeText(context, "faild to update", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(context, "coment Successfuly", Toast.LENGTH_SHORT).show();
    }

    public void comment_seance(int id,String comment){
        datab =this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(S_comment,comment);
        long result=  datab.update(TABLE_NAME4,cv," id_se = ? ",new String[]{String.valueOf(id)});
        if (result == -1){
            Toast.makeText(context, "faild to coment", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(context, "coment Successfuly", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("Range")
    public String getClassByGroupId(String groupId){
        String classeId;
        String classeName = "";

        datab = this.getReadableDatabase();
        Cursor cursor = null;
        if (datab != null){
            //   cursor=  datab.rawQuery(queryS, null);
            cursor = datab.query(TABLE_NAME3,
                    null,
                    ID3 + " = ?" ,
                    new String[]{groupId},
                    null,
                    null,
                    null
            );

        }

        if (cursor.moveToFirst()) {

            classeId = cursor.getString(cursor.getColumnIndex(ID2));

            Cursor cursor2 = null;
            if (datab != null){
                //   cursor=  datab.rawQuery(queryS, null);
                cursor2 = datab.query(TABLE_NAME2,
                        null,
                        ID2 + " = ?" ,
                        new String[]{String.valueOf(classeId)},
                        null,
                        null,
                        null
                );

                if (cursor2.moveToFirst()) {

                    classeName = cursor2.getString(cursor2.getColumnIndex(classe));
                }
                cursor2.close();


            }

        }
        cursor.close();

        return classeName;

    }

    @SuppressLint("Range")
    public group getGroupById(String groupId){

        group g = new group();

        datab = this.getReadableDatabase();
        Cursor cursor = null;
        if (datab != null){
            //   cursor=  datab.rawQuery(queryS, null);
            cursor = datab.query(TABLE_NAME3,
                    null,
                    ID3 + " = ?" ,
                    new String[]{groupId},
                    null,
                    null,
                    null
            );

        }

        if (cursor.moveToFirst()) {

            g.setIdC(cursor.getInt(cursor.getColumnIndex(ID2)));
            g.setId(cursor.getString(cursor.getColumnIndex(ID3)));
            g.setType(cursor.getString(cursor.getColumnIndex(gp)));
            g.setName(cursor.getString(cursor.getColumnIndex(group)));

        }
        cursor.close();

        return g;

    }

    public boolean updateStudentInfo(int id, int mat, String name, String familyName){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues cv= new ContentValues();

        cv.put(STUDENT_MAT,mat);
        cv.put(STUDENT_NAME,name);
        cv.put(STUDENT_FAM_NAME,familyName);

        long result=  db.update(STUDENTS_TABLE_NAME,cv,""+STUDENT_ID+" = ?",new String[]{String.valueOf(id)});
        if (result == -1){
            return false;
        } else {
            return true;}
    }

    public boolean deleteStudentAttendence(String id, String groupId){
        datab = this.getWritableDatabase();
        int b = datab.delete(ATTENDANCE_TABLE_NAME,STUDENT_ID+" = ? AND "+ID3 +" = ? ",new  String[]{id, groupId});

        if(b==1){return  true;}
        else {return false;}

    }









}
