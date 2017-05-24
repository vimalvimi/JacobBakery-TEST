package com.roxybakestudio.jacobbakery.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "baking.db";

    private static final int DATABASE_VERSION = 2;

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_RECIPE_TABLE =
                "CREATE TABLE " + RecipeContract.RecipeMain.TABLE_NAME + " (" +
                        RecipeContract.RecipeMain._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RecipeContract.RecipeMain.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                        RecipeContract.RecipeMain.COLUMN_NAME + " TEXT NOT NULL, " +
                        RecipeContract.RecipeMain.COLUMN_SERVINGS + " INTEGER NOT NULL);";

        final String SQL_CREATE_INGREDIENTS_TABLE =
                "CREATE TABLE " + RecipeContract.RecipeIngredients.TABLE_NAME + " (" +
                        RecipeContract.RecipeIngredients._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RecipeContract.RecipeIngredients.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                        RecipeContract.RecipeIngredients.COLUMN_RECIPE_NAME + " STRING NOT NULL, " +
                        RecipeContract.RecipeIngredients.COLUMN_QUANTITY + " REAL NOT NULL, " +
                        RecipeContract.RecipeIngredients.COLUMN_MEASURE + " TEXT NOT NULL, " +
                        RecipeContract.RecipeIngredients.COLUMN_INGREDIENT + " TEXT NOT NULL);";

        final String SQL_CREATE_STEPS_TABLE =
                "CREATE TABLE " + RecipeContract.RecipeSteps.TABLE_NAME + " (" +
                        RecipeContract.RecipeSteps._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RecipeContract.RecipeSteps.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                        RecipeContract.RecipeSteps.COLUMN_STEP_ID + " INTEGER NOT NULL, " +
                        RecipeContract.RecipeSteps.COLUMN_SHORT_DESCRIPTION + " TEXT, " +
                        RecipeContract.RecipeSteps.COLUMN_DESCRIPTION + " TEXT, " +
                        RecipeContract.RecipeSteps.COLUMN_VIDEO + " TEXT);";

        db.execSQL(SQL_CREATE_RECIPE_TABLE);
        db.execSQL(SQL_CREATE_INGREDIENTS_TABLE);
        db.execSQL(SQL_CREATE_STEPS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeMain.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeIngredients.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeSteps.TABLE_NAME);
        onCreate(db);
    }
}
