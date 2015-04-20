package com.muscleweather.app.db;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.muscleweather.app.model.City;
import com.muscleweather.app.model.Country;
import com.muscleweather.app.model.Province;


public class MuscleWeatherDB {
	
	public static final String DB_NAME = "muscle_weather";
	
	public static final int VERSION = 1;
	
	private static MuscleWeatherDB muscleWeatherDB;
	
	private SQLiteDatabase db;
	
	private MuscleWeatherDB(Context context) {
		MuscleWeatherOpenHelper dbHelper = new MuscleWeatherOpenHelper(context, 
				DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	/**
	 * 获得MuscleWeatherDB实例
	 */
	public synchronized static MuscleWeatherDB getInstance(Context context) {
		if (muscleWeatherDB == null) {
			muscleWeatherDB = new MuscleWeatherDB(context);
		}
		return muscleWeatherDB;
	}
	
	/**
	 * 保存Province实例到数据库
	 */
	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}
	
	/**
	 * 加载全国所有省份信息
	 */
	public List<Province> loadProvince() {
		List<Province> provinces = new ArrayList<Province>();
		Cursor cursor = db.rawQuery("selct * from Province", null);
		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor
						.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor
						.getColumnIndex("province_cod")));
				provinces.add(province);
			} while (cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
		}
		return provinces;
	}
	
	/**
	 * 将city实例保存到数据库
	 */
	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}
	
	/**
	 * 获取某省下所有的城市信息
	 */
	public List<City> loadCities(int provinceId) {
		List<City> cities = new ArrayList<City>();
		Cursor cursor = db.rawQuery("select * from City where province_id = ?", new String[]{String.valueOf(provinceId)});
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				cities.add(city);
			} while(cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
		}
		return cities;
	}
	
	/**
	 * 保存country实例到数据库
	 */
	public void saveCountry(Country country) {
		ContentValues values = new ContentValues();
		values.put("country_name", country.getCountryName());
		values.put("country_code", country.getCountryCode());
		values.put("city_id", country.getCityId());
		db.insert("Country", null, values);
	}
	
	/**
	 * 加载某城市下所有区县信息
	 */
	public List<Country> loadCountry(int cityId) {
		List<Country> countries = new ArrayList<Country>();
		Cursor cursor = db.rawQuery("select * from Country where city_id = ?", new String[]{String.valueOf(cityId)});
		if(cursor.moveToFirst()) {
			do {
				Country country = new Country();
				country.setId(cursor.getInt(cursor.getColumnIndex("id")));
				country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
				country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
				country.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
			}while(cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
		}
		return countries;
	}
}
