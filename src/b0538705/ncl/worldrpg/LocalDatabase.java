package b0538705.ncl.worldrpg;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocalDatabase extends SQLiteOpenHelper {
	
	
	public static String CREATE_TABLE_POINTS =
			"create table POINTS( _id integer primary key,"
			+ "latitude text not null,"
			+ "longitude text not null,"
			+ "normal integer not null,"
			+ "infected integer not null);";

	public LocalDatabase(Context context)
	{
		super(context,"worldrpg.db", null, 1);
		
		//quick init
		this.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//create all the tables needed
				try{
					db.execSQL(CREATE_TABLE_POINTS);
				}catch(SQLException e)
				{
					Log.e("worldrpg-database",e.getMessage());
				}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
