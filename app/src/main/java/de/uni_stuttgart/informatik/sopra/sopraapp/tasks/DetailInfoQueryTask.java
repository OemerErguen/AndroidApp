package de.uni_stuttgart.informatik.sopra.sopraapp.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;

import de.uni_stuttgart.informatik.sopra.sopraapp.query.SimpleSnmpListRequest;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.DefaultListQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general.AtTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general.EgpNeighTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general.IpAddressTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general.IpDefaultRouterTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general.IpNetToMediaTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general.IpRouteTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general.NetInterfaceTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general.TcpConnectionTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.general.UdpTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.genua.FuserTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.genua.RaidTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.genua.VpnTableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.view.CockpitQueryView;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.DeviceConfiguration;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpManager;

/**
 * this class is called when detail info of a device is requested
 * TODO i18n
 */
public class DetailInfoQueryTask extends AsyncTask<Void, Void, Void> implements TabTaskHelper {

    public static final String TAG = DetailInfoQueryTask.class.getName();

    private final AtomicReference<CockpitQueryView> queryView = new AtomicReference<>();
    private DeviceConfiguration deviceConfiguration;

    // start and send queries
    private QueryTask<NetInterfaceTableQuery> netInterfaceTableQueryTask = new QueryTask<>();
    private QueryTask<IpAddressTableQuery> ipQueryTask = new QueryTask<>();
    private QueryTask<IpRouteTableQuery> ipRouteTableTask = new QueryTask<>();
    private QueryTask<IpNetToMediaTableQuery> ipNetToMediaTask = new QueryTask<>();
    private QueryTask<AtTableQuery> atTask = new QueryTask<>();
    private QueryTask<TcpConnectionTableQuery> tcpConnectionTask = new QueryTask<>();
    private QueryTask<UdpTableQuery> udpConnectionTask = new QueryTask<>();
    private QueryTask<EgpNeighTableQuery> egpNeighTableQuery = new QueryTask<>();
    private QueryTask<RaidTableQuery> raidTableTask = new QueryTask<>();
    private QueryTask<VpnTableQuery> vpnTableTask = new QueryTask<>();
    private QueryTask<IpDefaultRouterTableQuery> ipDefaultRouterQueryTask = new QueryTask<>();
    private QueryTask<FuserTableQuery> fuserQueryTask = new QueryTask<>();
    private QueryTask<DefaultListQuery> ipInfoTask = new QueryTask<>();
    private QueryTask<DefaultListQuery> icmpInfoTask = new QueryTask<>();
    private QueryTask<DefaultListQuery> tcpInfoTask = new QueryTask<>();
    private QueryTask<DefaultListQuery> udpInfoTask = new QueryTask<>();
    private QueryTask<DefaultListQuery> egpInfoTask = new QueryTask<>();

    /**
     * constructor
     *
     * @param queryView
     * @param deviceConfiguration
     */
    public DetailInfoQueryTask(CockpitQueryView queryView, DeviceConfiguration deviceConfiguration) {
        this.queryView.set(queryView);
        this.deviceConfiguration = deviceConfiguration;
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "entering detail info query task");

        super.onPreExecute();

