package com.roxybakestudio.jacobbakery.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class RecipeContract {

    public static final String CONTENT_AUTHORITY = "com.roxybakestudio.jacobbakery";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_RECIPE = "recipe";
    public static final String PATH_INGREDIENTS = "ingredients";
    public static final String PATH_STEPS = "steps";

    public static final class RecipeMain implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_RECIPE)
                .build();

        public static final String TABLE_NAME = "recipes";
        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SERVINGS = "servings";

        public static final int INDEX_RECIPE_NAME = 0;
        public static final int INDEX_RECIPE_ID = 1;

        public static Uri BuildRecipeUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }

    public static final class RecipeIngredients implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_INGREDIENTS)
                .build();

        public static final String TABLE_NAME = "ingredients";
        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_RECIPE_NAME = "recipe_name";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_INGREDIENT = "ingredient";

        public static final int INDEX_COLUMN_RECIPE_NAME = 0;
        public static final int INDEX_COLUMN_QUANTITY = 1;
        public static final int INDEX_COLUMN_MEASURE = 2;
        public static final int INDEX_COLUMN_INGREDIENT = 3;


        public static Uri buildIngredientUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }

    public static final class RecipeSteps implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_STEPS)
                .build();

        public static final String TABLE_NAME = "steps";
        public static final String COLUMN_STEP_ID = "step_id";
        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_SHORT_DESCRIPTION = "short_description";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_VIDEO = "video";

        public static final int INDEX_STEP_RECIPE_ID = 0;
        public static final int INDEX_SHORT_DESCRIPTION = 1;
        public static final int INDEX_STEP_ID = 2;

        public static Uri BuildStepUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }
}
