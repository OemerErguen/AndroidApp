package de.uni_stuttgart.informatik.sopra.sopraapp.persistence.model;

import java.util.ArrayList;
import java.util.List;

/**
 * custom query model class
 */
public class CustomQuery {
    private long id;
    private String oid;
    private String name;
    private boolean isSingleQuery;
    private List<Tag> tagList = new ArrayList<>();

    public CustomQuery(long id, String oid, String name, boolean isSingleQuery) {
        this.id = id;
        this.oid = oid;
        this.name = name;
        this.isSingleQuery = isSingleQuery;
    }

    public long getId() {
        return id;
    }

    public String getOid() {
        return oid;
    }

    public String getName() {
        return name;
    }

    public boolean isSingleQuery() {
        return isSingleQuery;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    @Override
    public String toString() {
        return "CustomQuery{" +
                "id=" + id +
                ", oid='" + oid + '\'' +
                ", name='" + name + '\'' +
                ", isSingleQuery=" + isSingleQuery +
                '}';
    }
}
