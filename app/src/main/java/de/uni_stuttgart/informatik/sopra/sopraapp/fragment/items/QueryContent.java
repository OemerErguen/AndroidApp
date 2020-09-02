package de.uni_stuttgart.informatik.sopra.sopraapp.fragment.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class QueryContent {

    /**
     * An array of sample (dummy) items.
     */
    protected static final List<OidLeafItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    protected static final Map<String, OidLeafItem> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(OidLeafItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * create dummy item - e.g. for testing
     *
     * @param position
     * @return
     */
    private static OidLeafItem createDummyItem(int position) {
        return new OidLeafItem(String.valueOf(position), "Catalog Item " + position, makeDetails(position));
    }

    /**
     * only used in dummy item
     *
     * @param position
     * @return
     */
    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore detail information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class OidLeafItem {
        public final String id;
        public final String content;
        public final String details;

        public OidLeafItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
