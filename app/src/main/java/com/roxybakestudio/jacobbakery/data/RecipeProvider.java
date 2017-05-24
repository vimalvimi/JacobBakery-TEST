package com.roxybakestudio.jacobbakery.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class RecipeProvider extends ContentProvider {

    public static final int CODE_RECIPES = 100;
    public static final int CODE_RECIPES_WITH_ID = 101;
    public static final int CODE_INGREDIENTS = 200;
    public static final int CODE_INGREDIENTS_WITH_ID = 201;
    public static final int CODE_STEPS = 300;
    public static final int CODE_STEPS_WITH_ID = 301;

    public static final UriMatcher URI_MATCHER = uriMatcher();
    public RecipeDbHelper mRecipeDbHelper;

    private static UriMatcher uriMatcher() {

        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String contentAuthority = RecipeContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(contentAuthority,
                RecipeContract.PATH_RECIPE, CODE_RECIPES);
        uriMatcher.addURI(contentAuthority,
                RecipeContract.PATH_RECIPE + "/#", CODE_RECIPES_WITH_ID);

        uriMatcher.addURI(contentAuthority,
                RecipeContract.PATH_INGREDIENTS, CODE_INGREDIENTS);
        uriMatcher.addURI(contentAuthority,
                RecipeContract.PATH_INGREDIENTS + "/#", CODE_INGREDIENTS_WITH_ID);

        uriMatcher.addURI(contentAuthority,
                RecipeContract.PATH_STEPS, CODE_STEPS);
        uriMatcher.addURI(contentAuthority,
                RecipeContract.PATH_STEPS + "/#", CODE_STEPS_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mRecipeDbHelper = new RecipeDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor mCursor;

        switch (URI_MATCHER.match(uri)) {
            case CODE_RECIPES:
                mCursor = mRecipeDbHelper.getReadableDatabase().query(
                        RecipeContract.RecipeMain.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_RECIPES_WITH_ID:
                String recipeId = uri.getLastPathSegment();
                String[] selectionRecipe = new String[]{recipeId};

                mCursor = mRecipeDbHelper.getReadableDatabase().query(
                        RecipeContract.RecipeMain.TABLE_NAME,
                        projection,
                        RecipeContract.RecipeMain.COLUMN_RECIPE_ID + " = ? ",
                        selectionRecipe,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_INGREDIENTS:
                mCursor = mRecipeDbHelper.getReadableDatabase().query(
                        RecipeContract.RecipeIngredients.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_INGREDIENTS_WITH_ID:
                String ingredientsId = uri.getLastPathSegment();
                String[] selectionIngredients = new String[]{ingredientsId};

                mCursor = mRecipeDbHelper.getReadableDatabase().query(
                        RecipeContract.RecipeIngredients.TABLE_NAME,
                        projection,
                        RecipeContract.RecipeIngredients.COLUMN_RECIPE_ID + " = ? ",
                        selectionIngredients,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_STEPS:
                mCursor = mRecipeDbHelper.getReadableDatabase().query(
                        RecipeContract.RecipeIngredients.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_STEPS_WITH_ID:
                String stepsId = uri.getLastPathSegment();
                String[] selectionSteps = new String[]{stepsId};

                mCursor = mRecipeDbHelper.getReadableDatabase().query(
                        RecipeContract.RecipeSteps.TABLE_NAME,
                        projection,
                        RecipeContract.RecipeSteps.COLUMN_RECIPE_ID + " = ? ",
                        selectionSteps,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        mCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return mCursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase db = mRecipeDbHelper.getWritableDatabase();
        int rowsInserted = 0;

        switch (URI_MATCHER.match(uri)) {

            case CODE_RECIPES:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(RecipeContract.RecipeMain.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;

            case CODE_INGREDIENTS:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(RecipeContract.RecipeIngredients.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;

            case CODE_STEPS:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(RecipeContract.RecipeSteps.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int numRowsDeleted;

        if (null == selection) selection = "1";

        switch (URI_MATCHER.match(uri)) {
            case CODE_RECIPES:
                numRowsDeleted = mRecipeDbHelper.getWritableDatabase().delete(
                        RecipeContract.RecipeMain.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_RECIPES_WITH_ID:
                numRowsDeleted = mRecipeDbHelper.getWritableDatabase().delete(
                        RecipeContract.RecipeMain.TABLE_NAME,
                        RecipeContract.RecipeMain.COLUMN_RECIPE_ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_INGREDIENTS:
                numRowsDeleted = mRecipeDbHelper.getWritableDatabase().delete(
                        RecipeContract.RecipeIngredients.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_INGREDIENTS_WITH_ID:
                numRowsDeleted = mRecipeDbHelper.getWritableDatabase().delete(
                        RecipeContract.RecipeIngredients.TABLE_NAME,
                        RecipeContract.RecipeIngredients.COLUMN_RECIPE_ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_STEPS:
                numRowsDeleted = mRecipeDbHelper.getWritableDatabase().delete(
                        RecipeContract.RecipeSteps.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_STEPS_WITH_ID:
                numRowsDeleted = mRecipeDbHelper.getWritableDatabase().delete(
                        RecipeContract.RecipeSteps.TABLE_NAME,
                        RecipeContract.RecipeSteps.COLUMN_RECIPE_ID + " = ? ",
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
