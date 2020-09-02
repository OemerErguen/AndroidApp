package de.uni_stuttgart.informatik.sopra.sopraapp.query.view;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.ListQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * this class represents a ui section of a list query
 *
 */
public class ListQuerySection extends AbstractCockpitQuerySection {
    private ListQuery abstractListQuery;
    private boolean collapsible = false;

    public ListQuerySection(String title, ListQuery abstractListQuery) {
        super(title, SectionType.LIST);
        this.abstractListQuery = abstractListQuery;
    }

    /**
     * constructor
     *
     * @param title
     * @param abstractListQuery
     * @param collapsible
     */
    public ListQuerySection(String title, ListQuery abstractListQuery, boolean collapsible) {
        super(title, SectionType.LIST);
        this.abstractListQuery = abstractListQuery;
        this.collapsible = collapsible;
    }

    public boolean isCollapsible() {
        return collapsible;
    }

    public void setCollapsible(boolean collapsible) {
        this.collapsible = collapsible;
    }

    /**
     * generate html of this component
     *
     * @return
     */
    @Override
    public String generateHtml() {
        StringBuilder sb = new StringBuilder();

        // collapsible list items have title in accordion row
        if (!isCollapsible()) {
            sb.append(generateTitle());
        }

        if (isEmpty()) {
            if (isCollapsible()) {
                sb.append(generateTitle());
            }
            // translate this as a string resource!
            addNoDataText(sb);
        } else {
            StringBuilder cb = new StringBuilder();
            cb.append("<ul>");
            for (QueryResponse queryResponse : abstractListQuery.getListItems()) {
                addQueryResponseRow(cb, queryResponse);
            }
            cb.append("</ul>");

            if (isCollapsible()) {
                addAccordionItem(sb, getTitle(), cb.toString(), "list_header");
            } else {
                sb.append("<div class='list'>");
                sb.append(cb.toString());
                sb.append("</div>");
            }
        }

        return sb.toString();
    }

    @Override
    public boolean isEmpty() {
        return abstractListQuery.getListItems().isEmpty();
    }
}