        ThreadPoolExecutor tpe = SnmpManager.getInstance().getThreadPoolExecutor();
        if (tpe.isTerminating() || tpe.isShutdown() || tpe.isTerminated()) {
            Log.d(TAG, "invalid thread pool given");
            return;
        }
        netInterfaceTableQueryTask.executeOnExecutor(tpe, new NetInterfaceTableQuery.NetInterfaceTableRequest(deviceConfiguration));
        ipDefaultRouterQueryTask.executeOnExecutor(tpe, new IpDefaultRouterTableQuery.IpDefaultRouterTableQueryRequest(deviceConfiguration));
        ipQueryTask.executeOnExecutor(tpe, new IpAddressTableQuery.IpAddrTableRequest(deviceConfiguration));
        ipRouteTableTask.executeOnExecutor(tpe, new IpRouteTableQuery.IpRouteTableRequest(deviceConfiguration));
        ipNetToMediaTask.executeOnExecutor(tpe, new IpNetToMediaTableQuery.IpNetToMediaTableRequest(deviceConfiguration));
        atTask.executeOnExecutor(tpe, new AtTableQuery.AtTableRequest(deviceConfiguration));
        tcpConnectionTask.executeOnExecutor(tpe, new TcpConnectionTableQuery.TcpConnectionTableQueryRequest(deviceConfiguration));
        udpConnectionTask.executeOnExecutor(tpe, new UdpTableQuery.UdpTableQueryRequest(deviceConfiguration));
        egpNeighTableQuery.executeOnExecutor(tpe, new EgpNeighTableQuery.EgpNeighTableQueryRequest(deviceConfiguration));
        raidTableTask.executeOnExecutor(tpe, new RaidTableQuery.RaidTableQueryRequest(deviceConfiguration));
        vpnTableTask.executeOnExecutor(tpe, new VpnTableQuery.VpnTableQueryRequest(deviceConfiguration));
        fuserQueryTask.executeOnExecutor(tpe, new FuserTableQuery.FuserTableQueryRequest(deviceConfiguration));
        ipInfoTask.executeOnExecutor(tpe, new DefaultListQuery.IpSectionQueryRequest(deviceConfiguration));
        icmpInfoTask.executeOnExecutor(tpe, new SimpleSnmpListRequest(deviceConfiguration, "1.3.6.1.2.1.5"));
        tcpInfoTask.executeOnExecutor(tpe, new SimpleSnmpListRequest(deviceConfiguration, "1.3.6.1.2.1.6"));
        udpInfoTask.executeOnExecutor(tpe, new SimpleSnmpListRequest(deviceConfiguration, "1.3.6.1.2.1.7"));
        egpInfoTask.executeOnExecutor(tpe, new SimpleSnmpListRequest(deviceConfiguration, "1.3.6.1.2.1.8"));
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (deviceConfiguration == null) {
            throw new IllegalArgumentException("null DeviceConfiguration given");
        }

        addTableSection("Netzwerkschnittstellen | ifTable", netInterfaceTableQueryTask, queryView);
        addTableSection("Standard-Router | ipDefaultRouterTable", ipDefaultRouterQueryTask, queryView);
        addTableSection("IP-Adressen | ipAddrTable", ipQueryTask, queryView);
        addTableSection("IP-Routen | ipRouteTable", ipRouteTableTask, queryView);
        addTableSection("ipNetToMediaTable", ipNetToMediaTask, queryView);
        addTableSection("Address Translation | atTable", atTask, queryView);
        addTableSection("TCP-Verbindungen | tcpConnTable", tcpConnectionTask, queryView);
        addTableSection("UDP-Verbindungen | udpTable", udpConnectionTask, queryView);
        addTableSection("egpNeighTable", egpNeighTableQuery, queryView);
        addTableSection("raidTable", raidTableTask, queryView);
        addTableSection("vpnTable", vpnTableTask, queryView);
        addTableSection("fuserTable", fuserQueryTask, queryView);

        // add list query
        addListQuery("IP-Konfiguration | ip", ipInfoTask, queryView);
        addListQuery("ICMP-Konfiguration | icmp", icmpInfoTask, queryView);
        addListQuery("TCP-Konfiguration | tcp", tcpInfoTask, queryView);
        addListQuery("UDP-Konfiguration | udp", udpInfoTask, queryView);
        addListQuery("EGP-Konfiguration | egp", egpInfoTask, queryView);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.d(TAG, "start rendering");
        // here we are on the ui thread
        queryView.get().render(true);
    }

    @Override
    public void cancelTasks() {
        netInterfaceTableQueryTask.cancel(true);
        ipDefaultRouterQueryTask.cancel(true);
        ipQueryTask.cancel(true);
        ipRouteTableTask.cancel(true);
        ipNetToMediaTask.cancel(true);
        atTask.cancel(true);
        tcpConnectionTask.cancel(true);
        udpConnectionTask.cancel(true);
        egpNeighTableQuery.cancel(true);
        raidTableTask.cancel(true);
        vpnTableTask.cancel(true);
        fuserQueryTask.cancel(true);
        ipInfoTask.cancel(true);
        icmpInfoTask.cancel(true);
        tcpInfoTask.cancel(true);
        udpInfoTask.cancel(true);
        egpInfoTask.cancel(true);
    }
}