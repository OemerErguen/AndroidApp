package de.uni_stuttgart.informatik.sopra.sopraapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.util.regex.Pattern;

import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitPreferenceManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitStateManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.json.DeviceQrCode;

/**
 * SNMP Login Activity
 * <p>
 * A login screen that offers login via email/password.
 */
public class SNMPLoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
    ProtectedActivity {
    public static final String HOST_KEY = "host";
    public static final String PORT_KEY = "port";
    public static final String COMMUNITY_KEY = "v1_community";
    public static final String USER_KEY = "v3_user";
    public static final String USER_PASSPHRASE_KEY = "v3_password";
    public static final String ENC_KEY = "v3_enc";
    // default values
    public static final int DEFAULT_SNMP_PORT = 161;
    public static final String DEFAULT_SNMP_COMUNITY = "public";
    private String qrString;

    Spinner snmpSpinner;
    EditText portField; //Port
    EditText communityField; //Community
    EditText hostField; //Host
    EditText userField; //Username
    EditText passwordField; //Password
    EditText encryptField; //Encryption

    private static final String TAG = SNMPLoginActivity.class.getName();

    private static final Pattern IPV4_PATTERN =
            Pattern.compile("\\A(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z");

    private static final Pattern PORT_PATTERN =
            Pattern.compile("^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$");

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snmplogin);
        setupActionBar();

        //EditText which the user or the QR-Scanner fills
        hostField = findViewById(R.id.editText_Host);
        portField = findViewById(R.id.editText_Port);
        communityField = findViewById(R.id.editText_Community);
        userField = findViewById(R.id.editText_Username);
        passwordField = findViewById(R.id.editText_Password);
        encryptField = findViewById(R.id.editText_encrypt);

        //Spinner and all the functions
        snmpSpinner = findViewById(R.id.snmp_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.snmp_versions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snmpSpinner.setAdapter(adapter);
        snmpSpinner.setOnItemSelectedListener(this);

        initObservables(this, new AlertHelper(this), null);

        /*
         * By pressing the addDevice Button, it opens the class ConnectDevice.
         * The ConnectDevice has a SNMP4J library, which connects the device.
         * Also provides all the information (not decided on which typ, maybe as a String) to the class,
         * which the class recalls and connects with. For that just delete the commentent lines.
         */
        Button addDevice = findViewById(R.id.add_device);
        addDevice.setOnClickListener(view -> {
            onDeviceAddClicked(addDevice);
        });

        //"Autofill per QR Code" Button and its set-on-click-method.
        Button scanQrCode = findViewById(R.id.scan_qr_code);

        /*
        Click method for the button "Autofill per QR-Code", which opens the scanner, encrypts the
        code and fills each EditView, so you can connect to the device afterwards.
         */
        scanQrCode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                scan();
            }
        });

        // NOTE: do not commit the following lines UNCOMMENTED!
        //hostField.setText("192.168.161.1");

