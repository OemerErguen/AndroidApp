package de.uni_stuttgart.informatik.sopra.sopraapp.query.view;

import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.OIDCatalog;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.TableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.model.QueryResponse;

/**
 * generates an accordion view section
 */
public class GroupedListQuerySection extends AbstractCockpitQuerySection {

    private final TableQuery tableQuery;

    public GroupedListQuerySection(String title, TableQuery tableQuery) {
        super(title, SectionType.GROUPED_LIST);
        this.tableQuery = tableQuery;
    }

    @Override
    public String generateHtml() {
        StringBuilder sb = new StringBuilder();

        sb.append(generateTitle());

        if (isEmpty()) {
            // translate this as a string resource!
            addNoDataText(sb);
        }

        int i = 0;
        for (String rowIndex : tableQuery.getContent().keySet()) {
            Map<String, QueryResponse> stringQueryResponseMap = tableQuery.getContent().get(rowIndex);
            String rowTitle = tableQuery.getRowTitle(stringQueryResponseMap, i);
            StringBuilder cb = new StringBuilder();
            for (QueryResponse qr : stringQueryResponseMap.values()) {
                addQueryResponseRow(cb, qr);
            }
            addAccordionItem(sb, rowTitle, cb.toString(), null);
            i++;
        }

        return sb.toString();
    }

    @Override
    public boolean isEmpty() {
        return tableQuery.getContent().keySet().isEmpty();
    }
}
