package de.uni_stuttgart.informatik.sopra.sopraapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.json.JsonCatalogItem;

public class JsonCatalogTest {

    @Test
    public void testJsonCatalogRead() throws IOException {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        InputStream resourceAsStream = getClass().getResourceAsStream("/oid_catalog.json");

        Map<String, JsonCatalogItem> map = om.readValue(resourceAsStream, new TypeReference<Map<String, JsonCatalogItem>>(){});
        Assert.assertNotNull(map);
        Assert.assertNotEquals(0, map.size());
        Assert.assertEquals(1543, map.size());
    }

    @Test
    public void testJsonTreeRead() throws IOException {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        InputStream resourceAsStream = getClass().getResourceAsStream("/oid_tree.json");

        JsonNode node = om.readTree(resourceAsStream);
        Assert.assertNotNull(node);
    }
}