//        snmpSpinner.setSelection(1);
//        portField.setText("162");
//        hostField.setText("192.168.178.158");
//        userField.setText("batmanuser");
//        passwordField.setText("batmankey3");
//        encryptField.setText("batmankey3");
    }

    /**
     * helper method to check if connection can be started and how
     *
     * @param addDevice
     */
    private void onDeviceAddClicked(Button addDevice) {
        //If the input is correct, try to connect to the device
        if (isInputValid()) {
            if (CockpitStateManager.getInstance().isConnecting()) {
                Log.d(TAG, "avoid multiple connection attempts at the same time.");
                new AlertDialog.Builder(SNMPLoginActivity.this)
                        .setTitle("Verbindungsversuch läuft noch")
                        .setMessage("Es läuft noch ein Verbindungsversuch. Bitte warten.")
                        .create().show();
                return;
            }
            addDevice.setEnabled(false);
            while (CockpitStateManager.getInstance().isInRemoval()) {
                Log.d(TAG, "wait for removal event is finished");
            }
            //Check first the connection to the device, before you add the device and go back to the main activity.
            Intent data = new Intent();
            data.putExtra(HOST_KEY, hostField.getEditableText().toString());
            // handle default values for port + community (v1)
            String inputPort = portField.getEditableText().toString();
            if (inputPort.isEmpty() || inputPort.equals("null")) {
                inputPort = "" + DEFAULT_SNMP_PORT;
            }
            data.putExtra(PORT_KEY, inputPort);

            if (snmpSpinner.getSelectedItemPosition() == 0) {
                // v1
                String inputCommunity = communityField.getEditableText().toString();
                if (inputCommunity.isEmpty() || inputCommunity.equals("null")) {
                    inputCommunity = DEFAULT_SNMP_COMUNITY;
                }
                data.putExtra(COMMUNITY_KEY, inputCommunity);
            } else {
                // v3
                data.putExtra(USER_KEY, userField.getEditableText().toString());
                data.putExtra(USER_PASSPHRASE_KEY, passwordField.getEditableText().toString());
                data.putExtra(ENC_KEY, encryptField.getEditableText().toString());
            }

            Log.d(TAG, data.toString());
            setResult(RESULT_OK, data);
            CockpitStateManager.getInstance().getIsInSessionTimeoutObservable().setValueAndTriggerObservers(false);
            CockpitStateManager.getInstance().getIsInTimeoutsObservable().setValueAndTriggerObservers(false);
            finish();
        }
    }

    /**
     * Set up the {@link android.support.v7.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startProtection(this);
    }

    //Method to show for each SNMP version the correct input fields.
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //create spinner and add a notification box, when selected a item.
        String text = parent.getItemAtPosition(position).toString();
        Log.i(TAG, "selected version " + text);

        //Using the each row of the table, to hide or show them, depending on the SNMP Version.
        TableRow community = findViewById(R.id.tablerow_community);
        TableRow username = findViewById(R.id.tablerow_username);
        TableRow password = findViewById(R.id.tablerow_password);
        TableRow encrypt = findViewById(R.id.tablerow_encrypt);

        snmpSpinner = findViewById(R.id.snmp_spinner);
        Log.i(TAG, snmpSpinner.getSelectedItem().toString());

        switch (position) {
            case 0: //SNMP Version 1 or 2c
                community.setVisibility(View.VISIBLE);
                username.setVisibility(View.INVISIBLE);
                password.setVisibility(View.INVISIBLE);
                encrypt.setVisibility(View.INVISIBLE);
                break;
            case 1: //SNMP Version 3
                community.setVisibility(View.INVISIBLE);
                username.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                encrypt.setVisibility(View.VISIBLE);
                break;
            default:
                throw new IllegalArgumentException("not supported position value");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        restartTrigger(this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * QR-scanner-method show flashlight - if preferences say to do so
     */
    public void scan() {
        CockpitPreferenceManager cockpit = new CockpitPreferenceManager(this);
        QrScannerActivityHelper scannerActivityHelper = new QrScannerActivityHelper(this);
        if (cockpit.showFlashlightHint()) {
            new AlertHelper(this).showFlashlightHintDialog(scannerActivityHelper, false);
        } else {
            scannerActivityHelper.startDeviceScanner();
        }
    }

    /**
     * handle and process device qr code scan result
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, R.string.connection_attempt_canceled, Toast.LENGTH_LONG).show();
            } else {
                qrString = result.getContents();
                Log.d(TAG, qrString);

                DeviceQrCode deviceQrCode = getDeviceQrCode(qrString);
                if (deviceQrCode != null) {
                    Log.i(TAG, "scanned device qr code: " + deviceQrCode);
                    clearValidatedFields();
                    autoFillEditTexts(deviceQrCode);
                } else {
                    Toast.makeText(this, R.string.toast_no_valid_device_qr_code, Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Get the QR-Code
     */
    private DeviceQrCode getDeviceQrCode(String qrString) {
        try {
            return new ObjectMapper().readValue(qrString, DeviceQrCode.class);
        } catch (IOException e) {
            Log.e(TAG, "error reading qr code " + e.getMessage());
        }
        return null;
    }

    /**
     * Autofill method, which gets the qr-code and filling with JACKSON each text gap.
     */
    private void autoFillEditTexts(DeviceQrCode deviceQrCode) {
        String qrCode = qrString;
        Log.d(TAG, "found qr code: " + qrCode);
        //If there is no password or encoder --> take SNMP V1/V2c.
        String usedIpAddress = deviceQrCode.getNaddr().getIPv4().split(":")[0];
        if (deviceQrCode.getPw().isEmpty() && deviceQrCode.getEnc().isEmpty()) {
            snmpSpinner.setSelection(0);
            //String gets splitted, since IPv4 contains also the host "xx:161"
            hostField.setText(usedIpAddress);
            portField.setText(String.valueOf(deviceQrCode.getPortv4())); //port in Integer
            communityField.setText(deviceQrCode.getUser());
            //SNMP V.3
        } else {
            snmpSpinner.setSelection(1);
            //String gets splitted, since IPv4 contains also the host "xx:161"
            hostField.setText(usedIpAddress);
            portField.setText(String.valueOf(deviceQrCode.getPortv4())); //port in Integer
            userField.setText(deviceQrCode.getUser());
            passwordField.setText(deviceQrCode.getPw());
            encryptField.setText(deviceQrCode.getEnc());
        }
    }

    /*
     * showError method, which gives the edittext field a warning hint, if the parameter is wrong.
     */
    private void showError(EditText text) {
        //Animation if pattern isn´t matching
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        text.startAnimation(shake);
        //Text output
        text.setError(getString(R.string.invalid_user_input));
    }

    public static boolean isIPv4Address(final String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

    public static boolean isPortValid(final String input) {
        return PORT_PATTERN.matcher(input).matches();
    }

    //TODO: Pattern for the host, IPv6, Username, Password, Host...

    /**
     * Method which checks the pattern of every input.
     */
    private boolean isInputValid() {
        if (!isIPv4Address(hostField.getText().toString())) {
            //TODO: Issue: If you have a String with the wrong pattern and you scan the next barcode, the error massage, is not disappearing.
            showError(hostField);
            return false;
        } else {
            hostField.setError(null);
        }

        String inputPort = portField.getText().toString();
        // allow empty port field, we use default fallback
        if (!inputPort.isEmpty() && !isPortValid(inputPort)) {
            showError(portField);
            return false;
        }
        return true;
    }

    /**
     * method to clear all validated fields (after a new qr device code was scanned with success)
     */
    private void clearValidatedFields() {
        hostField.setError(null);
        portField.setError(null);
    }

    @Override
    public void restartQueryCall() {
        // do nothing
    }
}

