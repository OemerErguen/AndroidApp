package de.uni_stuttgart.informatik.sopra.sopraapp.activity;

import android.app.Activity;

import com.google.zxing.integration.android.IntentIntegrator;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

/**
 * this class wrayps functionality for wifi qr code scanner
 */
public class QrScannerActivityHelper {

    private Activity activity;

    public QrScannerActivityHelper(Activity activity) {
        this.activity = activity;
    }

    /**
     * starting the zxing intent integrator.
     */
    public void startWifiScanner() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
        intentIntegrator.setPrompt(activity.getString(R.string.scan_wifi_code_label));
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.initiateScan();
    }

    /**
     * start device qr code scanner
     */
    public void startDeviceScanner() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
        intentIntegrator.setPrompt(activity.getString(R.string.scan_device_code_label));
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.initiateScan();
    }
}
