package com.example.summerproject2024.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String dbName = "Android.db";
    public static int version = 52;

    public DatabaseHelper(@Nullable Context context) {

        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "onCreate 호출");

        //CreateTable
        db.execSQL(createTableBusinessZone());
        db.execSQL(createTableBusinessHours());
        db.execSQL(createTableBuilding());
        db.execSQL(createTableAmenity());
        db.execSQL(createTableCoordinate());
        db.execSQL(createTableCallNumbers());
        db.execSQL(createTableProfessorCallNumbers());
        db.execSQL(createTableMascot());

        //InsertTable
        db.execSQL(insertBusinessZone());
        db.execSQL(insertBusinessHours());
        db.execSQL(insertBuilding());
        db.execSQL(insertAmenity());
        db.execSQL(insertCoordinate());
        db.execSQL(insertCallNumbers());
        db.execSQL(insertProfessor());
        db.execSQL(insertMascot());
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(deleteTable());
        onCreate(db);
    }

    public ArrayList<String>[] cursorToArrayList(Cursor c){
        ArrayList<String>[] answer;
        int row = c.getCount();
        int columncnt = c.getColumnCount();

        answer = new ArrayList[row];
        for(int i = 0; i < answer.length; i++) {
            answer[i] = new ArrayList<>();
        }

        int rowind = 0;
        while(c.moveToNext()){
            for(int ind = 0; ind < columncnt; ind++){
                String val = c.getString(ind);
                answer[rowind].add(val);
            }
            rowind++;
        }
        return answer;
    }

    public ArrayList<String>[] selectTable(String sql) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        return cursorToArrayList(cursor);
    }


    //Select Building Code using coordinate
    public String selectBuildingCode(int x, int y) {

        String sql = "SELECT building_code FROM Coordinate " +
                "WHERE x1 <= " + x + " and x2 >= " + x + " and y1 <= " + y + " and y2 >= " + y +";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        else{
            return "None";
        }
    }

    //Select Building Name using buildingCode
    public String selectBuildingInfo(String building_code){
        String sql = "SELECT building_name FROM Building " +
                "WHERE building_code = '" + building_code + "';";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        return cursor.getString(0);
    }

    public ArrayList<String> selectMascot(String mascot_name){
        ArrayList<String> mascotList = new ArrayList<String>();

        String sql = "SELECT * FROM Mascot WHERE name = '"+ mascot_name + "';";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()){
            for(int i = 0; i < cursor.getColumnCount(); i++){
                mascotList.add(cursor.getString(i).replaceAll("<comma>", ","));
            }
        }

        return mascotList;
    }

    public ArrayList<String> selectCategoryUsingAmenity(String building_code){
        ArrayList<String> categoryList = new ArrayList<String>();

        String sql = "SELECT category FROM Amenity WHERE building_code = '"+ building_code + "' GROUP BY category;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()){
            categoryList.add(cursor.getString(0));
        }

        return categoryList;
    }

    public ArrayList<String>[] selectBusinessZone(){
        String sql = "SELECT name, category FROM BusinessZone ORDER BY name;";
        return selectTable(sql);
    }


    public ArrayList<String> selectTownInfo(String name) {
        ArrayList<String> list = new ArrayList<String>();
        String sql = "SELECT location, number, link FROM BusinessZone WHERE name = '" + name + "'; ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()){
            for(int i = 0; i < cursor.getColumnCount(); i++){
                list.add(cursor.getString(i));
            }
        }

        return list;
    }

    public String selectBusinessHours(String name, String day){
        String hours = "정보 없음";

        String sql = "SELECT hours " +
                "FROM BusinessHours " +
                "WHERE name = '" + name + "' AND day = '" + day + "';";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()){
            hours = day + " " + cursor.getString(0);
        }

        return hours;
    }


    public HashMap<String, String> selectHours(String townName) {
        String sql = "SELECT * FROM BusinessHours WHERE name = '" + townName + "';";

        HashMap<String, String> hoursMap = new HashMap<String, String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()){
            hoursMap.put(cursor.getString(1), cursor.getString(2));            ;
        }

        return hoursMap;
    }

    public ArrayList<String>[] selectProfessorAll(){
        String sql =
                "SELECT affiliation, name, CallNumber, officeNumber FROM ProfessorCallNumbers group by name, officeNumber order by affiliation;";
        return selectTable(sql);
    }

    public ArrayList<String>[] selectCallNumbersAll(){
        String sql = "SELECT affiliation, sub_affiliation, name, CallNumber, office_number FROM CallNumbers group by sub_affiliation, affiliation, CallNumber, name, office_number order by affiliation;";
        return selectTable(sql);
    }

    public String deleteTable(){
        String sql = "DROP TABLE IF EXISTS BusinessZone;\n" +
                "DROP TABLE IF EXISTS BusinessHours;\n" +
                "DROP TABLE IF EXISTS CallNumbers;\n" +
                "DROP TABLE IF EXISTS ProfessorCallNumbers;\n" +
                "DROP TABLE IF EXISTS Amenity;\n" +
                "DROP TABLE IF EXISTS Coordinate;\n" +
                "DROP TABLE IF EXISTS Building;\n" +
                "DROP TABLE IF EXISTS Mascot;";
        return sql;
    }
    public String createTableBusinessZone(){
        String sql = "CREATE TABLE IF NOT EXISTS BusinessZone (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    name TEXT,\n" +
                "    location TEXT default '정보없음',\n" +
                "    category TEXT,\n" +
                "    number TEXT default '정보없음' ,\n" +
                "    link TEXT default '정보없음'\n" +
                ");";
        return sql;
    }

    public String createTableBusinessHours(){
        String sql = "CREATE TABLE IF NOT EXISTS BusinessHours( \n" +
                " name TEXT, \n" +
                " day TEXT, \n" +
                " hours TEXT, \n" +
                " primary key(name, day)); ";

        return sql;
    }

    public String createTableBuilding(){
        String sql = "CREATE TABLE IF NOT EXISTS Building (\n" +
                "building_code TEXT PRIMARY KEY,\n" +
                "building_name TEXT );";
        return sql;
    }

    public String createTableAmenity(){
        String sql = "CREATE TABLE IF NOT EXISTS Amenity(\n" +
                "\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\tcategory TEXT,\n" +
                "\tbuilding_code TEXT,\n" +
                "\tFOREIGN KEY(building_code) REFERENCES Building(building_code)\n" +
                ");";
        return sql;
    }
    public String createTableCoordinate(){
        String sql = "CREATE TABLE IF NOT EXISTS Coordinate (\n" +
                "    building_code TEXT,\n" +
                "    x1 INTEGER,\n" +
                "    y1 INTEGER,\n" +
                "    x2 INTEGER,\n" +
                "    y2 INTEGER,\n" +
                "    PRIMARY KEY(building_code),\n" +
                "    FOREIGN KEY(building_code) REFERENCES Building(building_code)\n" +
                ");";
        return sql;
    }
    public String createTableCallNumbers() {
        String sql ="CREATE TABLE IF NOT EXISTS CallNumbers (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    affiliation TEXT NOT NULL,\n" +
                "    sub_affiliation TEXT,\n" +
                "    name TEXT NOT NULL,\n" +
                "    CallNumber TEXT NOT NULL,\n" +
                "    office_number TEXT\n" +
                ");\n";

        return sql;
    }

    public String createTableProfessorCallNumbers(){
        String sql = "CREATE TABLE IF NOT EXISTS ProfessorCallNumbers (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    affiliation TEXT NOT NULL,\n" +
                "    name TEXT NOT NULL,\n" +
                "    CallNumber TEXT NOT NULL,\n" +
                "    officeNumber TEXT\n" +
                ");\n";
        return sql;
    }

    public String createTableMascot(){
        String sql = "CREATE TABLE IF NOT EXISTS Mascot (\n" +
                "    name TEXT PRIMARY KEY,\n" +
                "    gender TEXT,\n" +
                "    hobby TEXT,\n" +
                "    specialty TEXT,\n" +
                "    dislike TEXT,\n" +
                "    birthBackground TEXT,\n" +
                "    source TEXT\n" +
                ");";
        return sql;
    }

    public String insertBusinessZone(){
        String sql = "INSERT INTO BusinessZone (name, location, category, number, link)\n" +
                "VALUES\n" +
                "    ('행복한 콩박사', '경기도 오산시 양산로398번길 8-11', '음식점', '031-372-1232', 'https://map.naver.com/p/search/%ED%96%89%EB%B3%B5%ED%95%9C%EC%BD%A9%EB%B0%95%EC%82%AC/place/33402674?c=15.00,0,0,0,dh&isCorrectAnswer=true'),\n" +
                "    ('한신식당', '경기도 오산시 양산동 294-26', '음식점', '031-372-3727', 'https://map.naver.com/p/search/%ED%95%9C%EC%8B%A0%EC%8B%9D%EB%8B%B9/place/17883450?c=15.00,0,0,0,dh&placePath=%3Fentry%253Dbmp'),\n" +
                "    ('해우리', '경기도 오산시 양산동 한신대길 135 1층', '음식점', '0507-1467-2031', 'https://map.naver.com/p/search/%ED%95%B4%EC%9A%B0%EB%A6%AC%20%20%EC%98%A4%EC%82%B0%EC%A0%90/place/1435536079?c=13.00,0,0,0,dh&isCorrectAnswer=true'),\n" +
                "    ('태리로제떡볶이', '경기 오산시 한신대133번길 4 1층', '음식점', '0507-1392-7626', 'https://map.naver.com/p/entry/place/1182858063?lng=127.0235578&lat=37.1948468&placePath=%2Fhome&searchType=place&c=15.00,0,0,0,dh'),\n" +
                "    ('우리반점', '경기도 오산시 양산동 420번지', '음식점', '031-372-7551', 'https://map.naver.com/p/search/%EC%9A%B0%EB%A6%AC%EB%B0%98%EC%A0%90/place/1346004577?c=15.00,0,0,0,dh&placePath=%3Fentry%253Dbmp'),\n" +
                "    ('금덕이네', '경기도 오산시 양산동 434번지 1층', '음식점', '031-375-4777', 'https://map.naver.com/p/search/%EA%B8%88%EB%8D%95%EC%9D%B4%EB%84%A4/place/1589827139?c=15.00,0,0,0,dh&placePath=/home&isCorrectAnswer=true'),\n" +
                "    ('이삭토스트', '경기 오산시 한신대길 130', '샌드위치', '031-372-3751', 'https://map.naver.com/p/entry/place/1325063257?c=15.00,0,0,0,dh'),\n" +
                "    ('복고다방한신대점', '경기 오산시 한신대133번길 5 101호', '카페', '0507-1481-7714', 'https://map.naver.com/p/entry/place/1388145793?c=15.00,0,0,0,dh&placePath=/home'),\n" +
                "    ('미소김밥', '경기 오산시 한신대길 131', '김밥전문 음식점', '031-378-0257', 'https://map.naver.com/p/search/%EB%AF%B8%EC%86%8C%EA%B9%80%EB%B0%A5/place/1989355960?c=15.00,0,0,0,dh&placePath=%3Fentry%253Dbmp'),\n" +
                "    ('맘스터치한신대점', '경기 오산시 한신대길 126-1', '패스트푸드 음식점', '031-374-7466', 'https://map.naver.com/p/entry/place/1835784842?c=15.00,0,0,0,dh'),\n" +
                "    ('CU 편의점', '경기 오산시 한신대길 127 (양산동)', '편의점', '1577-8007', 'https://map.naver.com/p/entry/place/1786045993?c=15.00,0,0,0,adh&p=0lddsaZSms3kGLeYTcZ06Q,76.08,6.25,80,Float&isMini=true'),\n" +
                "    ('요거프레스', '경기 오산시 한신대길 125-1 1층', '카페', '0507-0289-5075', 'https://map.naver.com/p/entry/place/1726407979?c=15.00,0,0,0,adh&p=HI3iJbv5PJpeFd8qa9PTZQ,102.26,17,80,Float&isMini=true'),\n" +
                "    ('봉구스밥버거 한신대점', '경기 오산시 한신대길 126 주민당구장', '음식점', '031-375-6799', 'https://map.naver.com/p/entry/place/32874940?c=15.00,0,0,0,adh&p=mB06SnYamX-8SZis3DmccA,-117.5,12.38,80,Float&isMini=true&placePath=/home'),\n" +
                "    ('나누리', '경기도 오산시 세마동 한신대길 126', '술집', '0507-1369-2063', 'https://map.naver.com/p/entry/place/1491352469?lng=127.0229817&lat=37.1957209&placePath=%2Fhome&entry=plt&searchType=place&c=15.00,0,0,0,dh'),\n" +
                "    ('코리엔탈깻잎두마리치킨', '경기 오산시 한신대길 125', '음식점', '031-374-5850', 'https://map.naver.com/p/search/%EC%BD%94%EB%A6%AC%EC%97%94%ED%83%88%EA%B9%BB%EC%9E%8E%EB%91%90%EB%A7%88%EB%A6%AC%EC%B9%98%ED%82%A8%20%ED%95%9C%EC%8B%A0%EB%8C%80/place/1617458368?c=15.00,0,0,0,dh&isCorrectAnswer=true'),\n" +
                "    ('GS25 한신대점', '경기 오산시 한신대길 121 대원빌딩', '편의점', '031-375-3704', 'https://map.naver.com/p/entry/place/32080182?c=15.00,0,0,0,dh&placePath=/home'),\n" +
                "    ('진현가든', '경기도 오산시 양산동 444-1', '음식점', '031-372-9971', 'https://www.diningcode.com/profile.php?rid=mob3TaDPX4Xe'),\n" +
                "    ('해뜨는집', '경기도 오산시 한신대길 118', '한식당', '031-378-7825','https://map.naver.com/p/search/%ED%95%B4%EB%9C%A8%EB%8A%94%EC%A7%91%20%EC%98%A4%EC%82%B0/place/1850208189?c=15.00,0,0,0,dh&placePath=/home'),\n" +
                "    ('몽상', '경기 오산시 양산로 354 1층 몽상', '음식점', '0508-8189-9706', 'https://map.naver.com/p/entry/place/1472232373?c=15.00,0,0,0,dh&placePath=/home'),\n" +
                "    ('카페 리메인', '경기도 오산시 양산로 351', '카페', '031-374-1208', 'https://www.google.com/maps/place/%EC%B9%B4%ED%8E%98+%EB%A6%AC%EB%A9%94%EC%9D%B8/data=!3m2!1e3!4b1!4m6!3m5!1s0x357b41d9e3aa81cd:0xd811f34061b612e5!8m2!3d37.1960732!4d127.0237567!16s%2Fg%2F11syf91y6h?entry=ttu&g_ep=EgoyMDI0MDgyMS4wIKXMDSoASAFQAw%3D%3D'),\n" +
                "    ('내가찜한닭', '경기 오산시 양산로 347', '음식점', '031-372-2356', 'https://map.naver.com/p/entry/place/35562793?c=15.00,0,0,0,dh&placePath=/home'),\n" +
                "    ('양산골 주막', '경기도 오산시 양산동 한신대길 113', '술집', '031-377-7371', 'https://map.naver.com/p/entry/place/37131480?c=15.00,0,0,0,dh&placePath=/home'),\n" +
                "    ('듬뿍만두샤브', '경기도 오산시 세마동 양산로 374', '음식점', '031-377-1187', 'https://map.naver.com/p/entry/place/13432346?c=15.00,0,0,0,dh'),\n" +
                "    ('현대E마트', '경기도 오산시 양산동 379-5', '식료품점', '031-372-7743', 'https://map.naver.com/p/entry/place/17883403?c=15.00,0,0,0,dh&placePath=/home'),\n" +
                "    ('드렁큰할매', '경기 오산시 한신대길 125 2층 드렁큰할매', '술집', '0507-1482-3307', 'https://map.naver.com/p/entry/place/1198298147?c=15.00,0,0,0,dh&placePath=/home');";
        return sql;
    }

    public String insertBusinessHours(){
        String sql = "REPLACE INTO BusinessHours (name, day, hours) \n " +
                "VALUES\n" +
                "('행복한 콩박사',  '월', '11:00 - 21:00'), \n" +
                "('행복한 콩박사',  '화', '11:00 - 21:00'), \n" +
                "('행복한 콩박사',  '수', '11:00 - 21:00'), \n" +
                "('행복한 콩박사',  '목', '11:00 - 21:00'), \n" +
                "('행복한 콩박사',  '금', '11:00 - 21:00'), \n" +
                "('행복한 콩박사',  '토', '11:00 - 21:00 \n(15:00 - 16:30 브레이크 타임)'), \n" +
                "('행복한 콩박사',  '일', '11:00 - 21:00 \n(15:00 - 16:30 브레이크 타임)'), \n" +
                "('해우리', '월', '11:00 - 24:00'), \n" +
                "('해우리', '화', '11:00 - 24:00'), \n" +
                "('해우리', '수', '11:00 - 24:00'), \n" +
                "('해우리', '목', '11:00 - 24:00'), \n" +
                "('해우리', '금', '11:00 - 24:00'), \n" +
                "('해우리', '토', '휴무'), \n" +
                "('해우리', '일', '휴무'), \n" +
                "('태리로제떡볶이', '월', '10:55 - 23:00'), \n" +
                "('태리로제떡볶이', '화', '10:55 - 23:00'), \n" +
                "('태리로제떡볶이', '수', '10:55 - 23:00'), \n" +
                "('태리로제떡볶이', '목', '10:55 - 23:00'), \n" +
                "('태리로제떡볶이', '금', '10:55 - 23:00'), \n" +
                "('태리로제떡볶이', '토', '10:55 - 23:00'), \n" +
                "('태리로제떡볶이', '일', '10:55 - 23:00'), \n" +
                "('이삭토스트', '월', '9:30 - 20:00'),\n " +
                "('이삭토스트', '화', '9:30 - 20:00'),\n " +
                "('이삭토스트', '수', '9:30 - 20:00'),\n " +
                "('이삭토스트', '목', '9:30 - 20:00'),\n " +
                "('이삭토스트', '금', '9:30 - 20:00'),\n " +
                "('이삭토스트', '토', '10:00 - 19:00'),\n " +
                "('이삭토스트', '일', '10:00 - 19:00'),\n " +
                "('복고다방한신대점', '월', '9:10 - 16:00'),\n "  +
                "('복고다방한신대점', '화', '9:10 - 16:00'),\n "  +
                "('복고다방한신대점', '수', '9:10 - 16:00'),\n "  +
                "('복고다방한신대점', '목', '9:10 - 16:00'),\n "  +
                "('복고다방한신대점', '금', '9:10 - 16:00'),\n "  +
                "('복고다방한신대점', '토', '12:00 - 16:00'),\n "  +
                "('복고다방한신대점', '일', '12:00 - 16:00'),\n "  +
                "('맘스터치한신대점', '월', '11:00 - 21:00'),\n "  +
                "('맘스터치한신대점', '화', '11:00 - 21:00'),\n "  +
                "('맘스터치한신대점', '수', '11:00 - 21:00'),\n "  +
                "('맘스터치한신대점', '목', '11:00 - 21:00'),\n "  +
                "('맘스터치한신대점', '금', '11:00 - 21:00'),\n "  +
                "('맘스터치한신대점', '토', '11:00 - 21:00'),\n "  +
                "('맘스터치한신대점', '일', '11:00 - 21:00'),\n "  +
                "('요거프레소', '월', '11:00 - 20:00'),\n "  +
                "('요거프레소', '화', '11:00 - 20:00'),\n "  +
                "('요거프레소', '수', '11:00 - 20:00'),\n "  +
                "('요거프레소', '목', '11:00 - 20:00'),\n "  +
                "('요거프레소', '금', '11:00 - 20:00'),\n "  +
                "('요거프레소', '토', '휴무'),\n "  +
                "('요거프레소', '일', '11:00 - 20:00'),\n "  +
                "('봉구스밥버거 한신대점', '월', '10:30 - 19:00'),\n "  +
                "('봉구스밥버거 한신대점', '화', '10:30 - 19:00'),\n "  +
                "('봉구스밥버거 한신대점', '수', '10:30 - 19:00'),\n "  +
                "('봉구스밥버거 한신대점', '목', '10:30 - 19:00'),\n "  +
                "('봉구스밥버거 한신대점', '금', '10:30 - 19:00'),\n "  +
                "('봉구스밥버거 한신대점', '토', '10:30 - 19:00'),\n "  +
                "('봉구스밥버거 한신대점', '일', '휴무'),\n "  +
                "('나누리한신대본점', '월', '15:00 - 04:00'),\n "  +
                "('나누리한신대본점', '화', '15:00 - 04:00'),\n "  +
                "('나누리한신대본점', '수', '15:00 - 04:00'),\n "  +
                "('나누리한신대본점', '목', '15:00 - 04:00'),\n "  +
                "('나누리한신대본점', '금', '15:00 - 04:00'),\n "  +
                "('나누리한신대본점', '토', '18:00 - 24:00'),\n "  +
                "('나누리한신대본점', '일', '18:00 - 03:00'),\n "  +
                "('진현가든', '월', '08:00 - 21:00'),\n "  +
                "('진현가든', '화', '08:00 - 21:00'),\n "  +
                "('진현가든', '수', '08:00 - 21:00'),\n "  +
                "('진현가든', '목', '08:00 - 21:00'),\n "  +
                "('진현가든', '금', '08:00 - 21:00'),\n "  +
                "('진현가든', '토', '08:00 - 21:00'),\n "  +
                "('진현가든', '일', '휴무'),\n "  +
                "('몽상', '월', '11:00 - 21:00'),\n "  +
                "('몽상', '화', '11:00 - 21:00'),\n "  +
                "('몽상', '수', '11:00 - 21:00'),\n "  +
                "('몽상', '목', '11:00 - 21:00'),\n "  +
                "('몽상', '금', '11:00 - 21:00'),\n "  +
                "('몽상', '토', '휴무'),\n "  +
                "('몽상', '일', '11:30 - 21:00'),\n "  +
                "('화락', '월', '11:00 - 23:00'),\n "  +
                "('화락', '화', '11:00 - 23:00'),\n "  +
                "('화락', '수', '11:00 - 23:00'),\n "  +
                "('화락', '목', '11:00 - 23:00'),\n "  +
                "('화락', '금', '11:00 - 23:00'),\n "  +
                "('화락', '토', '11:00 - 23:00'),\n "  +
                "('화락', '일', '휴무'),\n "  +
                "('카페 리메인', '월', '10:30 - 21:00'),\n "  +
                "('카페 리메인', '화', '10:30 - 21:00'),\n "  +
                "('카페 리메인', '수', '10:30 - 21:00'),\n "  +
                "('카페 리메인', '목', '10:30 - 21:00'),\n "  +
                "('카페 리메인', '금', '10:30 - 21:00'),\n "  +
                "('카페 리메인', '토', '10:30 - 21:00'),\n "  +
                "('카페 리메인', '일', '10:30 - 20:00'),\n "  +
                "('내가찜한닭', '월', '11:00 - 22:00'),\n "  +
                "('내가찜한닭', '화', '11:00 - 22:00'),\n "  +
                "('내가찜한닭', '수', '11:00 - 22:00'),\n "  +
                "('내가찜한닭', '목', '11:00 - 22:00'),\n "  +
                "('내가찜한닭', '금', '11:00 - 22:00'),\n "  +
                "('내가찜한닭', '토', '11:00 - 22:00'),\n "  +
                "('내가찜한닭', '일', '11:00 - 22:00'),\n "  +
                "('듬뿍만두샤브', '월', '09:00 - 22:00'),\n "  +
                "('듬뿍만두샤브', '화', '09:00 - 22:00'),\n "  +
                "('듬뿍만두샤브', '수', '09:00 - 22:00'),\n "  +
                "('듬뿍만두샤브', '목', '09:00 - 22:00'),\n "  +
                "('듬뿍만두샤브', '금', '09:00 - 22:00'),\n "  +
                "('듬뿍만두샤브', '토', '09:00 - 22:00'),\n "  +
                "('듬뿍만두샤브', '일', '휴무'),\n "  +
                "('드렁큰할매', '월', '11:00 - 25:00'),\n "  +
                "('드렁큰할매', '화', '11:00 - 25:00'),\n "  +
                "('드렁큰할매', '수', '11:00 - 25:00'),\n "  +
                "('드렁큰할매', '목', '11:00 - 25:00'),\n "  +
                "('드렁큰할매', '금', '11:00 - 25:00'),\n "  +
                "('드렁큰할매', '토', '11:00 - 25:00'),\n "  +
                "('드렁큰할매', '일', '17:00 - 24:00');";
        return sql;
    }

    public String insertBuilding(){
        String sql = "REPLACE INTO Building (building_code, building_name)\n" +
                "VALUES\n" +
                "('1', '장공관(본관)'),\n" +
                "('2', '필헌관(대학원)'),\n" +
                "('3', '만우관'),\n" +
                "('4', '샬롬채플'),\n" +
                "('5', '임마누엘관(학생회관)'),\n" +
                "('6', '경삼관(도서관)'),\n" +
                "('7', '송암관'),\n" +
                "('8', '소통관(어학관)'),\n" +
                "('9', '학과실습동'),\n" +
                "('10', '한울관(체육관)'),\n" +
                "('11', '성빈학사(생활관)'),\n" +
                "('14', '새롬터'),\n" +
                "('17', '해오름관'),\n" +
                "('18', '장준하통일관'),\n" +
                "('20', '늦봄관'), \n" +
                "('0', '버스 정류장');";
        return sql;
    }

    public String insertAmenity(){
        String sql = "REPLACE INTO Amenity (category, building_code)\n" +
                "VALUES\n" +
//                "('서점', '5'),\n" +
                "('남학생휴게실', '3'),\n" +
                "('여학생휴게실', '17'),\n" +
                "('ATM', '2'),\n" +
                "('ATM', '6'),\n" +
                "('ATM', '17'),\n" +
                "('ATM' , '18'),\n" +
                "('복사기', '2'),\n" +
                "('복사기', '3'),\n" +
                "('복사기', '6'),\n" +
                "('복사기' , '7'),\n" +
                "('복사기', '11'),\n" +
                "('복사기' , '18'),\n" +
                "('복사기', '20'),\n" +
                "('카페', '3'),\n" +
                "('카페', '6'),\n" +
                "('카페' , '14' ),\n" +
                "('카페' , '18'),\n" +
                "('우체국', '17'),\n" +
                "('식당', '5'),\n" +
                "('식당' , '18'),\n" +
                "('편의점', '5'),\n" +
                "('편의점' , '11'),\n" +
                "('편의점', '18');";
        return sql;
    }

    public String insertCoordinate(){
        String sql = "REPLACE INTO Coordinate (building_code, x1, y1, x2, y2) VALUES\n" +
                "('1', 1295,554,1379,641), \n" +
                "('2', 1088,473,1172,560), \n" +
                "('3', 1685,389,1772,473), \n" +
                "('4', 2051,509,2135,590), \n" +
                "('5', 1568,788,1652,873), \n" +
                "('6', 1178,758,1259,840), \n" +
                "('7', 797,635,882,722), \n" +
                "('8', 1013,659,1100,743), \n" +
                "('9', 482,539,570,621), \n" +
                "('10', 1880,906,1961,990), \n" +
                "('11', 2445,1170,2529,1257), \n" +
                "('14', 1022,315,1106,398), \n" +
                "('17', 1526,939,1613,1023), \n" +
                "('18', 521,362,610,449), \n" +
                "('20', 1166,247,1253,328);";
//                "('20', '1289', '294', '1385', '395')',  \n"  +
//                "('bus', '183', '1032', '518', '1163');";
        return sql;
    }

    public String insertMascot(){
        String ku_o = "오래전에 멸종한 종이지만<comma> 대한민국에 아직 사라지지 않은 강인한 공룡인 「쿠오」는 오랜 세월이 " +
                "흘러도 믿음과 진보적인 끈기를 잃지 않고 성장하는 한신대학교와 많이 닮았다. " +
                "1940년 대한민국이 혼란하던 시기에 조선신학원(한신대학교)이 어렵게 설립되면서 신학을 전하고자 하던 " +
                "굳건한 마음과 지식을 전하고자 하는 학도들의 믿음의 씨앗이 모여 하나의 큰 일이 되었다. " +
                "1970년대 말 오산으로 부지를 옮기며 한신대학이 종합화가 되어 또 다른 시작을 할 때 큰일에서 " +
                "한신대학교와 똑 닮은 쿠오가 탄생했다. " +
                "현재 한신대학교 오산캠퍼스의 모양이 쿠오와 닮아있는 것도 이 때문이라는 컨셉이다.";
        String hangomi = "「한고미」 는 본래 한신대학교 뒷산에 홀로 살던 파란색 곰이었으나<comma> 오산캠퍼스 설립시기에 우연히 내려와 우리 학교에서 지내게 되었다. " +
                "한신대학교 투쟁 역사와 함께하면서 몸에 열(빨간색)이 올랐는데<comma> 점점 본래의 색과 섞이며 털이 우리 학교 교색과 같은 보랏빛으로 바뀌었다.\n\n " +
                "평소에는 무던하고 우리 학교 구성원들과 어울리는 것을 좋아하는 성격이다. 그러나 불의를 보면 묵인하지 않고 부당한 상황에 맞서는 평화 중재자의 면모를 지녔다. " +
                "국내 토착종이자 우리 민족의 상징이라고 볼 수 있는 반달가슴곰으로<comma> 곰의 고대국어 <고마>에서 유래됐다.";

        String buzzi = "「버지」는 무한한 가능성과 잠재력이 있는 한신대학교 학생들과 닮아있다. " +
                "한신대학교의 수많은 벚나무 벚꽃 중에서도 작고 비완전체인 벚꽃 한 잎 일지라도 쿠오와 함께 자신을 탐구하고 열정적으로 꿈을 찾아 나아간다.";

        String hanggu = " 「한꾸」는 한신대학교의 꿈 꾸는 아기호랑이로<comma> 무엇이든 꿈 꿀 수 있고 무엇이든 될 수 있는 한신대학교 학생을 상징한 캐릭터이다. " +
                "백호 한꾸의 얼굴 무늬는 한신의 HS로 이루어져 있다. 한꾸는 항상 자신의 꿈을 위해 뛰어다녀 앞머리가 회오리 모양으로 고정되어 있다. " +
                "이는 목표를 이루기 위해 고군부투하는 우리 학생들의 의지를 상징한다.\n\n " +
                "한신대학교의 꿈 꾸는 아기호랑이 한꾸는 정직하고 상냥한 성격을 가졌으며 누구를 만나도 먼저 인사하는 인기인이다. " +
                "용맹한 호랑이로 도전을 두려워하지 않으며 모든 활동을 경험하고 싶어해 항상 바쁘게 뛰어다니기 때문에 앞머리는 항상 바람 모양으로 돌돌 말려있다. " +
                "도전을 두려워하는 친구를 만날 때마다 항상 성과가 어떻든 한 번 해보라는 말을 해주며 도전을 두려워하지 않는 한꾸가 도전자체가 값전 것이라는 메시지를 전달한다.";

        String sql = "REPLACE INTO Mascot (name, gender, hobby, specialty, dislike, birthBackground, source) VALUES\n" +
                "('쿠오 (KU-O)', '?', '순찰하기<comma> 새로운 길 탐색하기<comma> 퍼즐 맞추기', '발차기', '대충', '" + ku_o + "', '쿠오와 버지'), \n" +
                "('한고미 (HANGOMI)', '?', '학교 앞 맛집 탐방<comma> 한신공원 산책', '교수님과 학생들의 소통창구 되어주기', '?', '" + hangomi + "', '한고미'), \n" +
                "('버지 (BUZZI)', '?', '독서', '철학<comma> 경제<comma> 그림<comma> 컴퓨터<comma> 외국어<comma> 체육', '송충이', '"+ buzzi + "', '쿠오와 버지'), \n" +
                "('한꾸 (HANGGU)', '?', '?', '?', '?', '" + hanggu + "', '한꾸');";

        return sql;
    }

    public String insertCallNumbers() {
        String sql = "REPLACE INTO CallNumbers (affiliation, sub_affiliation, name, CallNumber, office_number) VALUES\n" +
                "('한신대학교','총장','강성영','031-379-0001','1201'),\n" +
                "('교목실','교목실장','나현기','031-379-0011','4102'),\n" +
                "('교목실','조교수','강은미','031-379-0781','17309'),\n" +
                "('교목실','조교수','진형섭','031-379-0779','17303'),\n" +
                "('교목실','담당','김흥식','031-379-0012','4106'),\n" +
                "('교목실','담당','박충만','031-379-0013','4106'),\n" +
                "('교목실','담당','김영호','031-379-0014','4106'),\n" +
                "('비서팀','팀장','김성미','031-379-0002','1202'),\n" +
                "('대학본부','부총장','이인재','031-379-0003','8403'),\n" +
                "('비서팀','담당','선미다','031-379-0001','1202'),\n" +
                "('비서팀','담당','성희영','031-379-0001','1202'),\n" +
                "('비서팀','담당','신문섭','031-379-0002','1202'),\n" +
                "('대외협력센터','센터장','전광희','031-379-0892','1317'),\n" +
                "('대외협력센터','담당','박병룡','031-379-0891','1317'),\n" +
                "('대외협력센터','담당','박주희','031-379-0890','1317'),\n" +
                "('인권센터','인권센터장','김민환','031-379-0765','17303'),\n" +
                "('인권센터','담당','윤슬기','031-379-0807','5007'),\n" +
                "('감사실','감사실장','전광희','031-379-0110','3517'),\n" +
                "('미래혁신본부','대학혁신추진단장','최창원','031-379-0638','18411'),\n" +
                "('감사실','담당','김미선','031-379-0111','1301'),\n" +
                "('미래혁신본부','RISE사업단장','김상욱','031-379-0593','7308'),\n" +
                "('미래혁신본부','SW중심대학사업단장','류승택','031-379-0644','18407'),\n" +
                "('기획처','기획처장','김상욱','031-379-0030','7308'),\n" +
                "('기획처','기획예산팀장','신윤선','031-379-0031','1304'),\n" +
                "('기획처','담당','김재욱','031-379-0033','1304'),\n" +
                "('기획처','담당','장민수','031-379-0034','1304'),\n" +
                "('기획처','담당','김진영','031-379-0035','1304'),\n" +
                "('기획처','혁신성과평가팀장','김정현','031-379-0851','1315'),\n" +
                "('기획처','담당','최지혜','031-379-0852','1315'),\n" +
                "('기획처','담당','이태영','031-379-0856','1315'),\n" +
                "('기획처','일반연구원','서예림','031-379-0854','1315'),\n" +
                "('기획처','일반연구원','한승아','031-379-0858','1315'),\n" +
                "('기획처','일반연구원','정유정','031-379-0247','1315'),\n" +
                "('기획처','일반연구원','이수인','031-379-0857','1315'),\n" +
                "('기획처','정보시스템팀장','조명원','031-379-0121','18305'),\n" +
                "('기획처','학사 업무','정준훈','031-379-0125','18305'),\n" +
                "('기획처','시스템·홈페이지 업무','유지혜','031-379-0122','18305'),\n" +
                "('기획처','그룹웨어·행정 업무','김현희','031-379-0124','18305'),\n" +
                "('기획처','AI빅데이터센터장','이양선','031-379-0632','18410'),\n" +
                "('기획처','조교수','김애영','031-379-0609','8207'),\n" +
                "('기획처','담당','조익일','031-379-0251','5006'),\n" +
                "('기획처','담당','박세환','031-379-0258','5006'),\n" +
                "('기획처','일반연구원','원유정','031-379-0254','5006'),\n" +
                "('기획처','일반연구원','임은애','031-379-0259','5006'),\n" +
                "('기획처','IR센터장','이양선','031-379-0632','18410'),\n" +
                "('기획처','IR센터 연구교수','이승연','031-379-0828','2101'),\n" +
                "('기획처','담당','이소현','031-379-0252','5006'),\n" +
                "('기획처','일반연구원','임수빈','031-379-0257','5006'),\n" +
                "('기획처','이러닝센터장','이양선','031-379-0632','18410'),\n" +
                "('기획처','담당','손병창','031-379-0751','20104'),\n" +
                "('기획처','담당','진민걸','031-379-0754','20309'),\n" +
                "('기획처','일반연구원','김한나','031-379-0752','20304'),\n" +
                "('기획처','디지털새싹사업단장','류승택','031-379-0644','18407'),\n" +
                "('기획처','일반연구원','박미주','031-379-0255','5006'),\n" +
                "('기획처','일반연구원','김한울','031-379-0256','5006'),\n" +
                "('기획처','일반연구원','조영미','031-379-0253','5006'),\n" +
                "('교무혁신처','교무혁신처장','최창원','031-379-0020','1203'),\n" +
                "('교무혁신처','교무팀장','박문수','031-379-0021','8104'),\n" +
                "('교무혁신처','담당','김기중','031-379-0024','8104'),\n" +
                "('교무혁신처','담당','신한나','031-379-0022','8104'),\n" +
                "('교무혁신처','담당','류수윤','031-379-0025','8104'),\n" +
                "('교무혁신처','담당','유가은','031-379-0023','8104'),\n" +
                "('교무혁신처','일반연구원','이세린','031-379-0264','8104'),\n" +
                "('교무혁신처','대학행정팀장','안혜영','031-379-0450','8106'),\n" +
                "('교무혁신처','담당','김가현','031-379-0631','18421-1'),\n" +
                "('교무혁신처','담당','박슬기롭다','031-379-0410','8105'),\n" +
                "('교무혁신처','담당','권미애','031-379-0470','8106'),\n" +
                "('교무혁신처','사회복지학/재활상담학 담당','심주영','031-379-0510','8106'),\n" +
                "('교무혁신처','신학/독일어문화학/영미문화학 담당','윤수빈','031-379-0420','8105'),\n" +
                "('교무혁신처','실습: 이‧공계융합 AI‧SW','윤사랑','031-379-0628','18421-1'),\n" +
                "('교무혁신처','학적:이·공계융합/AI·SW 담당','이소정','031-379-0649','18421-1'),\n" +
                "('교무혁신처','수업:경영학/종교문화학 담당','서지영','031-379-0570','8105'),\n" +
                "('교무혁신처','수업:소프트웨어융합학/IT영상콘텐츠학/AI시스템반도체학 담당','이소은','031-379-0650','18421-1'),\n" +
                "('교무혁신처','수업:컴퓨터공학/인공지능/데이터사이언스/XR콘텐츠 담당','박진용','031-379-0630','18421-1'),\n" +
                "('교무혁신처','수업:심리·아동학/사회학/평생교육HRD(연) 담당','이소윤','031-379-0550','8106'),\n" +
                "('교무혁신처','수업:중국학/일본학/국제경제학 담당','박세현','031-379-0560','8106'),\n" +
                "('교무혁신처','수업:한국어문학/ 문예창작학/ IT경영학 담당','황지윤','031-379-0390','8105'),\n" +
                "('교무혁신처','수업: 글로벌인재학/공공인재빅데이터융합학/경제학','최형서','031-379-0490','8106'),\n" +
                "('교무혁신처','수업:신학/독일어문화학/영미문화학/글로벌비즈니스학','안희연','031-379-0420','8105'),\n" +
                "('교무혁신처','학적: 인문융합대학/계열','고정훈','031-379-0440','8105'),\n" +
                "('교무혁신처','수업:사회복지학/ 재활상담학/ 특수체육학 담당','지다영','031-379-0510','8106'),\n" +
                "('교무혁신처','학적: 신학대학/ 경영미디어대학','방진현','031-379-0460','8105'),\n" +
                "('교무혁신처','학적:글로벌융합대학/글로벌인재학/공공인재빅데이터융합학 담당','박우성','031-379-0530','8106'),\n" +
                "('교무혁신처','수업:미디어영상광고홍보학/철학 담당','조수빈','031-379-0580','8105'),\n" +
                "('교무혁신처','학적: 휴먼서비스대학','장희주','031-379-0500','8106'),\n" +
                "('교무혁신처','수업: 한국사학/디지털영상문화콘텐츠학/중국어문화콘텐츠학','김하은','031-379-0509','8105'),\n" +
                "('교무혁신처','수업: 수리금융학 응용통계학','강혁인','031-379-0607','18421-1'),\n" +
                "('교무혁신처','교수연구팀장','이점건','031-379-0026','1102'),\n" +
                "('교무혁신처','담당','전재선','031-379-0028','1102'),\n" +
                "('교무혁신처','교원인사','강현우','031-379-0027','1102'),\n" +
                "('교무혁신처','담당','전인주','031-379-0029','1102'),\n" +
                "('교무혁신처','교육혁신원원장','최창원','031-379-0638','18411'),\n" +
                "('교무혁신처','교육혁신원부원장','장익현','031-379-0517','8314'),\n" +
                "('교무혁신처','교육혁신원 연구교수','차정민','031-379-0827','20407'),\n" +
                "('교무혁신처','교육혁신원 연구교수','이승연','031-379-0828','20407'),\n" +
                "('교무혁신처','교육혁신원 연구교수','김진주','031-379-0829','20407'),\n" +
                "('교무혁신처','교수학습지원센터장','나경욱','031-379-0670','3521'),\n" +
                "('교무혁신처','학습지원센터 연구교수','차정민','031-379-0827','2101'),\n" +
                "('교무혁신처','연구교수','김진주','031-379-0829','20407'),\n" +
                "('교무혁신처','학습지원센터 연구교수','이승연','031-379-0828','2101'),\n" +
                "('교무혁신처','교수학습지원센터 담당','이재영','031-379-0750','6202'),\n" +
                "('교무혁신처','교수학습지원센터 담당','김가영','031-379-0836','6202'),\n" +
                "('교무혁신처','일반연구원','이유라','031-379-0755','6202'),\n" +
                "('교무혁신처','일반연구원','이동진','031-379-0835','6202'),\n" +
                "('교무혁신처','교양교육센터장','나경욱','031-379-0670','3521'),\n" +
                "('교무혁신처','담당','최성혜','031-379-0732','8107'),\n" +
                "('교무혁신처','일반연구원','엄태선','031-379-0733','8107'),\n" +
                "('교무혁신처','교육성과관리센터장','장익현','031-379-0517','8314'),\n" +
                "('교무혁신처','연구교수','김진주','031-379-0829','6201'),\n" +
                "('교무혁신처','연구교수','차정민','031-379-0827','2101'),\n" +
                "('교무혁신처','연구교수','이승연','031-379-0828','2101'),\n" +
                "('교무혁신처','교육성과관리센터 팀장','이기형','031-379-0740','Jan-01'),\n" +
                "('교무혁신처','담당','오수연','031-379-0837','Jan-01'),\n" +
                "('교무혁신처','선임급연구원','권다영','031-379-0756','6201'),\n" +
                "('교무혁신처','일반연구원','한지원','031-379-0758','Jan-02'),\n" +
                "('교무혁신처','일반연구원','박선이','031-379-0831','Jan-02'),\n" +
                "('교무혁신처','SW교육센터장','이양선','031-379-0632','18410'),\n" +
                "('교무혁신처','부교수','박종현','031-379-0796','18408'),\n" +
                "('교무혁신처','SW교육센터 팀장','이기형','031-379-0740','Jan-01'),\n" +
                "('교무혁신처','일반연구원','정유정','031-379-0247','6202'),\n" +
                "('학생지원처','학생지원처장','차윤정','031-379-0040','3517'),\n" +
                "('학생지원처','학생복지팀장','송성선','031-379-0041','6426'),\n" +
                "('학생지원처','국내 장학','이혜영','031-379-0044','6426'),\n" +
                "('학생지원처','국가장학','최윤호','031-379-0042','6426'),\n" +
                "('학생지원처','보건진료소','김나영','031-379-0775','17106'),\n" +
                "('학생지원처','담당','박원식','031-379-0043','6426'),\n" +
                "('학생지원처','담당','정하나','031-379-0045','6426'),\n" +
                "('학생지원처','학생상담센터장','안도연','031-379-0749','8225'),\n" +
                "('학생지원처','담당','허영주','031-379-0769','6223'),\n" +
                "('학생지원처','담당','김지혜','031-379-0771','6223'),\n" +
                "('학생지원처','담당','조예지','031-379-0771','6223'),\n" +
                "('학생지원처','일반연구원','이정신','031-379-0770','6223'),\n" +
                "('학생지원처','장애학생지원센터장','김예랑','031-379-0518','8312'),\n" +
                "('학생지원처','담당','박원식','031-379-0043','6105'),\n" +
                "('학생지원처','생활관장(경기)','김흥기','031-379-0170','7209'),\n" +
                "('학생지원처','담당','양성모','031-379-0177','생활관'),\n" +
                "('학생지원처','담당','조현식','031-379-0171','생활관'),\n" +
                "('학생지원처','담당','염승자','031-379-0172','생활관'),\n" +
                "('학생지원처','담당','오민석','031-379-0173','생활관'),\n" +
                "('학생지원처','일반연구원','김성현','031-379-0175','생활관'),\n" +
                "('학생지원처','대학일자리플러스센터장','김용희','031-379-0050','1309'),\n" +
                "('학생지원처','진로취업팀장','이헌준','031-379-0051','6101'),\n" +
                "('학생지원처','담당','박가현','031-379-0057','6101'),\n" +
                "('학생지원처','담당','신수정','031-379-0056','6105'),\n" +
                "('학생지원처','담당','김익중','031-379-0053','6101'),\n" +
                "('학생지원처','일반연구원','이수진','031-379-0811','6101'),\n" +
                "('학생지원처','일반연구원','신아름','031-379-0812','6101'),\n" +
                "('학생지원처','일반연구원','김도아','031-379-0810','6101'),\n" +
                "('학생지원처','일반연구원','남궁혜영','031-379-0897','6101'),\n" +
                "('학생지원처','일반연구원','박은수','031-379-0816','6101'),\n" +
                "('학생지원처','일반연구원','정지혜','031-379-0813','6105'),\n" +
                "('학생지원처','일반연구원','강종곤','031-379-0054','6101'),\n" +
                "('학생지원처','일반연구원','조영식','031-379-0815','6105'),\n" +
                "('학생지원처','일반연구원','순혜란','031-379-0052','6105'),\n" +
                "('학생지원처','일반연구원','최민선','031-379-0894','6105'),\n" +
                "('학생지원처','일반연구원','황애양','031-379-0058','6101'),\n" +
                "('학생지원처','일반연구원','정해인','031-379-0682','6105'),\n" +
                "('학생지원처','일반연구원','김지수','031-379-0681','6105'),\n" +
                "('학생지원처','일반연구원','최하진','031-379-0683','6105'),\n" +
                "('학생지원처','일반연구원','박명숙','031-379-0680','6105'),\n" +
                "('학생지원처','일반연구원','최수경','031-379-0684','6105'),\n" +
                "('학생지원처','일반연구원','김영림','031-379-0685','6105'),\n" +
                "('학생지원처','IPP형일학습병행사업단장','김용희','031-379-0885','7210'),\n" +
                "('학생지원처','부교수','최기석','031-379-0887','17103'),\n" +
                "('학생지원처','부교수','손성달','031-379-0886','17104'),\n" +
                "('학생지원처','IPP센터장','김용희','031-379-0885','7210'),\n" +
                "('학생지원처','IPP센터 팀장','김민정','031-379-0881','6201'),\n" +
                "('학생지원처','IPP센터/듀얼공동훈련센터 선임급연구원','김동근','031-379-0876','6201'),\n" +
                "('학생지원처','일반연구원','방진희','031-379-0898','6201'),\n" +
                "('학생지원처','IPP센터/듀얼공동훈련센터 일반연구원','박혜미','031-379-0879','6201'),\n" +
                "('학생지원처','IPP센터/듀얼공동훈련센터 일반연구원','조민영','031-379-0878','6201'),\n" +
                "('학생지원처','IPP센터/듀얼공동훈련센터 일반연구원','이현희','031-379-0880','6201'),\n" +
                "('학생지원처','일반연구원','김솔','031-379-0877','6201'),\n" +
                "('학생지원처','듀얼공동훈련센터장','김용희','031-379-0885','7210'),\n" +
                "('학생지원처','부교수','이경봉','031-379-0884','17104'),\n" +
                "('학생지원처','팀장','김민정','031-379-0881','6201'),\n" +
                "('학생지원처','RnD산업인턴사업단장','이양선','031-379-0632','18410'),\n" +
                "('학생지원처','RnD산업인턴사업팀장','이헌준','031-379-0051','6101'),\n" +
                "('학생지원처','담당','유지혜','031-379-0122','18305'),\n" +
                "('학생지원처','일반연구원','이주은','031-379-0686','6101'),\n" +
                "('사무처','사무처장','전석철','031-379-0060','1206'),\n" +
                "('사무처','총무팀장','최승훈','031-379-0062','1101'),\n" +
                "('사무처','4대보험/ 개인정보보호','이정화','031-379-0063','1101'),\n" +
                "('사무처','담당','정인수','031-379-0064','1101'),\n" +
                "('사무처','구매·입찰·계약','서태원','031-379-0066','1101'),\n" +
                "('사무처','복무근태·서무','노경숙','031-379-0065','1101'),\n" +
                "('사무처','담당','양성모','031-379-0863','1101'),\n" +
                "('사무처','재무팀장','김종호','031-379-0074','1104'),\n" +
                "('사무처','급여·회계','김은비','031-379-0077','1104'),\n" +
                "('사무처','등록·회계','서민지','031-379-0075','1104'),\n" +
                "('사무처','재무팀 담당','한송희','031-379-0076','1104'),\n" +
                "('사무처','경비·주차','종합상황실','031-379-0070','정문'),\n" +
                "('사무처','시설자산팀장','장봉기','031-379-0080','6424'),\n" +
                "('사무처','전기','최성찬','031-379-0085','7001'),\n" +
                "('사무처','건축','이창덕','031-379-0084','6424'),\n" +
                "('사무처','목공','전세정','031-379-0088','6424'),\n" +
                "('사무처','조경','이재길','031-379-0087','20002'),\n" +
                "('사무처','건축','임종근','031-379-0093','3102'),\n" +
                "('사무처','설비','이영호','031-379-0098','2004'),\n" +
                "('사무처','설비','권상민','031-379-0097','2004'),\n" +
                "('사무처','전기','함석준','031-379-0096','7001'),\n" +
                "('사무처','미화','김미미','031-379-0092','3216'),\n" +
                "('사무처','안전','김경태','031-379-0083','6424'),\n" +
                "('사무처','담당','고유한','031-379-0081','6424'),\n" +
                "('사무처','담당','이수인','031-379-0082','6424'),\n" +
                "('사무처','미화용역감독','박연원','031-379-0298','18115'),\n" +
                "('사무처','그린캠퍼스사업단장','이상헌','031-379-0721','3518'),\n" +
                "('사무처','그린캠퍼스사업단 담당','전누리','031-379-0116','6424'),\n" +
                "('입학·홍보본부','입학홍보본부장','지원배','031-379-0101','8411'),\n" +
                "('입학·홍보본부','입학인재발굴팀장','류희정','031-379-0102','1103'),\n" +
                "('입학·홍보본부','입학전형','양수열','031-379-0103','1103'),\n" +
                "('입학·홍보본부','입학전형','김유진','031-379-0105','1103'),\n" +
                "('입학·홍보본부','입학전형','강호섭','031-379-0104','1103'),\n" +
                "('입학·홍보본부','담당','이은수','031-379-0106','1103'),\n" +
                "('입학·홍보본부','브랜드홍보팀장','김동규','031-379-0036','1313'),\n" +
                "('입학·홍보본부','언론·온라인홍보','정보라','031-379-0037','1313'),\n" +
                "('입학·홍보본부','브랜드홍보팀','서창수','031-379-0038','1313'),\n" +
                "('서울캠퍼스행정처','서울캠퍼스행정처장','전석철','02-2125-0173','1206'),\n" +
                "('서울캠퍼스행정처','부처장','임충','02-2125-0107','2201'),\n" +
                "('서울캠퍼스행정처','사무행정처 팀장','황은영','02-2125-0109','2201'),\n" +
                "('서울캠퍼스행정처','기능주임','김영국','02-2125-0112','2102'),\n" +
                "('서울캠퍼스행정처','담당','이광표','02-2125-0111','2201'),\n" +
                "('서울캠퍼스행정처','담당','조나단','02-2125-0106','2201'),\n" +
                "('서울캠퍼스행정처','담당','송실','02-2125-0246','2201'),\n" +
                "('서울캠퍼스행정처','담당','나현숙','02-2125-0116',''),\n" +
                "('서울캠퍼스행정처','담당','유용우','02-2125-0113',''),\n" +
                "('서울캠퍼스행정처','담당','김준혁','02-2125-0108','2201'),\n" +
                "('서울캠퍼스행정처','담당','김신규','02-2125-0129','2201'),\n" +
                "('서울캠퍼스행정처','담당','장보름','02-2125-0246','2201'),\n" +
                "('서울캠퍼스행정처','교역지도실장','김창주','02)2125-0104','Feb-03'),\n" +
                "('서울캠퍼스행정처','담당','조나단','02-2125-0106','2201'),\n" +
                "('서울캠퍼스행정처','국제교류협력실장','이서영','031-379-0374','2103'),\n" +
                "('서울캠퍼스행정처','담당','김준혁','02-2125-0108','2201'),\n" +
                "('서울캠퍼스행정처','생활관장(서울) 관장','김주한','02-2125-0140','6113'),\n" +
                "('서울캠퍼스행정처','담당','이광표','02-2125-0111','2201'),\n" +
                "('서울캠퍼스행정처','담당','장보름','02-2125-0246','2201'),\n" +
                "('대학원(경기)','대학원장(경기) 원장','노중기','031-379-0553','8424'),\n" +
                "('대학원(경기)','교학팀장','고영삼','031-379-0132','2108'),\n" +
                "('대학원(경기)','담당','이대회','031-379-0137','2108'),\n" +
                "('대학원(경기)','담당','최연주','031-379-0133','2108'),\n" +
                "('대학원(경기)','담당','하늘봄','031-379-0143','2108'),\n" +
                "('대학원(경기)','담당','장희지','031-379-0138','2108'),\n" +
                "('일반대학원(경기)','한국사학과주임교수','이형원','031-379-0446','8324'),\n" +
                "('일반대학원(경기)','담당','고정훈','031-379-0509','8105'),\n" +
                "('일반대학원(경기)','재활학과주임교수','백변경희','031-379-0526','8313'),\n" +
                "('일반대학원(경기)','담당','지다영','031-379-0510','8106'),\n" +
                "('일반대학원(경기)','담당','심주영','031-379-0510','8106'),\n" +
                "('일반대학원(경기)','심리학과주임교수','구훈정','031-379-0746','8318'),\n" +
                "('일반대학원(경기)','담당','이소윤','031-379-0550','8106'),\n" +
                "('일반대학원(경기)','담당','장희주','031-379-0500','8106'),\n" +
                "('일반대학원(경기)','정보통신학과주임교수','이용걸','031-379-0656','18506'),\n" +
                "('일반대학원(경기)','담당','박진용','031-379-0630','18421-1'),\n" +
                "('일반대학원(경기)','특수체육학과 주임교수','박상현','031-379-0519','8229'),\n" +
                "('일반대학원(경기)','유럽문화영상학과 주임교수','전춘명','031-379-0425','8217'),\n" +
                "('일반대학원(경기)','담당','윤수빈','031-379-0420','8105'),\n" +
                "('일반대학원(경기)','AI·SW학과 주임교수','서정욱','031-379-0662','18501'),\n" +
                "('일반대학원(경기)','담당','박진용','031-379-0630','18421-1'),\n" +
                "('일반대학원(경기)','국가와공공정책(협)주임교수','박상남','031-379-0496','8412'),\n" +
                "('일반대학원(경기)','담당','박우성','031-379-0530','8106'),\n" +
                "('일반대학원(경기)','기록관리학(협) 주임교수','이영남','031-379-0448','8325'),\n" +
                "('일반대학원(경기)','담당','고정훈','031-379-0509','8105'),\n" +
                "('일반대학원(경기)','디지털문화콘텐츠(협) 주임교수','이종현','031-379-0456','8420'),\n" +
                "('일반대학원(경기)','담당','고정훈','031-379-0509','8105'),\n" +
                "('일반대학원(경기)','빅데이터융합(협)주임교수','박성진','031-379-0641','18413'),\n" +
                "('일반대학원(경기)','담당','박진용','031-379-0630','18421-1'),\n" +
                "('일반대학원(경기)','IT영상데이터융합(협)주임교수','서정욱','031-379-0662','18501'),\n" +
                "('일반대학원(경기)','담당','박진용','031-379-0630','18421-1'),\n" +
                "('일반대학원(경기)','사회적경영(협) 주임교수','유한나','031-379-0488','8425'),\n" +
                "('일반대학원(경기)','담당','박우성','031-379-0530','8106'),\n" +
                "('일반대학원(경기)','e스포츠융합(협)주임교수','최은경','031-379-0665','3516'),\n" +
                "('일반대학원(경기)','담당','이소은','031-379-0650','18421-1'),\n" +
                "('교육대학원','원장','노중기','031-379-0671','2104'),\n" +
                "('교육대학원','교수','옥장흠','031-379-0672','8221'),\n" +
                "('교육대학원','부교수','김동심','031-379-0142','8223'),\n" +
                "('교육대학원','조교수','김애영','031-379-0609','8207'),\n" +
                "('교육대학원','조교수','백상현','031-379-0608','8321'),\n" +
                "('교육대학원','담당','하늘봄','031-379-0143','2108'),\n" +
                "('교육대학원','교육행정주임교수','옥장흠','031-379-0672','8221'),\n" +
                "('교육대학원','담당','하늘봄','031-379-0143','2108'),\n" +
                "('교육대학원','역사교육주임교수','정무용','031-379-0447','8329'),\n" +
                "('교육대학원','담당','하늘봄','031-379-0143','2108'),\n" +
                "('교육대학원','체육교육주임교수','명왕성','031-379-0627','10106'),\n" +
                "('교육대학원','담당','하늘봄','031-379-0143','2108'),\n" +
                "('교육대학원','상담심리주임교수','김동심','031-379-0142','8223'),\n" +
                "('교육대학원','담당','하늘봄','031-379-0143','2108'),\n" +
                "('교육대학원','한국어교육주임교수','유형동','031-379-0402','8203'),\n" +
                "('교육대학원','담당','하늘봄','031-379-0143','2108'),\n" +
                "('교육대학원','AI융합교육주임교수','김애영','031-379-0609','8207'),\n" +
                "('교육대학원','담당','하늘봄','031-379-0143','2108'),\n" +
                "('정신분석대학원','원장','노중기','031-379-0553','8424'),\n" +
                "('정신분석대학원','부교수','김태완','031-379-0667','8328'),\n" +
                "('정신분석대학원','담당','장희지','031-379-0138','2108'),\n" +
                "('정신분석대학원','심리학과주임교수','안도연','031-379-0749','8225'),\n" +
                "('정신분석대학원','담당','장희지','031-379-0138','2108'),\n" +
                "('정신분석대학원','아동교육상담학과주임교수','이경숙','031-379-0525','8308'),\n" +
                "('정신분석대학원','담당','장희지','031-379-0138','2108'),\n" +
                "('대학원(서울)','대학원장(서울)','전철','02-2125-0102','2213'),\n" +
                "('대학원(서울)','부처장','임충','02-2125-0107','2201'),\n" +
                "('대학원(서울)','교학행정팀장','황은영','02-2125-0109','2201'),\n" +
                "('대학원(서울)','기능주임','김영국','02-2125-0112','2102'),\n" +
                "('대학원(서울)','담당','나현숙','02-2125-0116','1201'),\n" +
                "('대학원(서울)','담당','유용우','02-2125-0113',''),\n" +
                "('대학원(서울)','담당','김준혁','02-2125-0108','2201'),\n" +
                "('대학원(서울)','교학행정팀 담당','김신규','02-2125-0129','2201'),\n" +
                "('대학원(서울)','담당','장보름','02-2125-0246','2201'),\n" +
                "('대학원(서울)','교학행정팀 담당','송실','02-2125-0246','2201'),\n" +
                "('대학원(서울)','담당','이광표','02-2125-0111','2201'),\n" +
                "('대학원(서울)','담당','조나단','02-2125-0106','2201'),\n" +
                "('대학원(서울)','일반연구원','이예림','02-2125-0126','4302'),\n" +
                "('대학원(서울)','신학과주임교수','이서영','02-2125-0105','2103'),\n" +
                "('대학원(서울)','목회신학(협)주임교수','이서영','02-2125-0105','2103'),\n" +
                "('대학원(서울)','사회혁신(협)주임교수','전병유','02-2125-0187','3158'),\n" +
                "('대학원(서울)','신학대학원장','전철','02-2125-0102','2231'),\n" +
                "('대학원(서울)','신학대학원교학부장','이서영','02-2125-0105','2103'),\n" +
                "('대학원(서울)','담당','황은영','02-2125-0109','2201'),\n" +
                "('대학원(서울)','담당','이광표','02-2125-0111','2201'),\n" +
                "('대학원(서울)','담당','조나단','02-2125-0106','2201'),\n" +
                "('대학원(서울)','담당','김준혁','02-2125-0108','2201'),\n" +
                "('대학원(서울)','사회적경영대학원장','이상헌','02-2125-0173','3518'),\n" +
                "('대학원(서울)','주임교수','전병유','02-2125-0187','3518'),\n" +
                "('대학원(서울)','교수','이일영','02-2125-0190','8302'),\n" +
                "('대학원(서울)','교수','이상헌','02-2125-0173','3518'),\n" +
                "('대학원(서울)','교수','이건범','02-2125-0183','8413'),\n" +
                "('대학원(서울)','부교수','이기호','02-2125-0189','3518'),\n" +
                "('대학원(서울)','조교수','유한나','031-379-0488','8425'),\n" +
                "('대학원(서울)','담당','황은영','02-2125-0109','2201'),\n" +
                "('대학원(서울)','담당','이광표','02-2125-0111','2201'),\n" +
                "('대학원(서울)','담당','김준혁','02-2125-0108','2201'),\n" +
                "('대학원(서울)','휴먼케어융합대학원장','백변경희','02-2125-0127','8313'),\n" +
                "('부속기관','중앙도서관장','문철수','031-379-0584','8426'),\n" +
                "('부속기관','팀장','이경미','031-379-0151','6422'),\n" +
                "('부속기관','담당','최미영','031-379-0152','6422'),\n" +
                "('부속기관','담당','양예희','031-379-0153','6422'),\n" +
                "('부속기관','담당','윤채린','031-379-0154','6422'),\n" +
                "('부속기관','담당','임지혜','031-379-0155','6422'),\n" +
                "('부속기관','담당','이은미','031-379-0159','6201'),\n" +
                "('부속기관','학술원장','노중기','031-379-0553','8424'),\n" +
                "('부속기관','담당','문경덕','031-379-0195','6204'),\n" +
                "('부속기관','출판부장','노중기','031-379-0553','8424'),\n" +
                "('부속기관','담당','이경미','031-379-0151','6422'),\n" +
                "('부속기관','박물관장','정해득','031-379-0449','8322'),\n" +
                "('부속기관','담당','문경덕','031-379-0195','6204'),\n" +
                "('부속기관','담당','박중국','031-379-0194','6204'),\n" +
                "('부속기관','기록정보관장','이영남','031-379-0448','8325'),\n" +
                "('부속기관','담당','문경덕','031-379-0195','6204'),\n" +
                "('부속기관','학보사주간','구훈정','031-379-0746','8318'),\n" +
                "('부속기관','담당','김동규','031-379-0036','1313'),\n" +
                "('부속기관','방송국주간','구훈정','031-379-0746','8318'),\n" +
                "('부속기관','담당','김동규','031-379-0036','1313'),\n" +
                "('부속기관','장공도서관장','전철','02-2125-0102','2213'),\n" +
                "('부속기관','장공도서관팀장','임충','02-2125-0107','2201'),\n" +
                "('부속기관','담당','김수남','02-2125-0131','3202'),\n" +
                "('부속기관','국제교류원장','김민환','031-379-0765','17303'),\n" +
                "('부속기관','부교수','PANG 니','031-379-0778','17302'),\n" +
                "('부속기관','조교수','정예수','031-379-0795','Jan-03'),\n" +
                "('부속기관','산학협력중점교수','정연수','031-379-0786','18306-2'),\n" +
                "('부속기관','국제교류원팀장','정승필','031-379-0210','6323'),\n" +
                "('부속기관','담당','손자람','031-379-0212','6323'),\n" +
                "('부속기관','일반연구원','허소연','031-379-0211','6323'),\n" +
                "('부속기관','담당','박주영','031-379-0215','6323'),\n" +
                "('부속기관','평생교육원(경기)원장','김대숙','031-379-0793','2303'),\n" +
                "('부속기관','담당','목윤아','031-379-0192','14102'),\n" +
                "('부속기관','담당','김은비','031-379-0189','14102'),\n" +
                "('부속기관','담당','김지수','031-379-0188','14102'),\n" +
                "('부속기관','일반연구원','김기수','031-379-0190','14102'),\n" +
                "('부속기관','평생교육원(서울)원장','백변경희','02-2125-0127','3101'),\n" +
                "('부설기관','민주사회정책연구원장','이기호','02-2125-0193','3158'),\n" +
                "('부설기관','산학협력단장','류승택','031-379-0644','18407'),\n" +
                "('부설기관','산학협력사업팀장','임병철','031-379-0241','18518'),\n" +
                "('부설기관','담당','김은정','031-379-0245','18518'),\n" +
                "('부설기관','담당','하은영','031-379-0243','18518'),\n" +
                "('부설기관','담당','한주연','031-379-0244','18518'),\n" +
                "('부설기관','담당','김대훈','031-379-0243','18518'),\n" +
                "('부설기관','창업지원팀 부교수','이미옥','031-379-0767','7210'),\n" +
                "('부설기관','창업지원팀장','임병철','031-379-0241','18518'),\n" +
                "('부설기관','지역발전센터부소장','김준혁','031-379-0439','3521'),\n" +
                "('부설기관','창업보육센터장','류승택','031-379-0644','18407'),\n" +
                "('부설기관','한신아동발달연구센터장','이경숙','031-379-0525','8308'),\n" +
                "('부설기관','중소기업산학협력센터장','류승택','031-379-0644','18407'),\n" +
                "('부설기관','지역발전센터장','류승택','031-379-0644','18407'),\n" +
                "('부설기관','지역발전센터팀장','임병철','031-379-0241','18518'),\n" +
                "('부설기관','한신어린이센터장','김상원','031-379-0790','8430'),\n" +
                "('부설기관','한신대학교종교와과학센터장','전철','02-2125-0171','2213'),\n" +
                "('부설기관','기업협업(ICC)센터장','류승택','031-379-0644','18407'),\n" +
                "('부설기관','일반연구원','구혜린','031-379-0846','9201'),\n" +
                "('부설기관','한반도평화학술원장','백준기','031-379-0493','8408'),\n" +
                "('부설기관','글로벌피스연구원','이종운','02-2125-0102','4001'),\n" +
                "('부설기관','평화와공공성센터장','이기호','02-2125-0193','Feb-02'),\n" +
                "('부설기관','정의평화생명518연구센터장','김민환','031-379-0765','17303'),\n" +
                "('부설기관','캠퍼스타운사업단(서울)단장','이기호','02-2125-0193','3518'),\n" +
                "('부설기관','팀장','임충','02-2125-0107','2201'),\n" +
                "('부설기관','선임급연구원','김민수','02-21250177','7307'),\n" +
                "('부설기관','일반연구원','전유나','02-2125-0178',''),\n" +
                "('부설기관','일반연구원','정희영','02-2125-0178',''),\n" +
                "('부설기관','휴먼케어서비스센터장','백변경희','031-379-0526','8313'),\n" +
                "('부설기관','조교수','김용훈','031-379-0524','3401'),\n" +
                "('부설기관','조교수','박상현','031-379-0519','8229'),\n" +
                "('부설기관','일반연구원','한승아','031-379-0858','1315'),\n" +
                "('부설기관','임상심리연구센터장','안도연','031-379-0749','8225'),\n" +
                "('부설기관','실습조교','조준형','031-379-0201','Jan-06'),\n" +
                "('부설기관','실습조교','최미란','031-379-0201','Jan-06'),\n" +
                "('부설기관','실습조교','박민이','031-379-0202','Jan-06'),\n" +
                "('부설기관','실습조교','구교성','031-379-0202','Jan-06'),\n" +
                "('부설기관','교학협력센터장','전철','02-2125-0102','2213'),\n" +
                "('전노조한신대지부','전국대학노동조합한신대학교지부 지부장','유두영','031-379-0271~2','3105'),\n" +
                "('전노조한신대지부','전국대학노동조합한신대학교지부 부지부장','김기중','031-379-0024','8104'),\n" +
                "('전노조한신대지부','전국대학노동조합한신대학교지부 사무국장','강현우','031-379-0027','1102'),\n" +
                "('전노조한신대지부','전국대학노동조합한신대학교지부 집행부장','김영국','02-2125-0112','2102'),\n" +
                "('전노조한신대지부','전국대학노동조합한신대학교지부 집행부장','김정현','031-379-0851','1315'),\n" +
                "('전노조한신대지부','전국대학노동조합한신대학교지부 집행부장','전세정','031-379-0088','6424'),\n" +
                "('전노조한신대지부','전국대학노동조합한신대학교지부 집행부장','진민걸','031-379-0754','20309'),\n" +
                "('전노조한신대지부','전국대학노동조합한신대학교지부 집행부장','류수윤','031-379-0025','8104'),\n" +
                "('전노조한신대지부','전국대학노동조합한신대학교지부 집행부장','정준훈','031-379-0125','18305'),\n" +
                "('교수협의회','대표의장','정해득','031-379-0449','8322'),\n" +
                "('교수협의회','공동의장','홍창기','031-379-0498','7209'),\n" +
                "('교수협의회','집행위원','정예수','031-379-0795','Jan-03'),\n" +
                "('교수협의회','집행위원','이용걸','031-379-0656','18506'),\n" +
                "('교수협의회','집행위원','안도연','031-379-0749','8225'),\n" +
                "('교수협의회','집행위원','이서영','031-379-0374','2103'),\n" +
                "('교수협의회','총무','윤건','031-379-0739','8325'),\n" +
                "('직장예비군대대(오산)','대대장','권기민','031-379-0276','10102'),\n" +
                "('한신학원','이사장','박유철','031-379-0005','1209'),\n" +
                "('한신학원','국장','전형금','031-379-0006','1208'),\n" +
                "('한신학원','과장','김다혜','031-379-0007','1208'),\n" +
                "('총동문회','내선','총동문회','031-379-0274','17307'),\n" +
                "('총동문회','직통','총동문회','031-372-8529','17307'),\n" +
                "('학생기구','','총학생회','031-379-0327','5206'),\n" +
                "('학생기구','','학생복지위원회','031-379-0328','5203'),\n" +
                "('학생기구','','동아리연합회','031-379-0332','5305'),\n" +
                "('학생기구','','대학원총학생회','031-379-0333','2401'),\n" +
                "('명예교수실','필헌관','명예교수실','031-379-0399','2106'),\n" +
                "('강사휴게실','강사휴게실','만우관강사휴게실','031-379-0282','3421'),\n" +
                "('강사휴게실','강사휴게실','실습동강사휴게실','031-379-0283','9102'),\n" +
                "('우편취급국','우편취급국','이미숙','031-379-0285','17102'),\n" +
                "('우편취급국','우편취급국','서정원','031-379-0268','17102');";
        return sql;
    }
    public String insertProfessor(){
        String sql = "REPLACE INTO ProfessorCallNumbers (affiliation, name, CallNumber, officeNumber) VALUES\n" +
                "('신학대학','이영미','031-379-0383','2102'),\n" +
                "('신학대학','방진현','031-379-0460','8105'),\n" +
                "('신학대학','윤수빈','031-379-0420','8105'),\n" +
                "('신학대학','김희선','031-379-0372','2102'),\n" +
                "('신학대학','강성영','031-379-0375','1202'),\n" +
                "('신학대학','연규홍','031-379-0379','2303-3'),\n" +
                "('신학대학','이영미','031-379-0383','2102'),\n" +
                "('신학대학','김주한','031-379-0373','2105'),\n" +
                "('신학대학','김창주','031-379-0388','2303-3'),\n" +
                "('신학대학','전철','031-379-0389','2103'),\n" +
                "('신학대학','이서영','031-379-0374','2103'),\n" +
                "('인문융합대학','신광철','031-379-0476','8211'),\n" +
                "('인문융합대학','신광철','031-379-0476','8211'),\n" +
                "('인문융합대학','정무용','031-379-0447','8329'),\n" +
                "('인문융합대학','이종현','031-379-0456','8420'),\n" +
                "('인문융합대학','김지혜','031-379-0405','8416'),\n" +
                "('인문융합대학','백수린','031-379-0485','8307'),\n" +
                "('인문융합대학','윤수빈','031-379-0420','8106'),\n" +
                "('인문융합대학','황지윤','031-379-0390','8105'),\n" +
                "('인문융합대학','조수빈','031-379-0580','8105'),\n" +
                "('인문융합대학','서지영','031-379-0570','8105'),\n" +
                "('인문융합대학','고정훈','031-379-0509','8105'),\n" +
                "('인문융합대학','유형동','031-379-0402','8203'),\n" +
                "('인문융합대학','유문선','031-379-0404','8228'),\n" +
                "('인문융합대학','김수영','031-379-0403','8204'),\n" +
                "('인문융합대학','김지혜','031-379-0405','8416'),\n" +
                "('인문융합대학','황지윤','031-379-0390','8105'),\n" +
                "('인문융합대학','김지혜','031-379-0412','8218'),\n" +
                "('인문융합대학','박미선','031-379-0417','8216'),\n" +
                "('인문융합대학','이향미','031-379-0419','8212'),\n" +
                "('인문융합대학','LUCIER PETER','031-379-0418','8111'),\n" +
                "('인문융합대학','윤수빈','031-379-0420','8106'),\n" +
                "('인문융합대학','전춘명','031-379-0425','8217'),\n" +
                "('인문융합대학','오동식','031-379-0428','8209'),\n" +
                "('인문융합대학','심혜경','031-379-0426','3401-1'),\n" +
                "('인문융합대학','윤수빈','031-379-0420','8106'),\n" +
                "('인문융합대학','장정해','031-379-0474','8213'),\n" +
                "('인문융합대학','김남희','031-379-0443','8317'),\n" +
                "('인문융합대학','김남희','031-379-0443','8317'),\n" +
                "('인문융합대학','류기수','031-379-0475','8215'),\n" +
                "('인문융합대학','최민성','031-379-0477','8214'),\n" +
                "('인문융합대학','문미진','031-379-0478','7311'),\n" +
                "('인문융합대학','고정훈','031-379-0509','8105'),\n" +
                "('인문융합대학','김희정','031-379-0435','8301'),\n" +
                "('인문융합대학','김대오','031-379-0434','8330'),\n" +
                "('인문융합대학','강인철','031-379-0453','8309'),\n" +
                "('인문융합대학','서지영','031-379-0570','8105'),\n" +
                "('인문융합대학','이형원','031-379-0446','8324'),\n" +
                "('인문융합대학','정해득','031-379-0449','8322'),\n" +
                "('인문융합대학','이영남','031-379-0448','8325'),\n" +
                "('인문융합대학','정무용','031-379-0447','8329'),\n" +
                "('인문융합대학','고정훈','031-379-0509','8105'),\n" +
                "('인문융합대학','강지희','031-379-0467','8423'),\n" +
                "('인문융합대학','정한아','031-379-0466','8219'),\n" +
                "('인문융합대학','황지윤','031-379-0390','8105'),\n" +
                "('인문융합대학','김윤성','031-379-0455','8320'),\n" +
                "('인문융합대학','신광철','031-379-0476','8211'),\n" +
                "('인문융합대학','이종현','031-379-0456','8420'),\n" +
                "('인문융합대학','고정훈','031-379-0590','8105'),\n" +
                "('인문융합대학','최민성','031-379-0213','8214'),\n" +
                "('글로벌융합대학','이일영','031-379-0533','8302'),\n" +
                "('글로벌융합대학','이일영','031-379-0533','8302'),\n" +
                "('글로벌융합대학','노승철','031-379-0842','8431'),\n" +
                "('글로벌융합대학','정희진','031-379-0565','8202'),\n" +
                "('글로벌융합대학','한재명','031-379-0503','8429'),\n" +
                "('글로벌융합대학','박세현','031-379-0560','8106'),\n" +
                "('글로벌융합대학','최형서','031-379-0490','8106'),\n" +
                "('글로벌융합대학','박우성','031-379-0530','8106'),\n" +
                "('글로벌융합대학','성락선','031-379-0506','8331'),\n" +
                "('글로벌융합대학','양우진','031-379-0505','8409'),\n" +
                "('글로벌융합대학','최형서','031-379-0490','8106'),\n" +
                "('글로벌융합대학','박우성','031-379-0530','8106'),\n" +
                "('글로벌융합대학','전창환','031-379-0566','8410'),\n" +
                "('글로벌융합대학','정지영','031-379-0568','8419'),\n" +
                "('글로벌융합대학','정희진','031-379-0565','8202'),\n" +
                "('글로벌융합대학','박우성','031-379-0530','8106'),\n" +
                "('글로벌융합대학','박세현','031-379-0560','8106'),\n" +
                "('글로벌융합대학','김도희','031-379-0534','8208'),\n" +
                "('글로벌융합대학','이일영','031-379-0533','8302'),\n" +
                "('글로벌융합대학','주장환','031-379-0535','8405'),\n" +
                "('글로벌융합대학','황선미','031-379-0537','7311'),\n" +
                "('글로벌융합대학','박우성','031-379-2530','8106'),\n" +
                "('글로벌융합대학','박세현','031-379-0560','8106'),\n" +
                "('글로벌융합대학','오미정','031-379-0545','8422'),\n" +
                "('글로벌융합대학','하종문','031-379-0542','8316'),\n" +
                "('글로벌융합대학','송주명','031-379-0544','8306'),\n" +
                "('글로벌융합대학','도키와유코','031-379-0547','17305'),\n" +
                "('글로벌융합대학','나카무라모모코','031-379-0548','17305'),\n" +
                "('글로벌융합대학','박우성','031-379-0530','8106'),\n" +
                "('글로벌융합대학','박세현','031-379-0560','8106'),\n" +
                "('글로벌융합대학','백준기','031-379-0493','8408'),\n" +
                "('글로벌융합대학','이해영','031-379-0492','8401'),\n" +
                "('글로벌융합대학','조성대','031-379-0494','8310'),\n" +
                "('글로벌융합대학','최형익','031-379-0495','8428'),\n" +
                "('글로벌융합대학','박상남','031-379-0496','8412'),\n" +
                "('글로벌융합대학','최형서','031-379-0490','8106'),\n" +
                "('글로벌융합대학','박우성','031-379-0530','8106'),\n" +
                "('글로벌융합대학','노승철','031-379-0842','8431'),\n" +
                "('글로벌융합대학','이석민','031-379-0738','8303'),\n" +
                "('글로벌융합대학','윤건','031-379-0739','8325'),\n" +
                "('글로벌융합대학','최형서','031-379-0490','8106'),\n" +
                "('글로벌융합대학','박우성','031-379-0530','8106'),\n" +
                "('휴먼서비스대학','박상현','031-379-0519','8229'),\n" +
                "('휴먼서비스대학','홍선미','031-379-0514','8311'),\n" +
                "('휴먼서비스대학','홍선미','031-379-0514','8311'),\n" +
                "('휴먼서비스대학','김용훈','031-379-0524','3401'),\n" +
                "('휴먼서비스대학','김상원','031-379-0790','8430'),\n" +
                "('휴먼서비스대학','장희주','031-379-0500','8106'),\n" +
                "('휴먼서비스대학','심주영','031-379-0510','8106'),\n" +
                "('휴먼서비스대학','지다영','031-379-0510','8106'),\n" +
                "('휴먼서비스대학','이소윤','031-379-0550','8106'),\n" +
                "('휴먼서비스대학','윤상철','031-379-0555','8402'),\n" +
                "('휴먼서비스대학','노중기','031-379-0553','8424'),\n" +
                "('휴먼서비스대학','김종엽','031-379-0554','8421'),\n" +
                "('휴먼서비스대학','이소윤','031-379-0550','8106'),\n" +
                "('휴먼서비스대학','장희주','031-379-0500','8106'),\n" +
                "('휴먼서비스대학','김예량','031-379-0518','8312'),\n" +
                "('휴먼서비스대학','홍선미','031-379-0514','8311'),\n" +
                "('휴먼서비스대학','주경희','031-379-0516','8315'),\n" +
                "('휴먼서비스대학','장익현','031-379-0517','8314'),\n" +
                "('휴먼서비스대학','김수경','031-379-0512','8319'),\n" +
                "('휴먼서비스대학','김은영','031-379-0515','17309'),\n" +
                "('휴먼서비스대학','심주영','031-379-0510','8106'),\n" +
                "('휴먼서비스대학','지다영','031-379-0510','8106'),\n" +
                "('휴먼서비스대학','장희주','031-379-0500','8106'),\n" +
                "('휴먼서비스대학','이경숙','031-379-0525','8308'),\n" +
                "('휴먼서비스대학','이인재','031-379-0523','8403'),\n" +
                "('휴먼서비스대학','백변경희','031-379-0526','8313'),\n" +
                "('휴먼서비스대학','남세현','031-379-0527','8206'),\n" +
                "('휴먼서비스대학','심주영','031-379-0510','8106'),\n" +
                "('휴먼서비스대학','지다영','031-379-0510','8106'),\n" +
                "('휴먼서비스대학','장희주','031-379-0500','8106'),\n" +
                "('휴먼서비스대학','구훈정','031-379-0746','8318'),\n" +
                "('휴먼서비스대학','구훈정','031-379-0746','8318'),\n" +
                "('휴먼서비스대학','오현숙','031-379-0671','8224'),\n" +
                "('휴먼서비스대학','유희정','031-379-0748','8323'),\n" +
                "('휴먼서비스대학','김영주','031-379-0789','3401-1'),\n" +
                "('휴먼서비스대학','안도연','031-379-0749','8225'),\n" +
                "('휴먼서비스대학','김상원','031-379-0790','8430'),\n" +
                "('휴먼서비스대학','이소윤','031-379-0550','8106'),\n" +
                "('휴먼서비스대학','장희주','031-379-0500','8106'),\n" +
                "('휴먼서비스대학','박상현','031-379-0519','8229'),\n" +
                "('휴먼서비스대학','박상현','031-379-0519','8229'),\n" +
                "('휴먼서비스대학','조규청','031-379-0626','8222'),\n" +
                "('휴먼서비스대학','명왕성','031-379-0627','10106'),\n" +
                "('휴먼서비스대학','지다영','031-379-0510','8106'),\n" +
                "('휴먼서비스대학','장희주','031-379-0500','8106'),\n" +
                "('경영·미디어대학','이건범','031-379-0576','8413'),\n" +
                "('경영·미디어대학','이건범','031-379-0576','8413'),\n" +
                "('경영·미디어대학','홍창기','031-379-0498','7209'),\n" +
                "('경영·미디어대학','최지환','031-379-0577','8407'),\n" +
                "('경영·미디어대학','유현미','031-379-0573','8415'),\n" +
                "('경영·미디어대학','윤수빈','031-379-0420','8105'),\n" +
                "('경영·미디어대학','서지영','031-379-0570','8105'),\n" +
                "('경영·미디어대학','황지윤','031-379-0390','8105'),\n" +
                "('경영·미디어대학','조수빈','031-379-0580','8105'),\n" +
                "('경영·미디어대학','방진현','031-379-0460','8105'),\n" +
                "('경영·미디어대학','고정훈','031-379-0509','8105'),\n" +
                "('경영·미디어대학','김경민','031-379-0482','8226'),\n" +
                "('경영·미디어대학','이건범','031-379-0576','8413'),\n" +
                "('경영·미디어대학','박규호','031-379-0487','8414'),\n" +
                "('경영·미디어대학','김병조','031-379-0485','8418'),\n" +
                "('경영·미디어대학','정의범','031-379-0572','8417'),\n" +
                "('경영·미디어대학','손정현','031-379-0673','3420'),\n" +
                "('경영·미디어대학','서지영','031-379-0570','8105'),\n" +
                "('경영·미디어대학','방진현','031-379-0460','8105'),\n" +
                "('경영·미디어대학','이남연','031-379-0595','7309'),\n" +
                "('경영·미디어대학','김상욱','031-379-0593','7308'),\n" +
                "('경영·미디어대학','황지윤','031-379-0390','8105'),\n" +
                "('경영·미디어대학','방진현','031-379-0460','8105'),\n" +
                "('경영·미디어대학','양영수','031-379-0489','8205-1'),\n" +
                "('경영·미디어대학','장종익','031-379-0486','8227'),\n" +
                "('경영·미디어대학','윤수빈','031-379-0420','8105'),\n" +
                "('경영·미디어대학','유한나','031-379-0488','8425'),\n" +
                "('경영·미디어대학','방진현','031-379-0460','8105'),\n" +
                "('경영·미디어대학','박혜영','031-379-0583','8427'),\n" +
                "('경영·미디어대학','문철수','031-379-0584','8426'),\n" +
                "('경영·미디어대학','채희상','031-379-0587','8406'),\n" +
                "('경영·미디어대학','지원배','031-379-0586','8411'),\n" +
                "('경영·미디어대학','이성미','031-379-0578','8404'),\n" +
                "('경영·미디어대학','조수빈','031-379-0580','8106'),\n" +
                "('경영·미디어대학','방진현','031-379-0460','8105'),\n" +
                "('이공계융합대학','강민구','031-379-0658','18511'),\n" +
                "('이공계융합대학','장재웅','031-379-0603','7207'),\n" +
                "('이공계융합대학','윤사랑','031-379-0628','18421-1'),\n" +
                "('이공계융합대학','이소정','031-379-0649','18421-1'),\n" +
                "('이공계융합대학','김가현','031-379-0631','18421-1'),\n" +
                "('이공계융합대학','안희연','031-379-0607','18421-1'),\n" +
                "('이공계융합대학','양춘우','031-379-0605','7314'),\n" +
                "('이공계융합대학','박기현','031-379-0604','7204'),\n" +
                "('이공계융합대학','윤성식','031-379-0606','7310'),\n" +
                "('이공계융합대학','정재웅','031-379-0603','7207'),\n" +
                "('이공계융합대학','이소정','031-379-0649','18421-1'),\n" +
                "('이공계융합대학','안희연','031-379-0607','18421-1'),\n" +
                "('이공계융합대학','윤사랑','031-379-0628','18421-1'),\n" +
                "('이공계융합대학','유한나','031-379-0612','7206'),\n" +
                "('이공계융합대학','박동련','031-379-0615','7205'),\n" +
                "('이공계융합대학','변종석','031-379-0616','7208'),\n" +
                "('이공계융합대학','이소정','031-379-0649','18421-1'),\n" +
                "('이공계융합대학','안희연','031-379-0607','18421-1'),\n" +
                "('이공계융합대학','윤사랑','031-379-0628','18421-1'),\n" +
                "('AI·SW대학','강민구','031-379-0658','18511'),\n" +
                "('AI·SW대학','이형우','031-379-0642','18406'),\n" +
                "('AI·SW대학','정승민','031-379-0661','18507'),\n" +
                "('AI·SW대학','윤효석','031-379-0645','18401'),\n" +
                "('AI·SW대학','안현','031-379-0663','18509'),\n" +
                "('AI·SW대학','서정욱','031-379-0662','18501'),\n" +
                "('AI·SW대학','임익수','031-379-0637','18414'),\n" +
                "('AI·SW대학','이용걸','031-379-0656','18506'),\n" +
                "('AI·SW대학','이용걸','031-379-0656','18506'),\n" +
                "('AI·SW대학','안현','031-379-0663','18509'),\n" +
                "('AI·SW대학','임익수','031-379-0637','18414'),\n" +
                "('AI·SW대학','나근식','031-379-0633','18402'),\n" +
                "('AI·SW대학','김성기','031-379-0635','18404'),\n" +
                "('AI·SW대학','최창원','031-379-0638','18411'),\n" +
                "('AI·SW대학','장재건','031-379-0636','18405'),\n" +
                "('AI·SW대학','김현경','031-379-0654','18514'),\n" +
                "('AI·SW대학','강영경','031-379-0639','18415'),\n" +
                "('AI·SW대학','이성구','031-379-0640','18412'),\n" +
                "('AI·SW대학','박성진','031-379-0641','18413'),\n" +
                "('AI·SW대학','이경옥','031-379-0657','18512'),\n" +
                "('AI·SW대학','조성호','031-379-0660','18510'),\n" +
                "('AI·SW대학','손승일','031-379-0659','18505'),\n" +
                "('AI·SW대학','류승택','031-379-0644','18407'),\n" +
                "('AI·SW대학','나경욱','031-379-0670','3521'),\n" +
                "('AI·SW대학','여협구','031-379-0664','18508'),\n" +
                "('AI·SW대학','이양선','031-379-0632','18410'),\n" +
                "('AI·SW대학','백수진','031-379-0655','18513'),\n" +
                "('AI·SW대학','박기홍','031-379-0643','18408'),\n" +
                "('AI·SW대학','김가현','031-379-0631','18421-1'),\n" +
                "('AI·SW대학','윤사랑','031-379-0607','18421-1'),\n" +
                "('AI·SW대학','이소정','031-379-0649','18421-1'),\n" +
                "('AI·SW대학','이소은','031-379-0650','18421-1'),\n" +
                "('AI·SW대학','박진용','031-379-0630','18421-1'),\n" +
                "('평화교양대학','이상헌','031-379-0670','3521'),\n" +
                "('평화교양대학','신광철','031-379-0476','8211'),\n" +
                "('평화교양대학','노중기','031-379-0553','8424'),\n" +
                "('평화교양대학','정태성','031-379-0666','18515'),\n" +
                "('평화교양대학','나경욱','031-379-0670','3521'),\n" +
                "('평화교양대학','차윤정','031-379-0668','3517'),\n" +
                "('평화교양대학','전병유','031-379-0724','3518'),\n" +
                "('평화교양대학','오동식','031-379-0428','8209'),\n" +
                "('평화교양대학','이기호','031-379-0721','3518'),\n" +
                "('평화교양대학','이은정','031-379-0768','17306'),\n" +
                "('평화교양대학','서경희','031-379-0766','17308'),\n" +
                "('평화교양대학','유동석','031-379-0782','18306'),\n" +
                "('평화교양대학','고기영','031-379-0780','18306'),\n" +
                "('평화교양대학','김수겸','031-379-0792','10106'),\n" +
                "('평화교양대학','강석원','031-379-0787','18305-1'),\n" +
                "('평화교양대학','김흥기','031-379-0785','7209'),\n" +
                "('평화교양대학','송주현','031-379-0761','17301'),\n" +
                "('평화교양대학','김준혁','031-379-0439','3520'),\n" +
                "('평화교양대학','고우경','031-379-0436','17301'),\n" +
                "('평화교양대학','전철','031-379-0389','3518'),\n" +
                "('평화교양대학','정해득','031-379-0449','8322'),\n" +
                "('평화교양대학','이익주','031-379-0784','18306-1'),\n" +
                "('평화교양대학','공주형','031-379-0772','7212'),\n" +
                "('평화교양대학','김민환','031-379-0765','17303'),\n" +
                "('평화교양대학','심주연','031-379-0674','7313'),\n" +
                "('평화교양대학','이미옥','031-379-0767','7210'),\n" +
                "('평화교양대학','유은하','031-379-0729','8305'),\n" +
                "('평화교양대학','이종운','031-379-0730','8305'),\n" +
                "('평화교양대학','염동호','031-379-0788','2303'),\n" +
                "('평화교양대학','김계자','031-379-0731','8327'),\n" +
                "('평화교양대학','매튜','031-379-0438','17304'),\n" +
                "('평화교양대학','모리스','031-379-0759','17101-1'),\n" +
                "('평화교양대학','야니','031-379-0760','17304'),\n" +
                "('평화교양대학','박종현','031-379-0796','18502'),\n" +
                "('평화교양대학','정예수','031-379-0795','2303-1'),\n" +
                "('평화교양대학','허태윤','031-379-0797','2303-1'),\n" +
                "('평화교양대학','김태완','031-379-0667','8328'),\n" +
                "('평화교양대학','김대숙','031-379-0793','2303'),\n" +
                "('평화교양대학','박선화','031-379-0794','17309'),\n" +
                "('평화교양대학','진형섭','031-379-0779','17103'),\n" +
                "('평화교양대학','강은미','031-379-0781','17309'),\n" +
                "('평화교양대학','최은경','031-379-0665','3516'),\n" +
                "('평화교양대학','유영국','031-379-0677','7312'),\n" +
                "('평화교양대학','박용수','031-379-0762','18305'),\n" +
                "('평화교양대학','최성혜','031-379-0732','8107'),\n" +
                "('평화교양대학','엄태선','031-379-0733','8107'),\n" +
                "('마이크로전공','윤건','031-379-0739','8325'),\n" +
                "('마이크로전공','전춘명','031-379-0425','8217'),\n" +
                "('마이크로전공','성락선','031-379-0506','8331'),\n" +
                "('마이크로전공','전병유','031-379-0724','3518'),\n" +
                "('마이크로전공','염동호','031-379-0788','2303'),\n" +
                "('마이크로전공','최민성','031-379-0477','8214'),\n" +
                "('마이크로전공','안현','031-379-0663','18509'),\n" +
                "('마이크로전공','서정욱','031-379-0662','18501'),\n" +
                "('마이크로전공','박상남','031-379-0496','8412'),\n" +
                "('마이크로전공','이석민','031-379-0738','8303'),\n" +
                "('마이크로전공','박상남','031-379-0496','8412'),\n" +
                "('연계전공','김동심','031-379-0142','8223'),\n" +
                "('연계전공','이소윤','031-379-0550','8105'),\n" +
                "('연계전공','박규호','031-379-0487','8416'),\n" +
                "('연계전공','박세현','031-379-0560','8106'),\n" +
                "('연계전공','장종익','031-379-0486','8227'),\n" +
                "('연계전공','박순빈','031-379-0460','8105'),\n" +
                "('연계전공','유한나','031-379-0612','7206'),\n" +
                "('연계전공','안희연','031-379-0607','18421-1');";
        return sql;
    }

}