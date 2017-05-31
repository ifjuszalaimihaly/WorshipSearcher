package com.worshipsearcher.dbloader;

/**
 * Created by Mihaly on 2015.04.24..
 */
public class DbConstants {

    public static final String DATABASE_NAME = "data.db";
    public static final int DATABASE_VERSION = 1;



    public static class Churches {
        public static final String DATABASE_TABLE = "churches";

        public static final String CHURCHID = "churchid";
        public static final String CONID = "conid";
        public static final String CITY = "city";
        public static final String ADDRESS = "address";
        public static final String LAT = "lat";
        public static final String LON = "lon";
        public static final String COMMENT = "comment";

        public static final String DATABASE_CREATE = "create table if not exists "
                + DATABASE_TABLE
                + " ( "
                + CHURCHID
                + " smallint primary key, "
                + CONID
                + " intger not null, "
                + CITY
                + " text not null, "
                + ADDRESS
                + " text not null, "
                + LAT
                + " real not null, "
                + LON
                + " real not null, "
                + COMMENT
                + " text"
                + "); ";

        public static final String DATABASE_DROP = "drop table if exists "
                + DATABASE_TABLE + "; ";
    }

    public static class Worships {
        public static final String DATABASE_TABLE = "worships";
        public static final String WORSHIPID = "worshipid";
        public static final String CHURCHID = "churchid";

        public static final String TERMIN = "termin";
        public static final String COMMENT = "comment";
        public static final String WEEKID = "weekid";

        public static final String DATABASE_CREATE = "create table if not exists "
                + DATABASE_TABLE
                + " ( "
                + WORSHIPID
                + " integer primary key, "
                + CHURCHID
                + " smallint, "
                + TERMIN
                + " datetime, "
                + WEEKID
                + " integer, "
                + COMMENT
                + " text, "
                + "unique("
                + CHURCHID
                + ", "
                + TERMIN
                + ", "
                + WEEKID
                + "), "

                + "foreign key("
                + WEEKID
                + ") references "
                + Weeks.DATABASE_TABLE
                + "("
                + Weeks.WEEKID
                + "),"
                + "foreign key("
                + CHURCHID
                + ") references "
                + Churches.DATABASE_TABLE
                + "("
                + Churches.CHURCHID + "));";

        public static final String DATABASE_DROP = "drop table if exists "
                + DATABASE_TABLE + "; ";

    }

    public static class Weeks {
        public static final String DATABASE_TABLE = "weeks";
        public static final String WEEKID = "weekid";
        public static final String TYPE = "type";
        public static final String DATABASE_CREATE = "create table if not exists "
                + DATABASE_TABLE
                + " ( "
                + WEEKID
                + " tinyint primary key, "
                + TYPE
                + " text not null" + "); ";
        public static final String DATABASE_DROP = "drop table if exists "
                + DATABASE_TABLE + "; ";
    }

    public static final class UpdatedTime{
        public static final String DATABASE_TABLE = "updated";
        public static final String ID = "id";
        public static final String LAST_MODIFIED = "lastmodified";
        public static final String DATABASE_CREATE = "create table if not exists " + DATABASE_TABLE + " ( "
                + ID + " integer primary key, " + LAST_MODIFIED + " text ); ";

    }

}
