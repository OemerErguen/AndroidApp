package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.VendorCatalog;

@RunWith(AndroidJUnit4.class)
public class VendorCatalogTest {

    @Rule
    public ActivityTestRule<CockpitMainActivity> rule = new ActivityTestRule<>(CockpitMainActivity.class);

    @Test
    public void testVendorCatalogBasics() {
        VendorCatalog vendorCatalog = VendorCatalog.getInstance(rule.getActivity().getApplicationContext());
        Assert.assertEquals("GeNUA mbH", vendorCatalog.getVendorById("3717"));
        Assert.assertEquals("Unix", vendorCatalog.getVendorById("4"));

        Assert.assertEquals("GeNUA mbH", vendorCatalog.getVendorByOID("1.3.6.1.4.1.3717"));
        Assert.assertEquals("Unix", vendorCatalog.getVendorByOID("1.3.6.1.4.1.4"));
        Assert.assertEquals("net-snmp", vendorCatalog.getVendorByOID("1.3.6.1.4.1.8072.3.2.10"));
        Assert.assertNull(vendorCatalog.getVendorById(null));
        Assert.assertNull(vendorCatalog.getVendorByOID(null));
    }
}
