package de.uni_stuttgart.informatik.sopra.sopraapp.tasks;

import android.util.Log;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import de.uni_stuttgart.informatik.sopra.sopraapp.CockpitPreferenceManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.SnmpQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.TableQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.impl.DefaultListQuery;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.view.CockpitQueryView;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.view.GroupedListQuerySection;
import de.uni_stuttgart.informatik.sopra.sopraapp.query.view.ListQuerySection;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpManager;

/**
 * this interface provides methods for tab tasks so we can use them whithout extending android
 * {@link android.os.AsyncTask}
 * <p>
 * this is more a "trait" as an interface and the price we pay is: default interface methods need to be public
 */
public interface TabTaskHelper {

    String TAB_TASK_TAG = "TabTask";

    public void cancelTasks();

    /**
     * simple helper method
     *
     * @param qt
     * @return
     */
    public default SnmpQuery getAnswer(QueryTask<?> qt) {
        try {
            if (qt.getDeviceConfiguration() == null) {
                throw new IllegalStateException("null device config given");
            }
            int offset = qt.getDeviceConfiguration().getVersionSpecificTimeoutOffset();
            SnmpQuery query = qt.get((long) CockpitPreferenceManager.TIMEOUT_WAIT_ASYNC_MILLISECONDS + offset, TimeUnit.MILLISECONDS);
            if (query != null && qt.getDeviceConfiguration() != null) {
                SnmpManager.getInstance().resetTimeout(qt.getDeviceConfiguration());
                return query;
            }
        } catch (RuntimeException | InterruptedException e) {
            Log.e(TAB_TASK_TAG, "detail task interrupted!");
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            Log.e(TAB_TASK_TAG, "detail task failed: " + e.getMessage());
        } catch (TimeoutException e) {
            Log.w(TAB_TASK_TAG, "timeout reached for task: detail info query task");
            if (qt.getDeviceConfiguration() != null) {
                Log.d(TAB_TASK_TAG, "cancel all running tasks");
                cancelTasks();
                SnmpManager.getInstance().registerTimeout(qt.getDeviceConfiguration());
            }
        }
        return null;
    }

    /**
     * generic method to add a table section to this query view
     *
     * @param title
     * @param queryTask
     * @param queryView
     */
    public default void addTableSection(String title,
                                        QueryTask<? extends SnmpQuery> queryTask,
                                        AtomicReference<CockpitQueryView> queryView) {
        TableQuery snmpQuery = (TableQuery) getAnswer(queryTask);
        if (snmpQuery != null) {
            queryView.get().addQuerySection(new GroupedListQuerySection(title, snmpQuery));
        }
    }

    /**
     * generic method to add a list query
     *
     * @param title
     * @param infoTask
     * @param queryView
     */
    public default void addListQuery(String title,
                                     QueryTask<? extends SnmpQuery> infoTask,
                                     AtomicReference<CockpitQueryView> queryView) {
        DefaultListQuery ipInfoListQuery = (DefaultListQuery) getAnswer(infoTask);
        if (ipInfoListQuery != null) {
            ListQuerySection querySection = new ListQuerySection(title, ipInfoListQuery);
            querySection.setCollapsible(true);
            queryView.get().addQuerySection(querySection);
        }
    }
}
