package de.uni_stuttgart.informatik.sopra.sopraapp.query.view;

import android.util.Log;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.OIDCatalog;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.OIDNotInCatalogException;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * this class represents a displayable section in the ui with a title and a type (enum {@link SectionType} )
 */
abstract class AbstractCockpitQuerySection {

    public static final String TAG = AbstractCockpitQuerySection.class.getName();
    private String title;
    private SectionType type;
    private OIDCatalog catalog;
    private int unkownOIDCounter = -1;
    private boolean skipUnknown = true;

    public AbstractCockpitQuerySection(String title, SectionType type) {
        this.title = title;
        this.type = type;
        this.catalog = OIDCatalog.getInstance(null);
    }

    public abstract String generateHtml();

    public abstract boolean isEmpty();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SectionType getType() {
        return type;
    }

    protected String generateTitle() {
        return "<h3>" + getTitle() + "</h3>";
    }

    public boolean isSkipUnknown() {
        return skipUnknown;
    }

    public void setSkipUnknown(boolean skipUnknown) {
        this.skipUnknown = skipUnknown;
    }

    public OIDCatalog getCatalog() {
        return catalog;
    }

    /**
     * helper method to display a line of a component of a single query response
     *
     * @param sb
     * @param catalog
     * @param qr
     */
    protected void addQueryResponseRow(StringBuilder sb, QueryResponse qr) {
        String asnName = null;
        try {
            asnName = catalog.getAsnByOidStripLast(qr.getOid());
        } catch (OIDNotInCatalogException e) {
            Log.d(TAG, "unknown oid '" + qr.getOid() + "' use 'unknown' as fallback and skipunknown: " + skipUnknown);
            if (skipUnknown) {
                return;
            }
            asnName = "unknown-" + unkownOIDCounter++;
        }
        sb.append("<li>");
        sb.append("<i>").append(asnName).append("</i>").append(": ");
        sb.append("<span class='oid_value'>").append(qr.getValue()).append("</span>");

        sb.append("<span class='oid_info'>(").append(qr.getOid()).append(")</span>");

        sb.append("</li>");
    }

    protected void addNoDataText(StringBuilder sb) {
        sb.append("<i>Keine Informationen vorhanden.</i>");
    }

    /**
     *
     * @param sb
     * @param title
     * @param content
     * @param cssClasses nullable
     */
    protected void addAccordionItem(StringBuilder sb, String title, String content, String cssClasses) {
        if (cssClasses == null) {
            sb.append("<div class='accordion'>");
        } else {
            sb.append("<div class='accordion " + cssClasses + "'>");
        }
        sb.append(title);
        sb.append("</div>");
        sb.append("<div class='content'><ul>");
        sb.append(content);
        sb.append("</ul></div>");
    }

    public enum SectionType {
        LIST, TABLE, GROUPED_LIST
    }
}
