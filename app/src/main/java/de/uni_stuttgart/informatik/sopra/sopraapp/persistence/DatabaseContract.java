package de.uni_stuttgart.informatik.sopra.sopraapp.persistence;

import android.provider.BaseColumns;

/**
 * this class defines the database structure
 */
public class DatabaseContract {
    public static final String DATABASE_NAME = "SNMPCockpit.db";
    public static final int DATABASE_VERSION = 21;

    /**
     * query table
     */
    public class QueryTable implements BaseColumns {
        static final String TABLE_NAME = "custom_queries";

        static final String COLUMN_NAME_OID = "OID";
        static final String COLUMN_NAME_NAME = "description";
        static final String COLUMN_NAME_IS_SINGLE = "is_single";
    }

    /**
     * category table
     */
    public class CategoryTable implements BaseColumns {
        static final String TABLE_NAME = "tags";

        static final String COLUMN_NAME_NAME = "description";
    }

    /**
     * linking table for m:n relation of the tables above
     */
    public class QueryToCategoryTable implements BaseColumns {
        static final String TABLE_NAME = "query_to_category";

        static final String COLUMN_NAME_QUERY_ID = "query_id";
        static final String COLUMN_NAME_CATEGORY_ID = "category_id";
    }
}
