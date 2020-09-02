package de.uni_stuttgart.informatik.sopra.sopraapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.activity.AlertHelper;
import de.uni_stuttgart.informatik.sopra.sopraapp.fragment.items.QueryContent.OidLeafItem;
import tellh.com.recyclertreeview_lib.LayoutItemType;
import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;
import tellh.com.recyclertreeview_lib.TreeViewBinder;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class QueryCatalogFragment extends Fragment {

    public static final String TAG = QueryCatalogFragment.class.getName();
    private LinearLayoutManager layout;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public QueryCatalogFragment() {
        // mandatory empty
    }

    /**
     * @return
     */
    public static QueryCatalogFragment newInstance() {
        QueryCatalogFragment fragment = new QueryCatalogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_query_catalog, container, false);

        List<TreeNode> nodes = new ArrayList<>();
        TreeNode<CatalogItem> catalogTree = new TreeNode<>(new CatalogItem(getString(R.string.mib_tree_title)));
        nodes.add(catalogTree);

        JsonNode parentNode = readJsonTree();
        if (parentNode != null) {
            loadJsonTree(parentNode, 0, catalogTree);
        }
        TreeViewAdapter adapter = new TreeViewAdapter(nodes,
                Arrays.asList(new CatalogItemBinder(),
                        new DirectoryNodeBinder(),
                        new TableNodeBinder(),
                        new TableEntryNodeBinder(),
                        new TableEntryItemNodeBinder()
                ));
        RecyclerView rv = view.findViewById(R.id.query_catalog_list);
        layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layout);

        adapter.setOnTreeNodeListener(new TreeViewAdapter.OnTreeNodeListener() {
            @Override
            public boolean onClick(TreeNode node, RecyclerView.ViewHolder holder) {
                if (!node.isLeaf()) {
                    //Update and toggle the node.
                    onToggle(!node.isExpand(), holder);
                }
                // only leafs are clickable directly
                if (node.isLeaf() && node.isQueryable()) {
                    showOIDQueryDialog(node.getOidValue());
                    return true;
                }
                return false;
            }

            @Override
            public boolean onLongTap(TreeNode node, RecyclerView.ViewHolder holder) {
                // only proper oid value nodes are allowed here
                if (node.isQueryable() && node.isDeep()) {
                    showOIDQueryDialog(node.getOidValue());
                    return true;
                }
                return false;
            }

            @Override
            public void onToggle(boolean isExpand, RecyclerView.ViewHolder holder) {
                if (holder instanceof DirectoryNodeBinder.ViewHolder) {
                    layout.scrollToPositionWithOffset(holder.getAdapterPosition(), 175);
                    DirectoryNodeBinder.ViewHolder dirViewHolder = (DirectoryNodeBinder.ViewHolder) holder;
                    final ImageView ivArrow = dirViewHolder.getIvArrow();
                    if (ivArrow != null) {
                        int rotateDegree = isExpand ? 90 : -90;
                        ivArrow.animate().rotationBy(rotateDegree)
                                .start();
                    }
                }
            }
        });
        adapter.ifCollapseChildWhileCollapseParent(false);
        rv.setAdapter(adapter);

        if (parentNode == null) {
            return view;
        }

        TreeNode firstNode = catalogTree.getChildList().get(0);
        TreeNode secondNode = (TreeNode) firstNode.getChildList().get(0);
        TreeNode thirdNode = (TreeNode) secondNode.getChildList().get(0);
        catalogTree.expand(); // expand root
        firstNode.expand(); // 1.3
        secondNode.expand(); // 1.3.6
        thirdNode.expand(); // 1.3.6.1
        TreeNode node4 = (TreeNode) thirdNode.getChildList().get(0);
        TreeNode node5 = (TreeNode) thirdNode.getChildList().get(1);
        node4.expand(); // 1.3.6.1.2
        node5.expand(); // 1.3.6.1.6
        adapter.refresh(nodes);

        return view;
    }

    /**
     * helper method to show dialog to choose devices
     *
     * @param oidValue
     */
    private void showOIDQueryDialog(String oidValue) {
        if (getActivity() == null) {
            return;
        }
        showQueryTargetDialog(oidValue);
    }

    private void showQueryTargetDialog(final String oidValue) {
        new AlertHelper(getActivity()).showQueryTargetDialog(oidValue);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * heart of the tree
     *
     * @param parentNode
     * @param depth
     * @param parentTreeNode
     */
    private static void loadJsonTree(JsonNode parentNode, int depth, TreeNode<CatalogItem> parentTreeNode) {
        if (!parentNode.isObject()) {
            throw new IllegalStateException("invalid input given. no json object in " + parentNode);
        }
        if (parentTreeNode == null) {
            throw new IllegalStateException("invalid parent tree node given");
        }
        JsonNode childrenNodes = parentNode.path("children");
        if (childrenNodes == null || !childrenNodes.isObject()) {
            throw new IllegalStateException("invalid children object received in json oid tree");
        }
        Iterator<JsonNode> iter = childrenNodes.iterator();
        Iterator<String> fieldNames = childrenNodes.fieldNames();
        while (iter.hasNext()) {
            JsonNode node = iter.next();
            String fieldName = fieldNames.next();
            // affects "children" fields
            boolean isLeaf = node.path("isLeaf").asBoolean();
            if (isLeaf) {
                processLeaf(depth, parentTreeNode, childrenNodes, node);
            } else {
                if (fieldName.equals(".") || fieldName.equals("1")) {
                    loadJsonTree(node, depth, parentTreeNode);
                } else {
                    TreeNode<CatalogItem> subTree = new TreeNode<>(new CatalogItem(fieldName));
                    parentTreeNode.addChild(subTree);
                    loadJsonTree(node, depth + 1, subTree);
                }
            }
        }
    }

    /**
     * helper method to process a single leaf tree node
     *
     * @param depth
     * @param parentTreeNode
     * @param childrenNodes
     * @param node
     */
    private static void processLeaf(int depth, TreeNode<CatalogItem> parentTreeNode, JsonNode childrenNodes, JsonNode node) {
        if (parentTreeNode == null) {
            throw new IllegalStateException("invalid parent tree node");
        }
        String nodeAsnName = node.path("name").textValue();
        String oidValueOfLeaf = node.path("oidValue").textValue();
        String leafNodeTitle = oidValueOfLeaf + " - " + nodeAsnName;
        TreeNode<CatalogItem> subTree;
        if (nodeAsnName.endsWith("Table")) {
            subTree = new TreeNode<>(new CatalogTable(leafNodeTitle), oidValueOfLeaf, leafNodeTitle);
        } else if (nodeAsnName.endsWith("Entry")) {
            subTree = new TreeNode<>(new CatalogTableEntry(leafNodeTitle), oidValueOfLeaf, leafNodeTitle);
        } else {
            subTree = new TreeNode<>(new CatalogItem(leafNodeTitle), oidValueOfLeaf, leafNodeTitle);
        }
        JsonNode childrenOfLeaf = node.path("children");
        if (!childrenNodes.isObject()) {
            throw new IllegalStateException("invalid children object received in json oid tree");
        }
        if (childrenOfLeaf.size() == 0) {
            // we shorten leaf titles here
            if (parentTreeNode.isDeep()) {
                leafNodeTitle = leafNodeTitle.replace(parentTreeNode.getOidValue() + ".", "");
            }
            if (parentTreeNode.isTableOrEntry()) {
                subTree = new TreeNode<>(new CatalogTableEntryItem(leafNodeTitle), oidValueOfLeaf, leafNodeTitle);
            } else {
                subTree = new TreeNode<>(new CatalogLeaf(leafNodeTitle), oidValueOfLeaf, leafNodeTitle);
            }
        }
        parentTreeNode.addChild(subTree);
        loadJsonTree(node, depth + 1, subTree);
    }

    /**
     * method to read json tree file to a jackson JsonNode object
     * @return
     */
    private JsonNode readJsonTree() {
        ObjectMapper om = new ObjectMapper();
        try {
            Reader is = new BufferedReader(
                    new InputStreamReader(getContext().getResources().openRawResource(R.raw.oid_tree), "UTF8"));
            JsonNode tree = om.readTree(is);
            is.close();
            return tree;
        } catch (IOException e) {
            Log.w(TAG, "error reading json tree: " + e.getMessage());
        }
        return null;
    }
    /* NOTE: the following code is for recycler tree view integration only !*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(OidLeafItem item);
    }

    public static class CatalogItem implements LayoutItemType {
        private String nodeName;

        public CatalogItem(String nodeName) {
            this.nodeName = nodeName;
        }

        @Override
        public int getLayoutId() {
            return R.layout.catalog_item_node;
        }

        public String getNodeName() {
            return nodeName;
        }

        public void setNodeName(String nodeName) {
            this.nodeName = nodeName;
        }
    }

    public static class CatalogLeaf extends CatalogItem {
        public CatalogLeaf(String nodeName) {
            super(nodeName);
        }

        @Override
        public int getLayoutId() {
            return R.layout.catalog_item_leaf;
        }
    }

    public static class CatalogTable extends CatalogItem {
        public CatalogTable(String nodeName) {
            super(nodeName);
        }

        @Override
        public int getLayoutId() {
            return R.layout.catalog_item_table;
        }
    }

    public static class CatalogTableEntry extends CatalogItem {
        public CatalogTableEntry(String nodeName) {
            super(nodeName);
        }

        @Override
        public int getLayoutId() {
            return R.layout.catalog_item_entry;
        }
    }

    public static class CatalogTableEntryItem extends CatalogItem {
        public CatalogTableEntryItem(String nodeName) {
            super(nodeName);
        }

        @Override
        public int getLayoutId() {
            return R.layout.catalog_item_entry_leaf;
        }
    }

    /**
     * the following classes are used for recycler tree view
     */
    public class CatalogItemBinder extends TreeViewBinder<CatalogItemBinder.CatalogItemViewHolder> {
        @Override
        public CatalogItemViewHolder provideViewHolder(View itemView) {
            return new CatalogItemViewHolder(itemView);
        }

        @Override
        public void bindView(CatalogItemViewHolder holder, int position, TreeNode node) {
            CatalogItem fileNode = (CatalogItem) node.getContent();
            holder.getNodeInformationTextView().setText(fileNode.getNodeName());
        }

        @Override
        public int getLayoutId() {
            return R.layout.catalog_item_leaf;
        }

        public class CatalogItemViewHolder extends TreeViewBinder.ViewHolder {
            private final TextView nodeInformationTextView;

            public CatalogItemViewHolder(View rootView) {
                super(rootView);
                this.nodeInformationTextView = (TextView) rootView.findViewById(R.id.query_catalog_list_item_text);
            }

            public TextView getNodeInformationTextView() {
                return nodeInformationTextView;
            }
        }
    }

    public class DirectoryNodeBinder extends TreeViewBinder<DirectoryNodeBinder.ViewHolder> {
        @Override
        public ViewHolder provideViewHolder(View itemView) {
            return new ViewHolder(itemView);
        }

        @Override
        public void bindView(ViewHolder holder, int position, TreeNode node) {
            holder.ivArrow.setRotation(0);
            holder.ivArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black);
            int rotateDegree = node.isExpand() ? 90 : 0;
            holder.ivArrow.setRotation(rotateDegree);
            CatalogItem dirNode = (CatalogItem) node.getContent();
            holder.listItemText.setText(dirNode.getNodeName());
            if (node.isLeaf()) {
                holder.ivArrow.setVisibility(View.INVISIBLE);
            } else {
                holder.ivArrow.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getLayoutId() {
            return R.layout.catalog_item_node;
        }

        public class ViewHolder extends TreeViewBinder.ViewHolder {
            private ImageView ivArrow;
            private TextView listItemText;

            public ViewHolder(View rootView) {
                super(rootView);
                this.ivArrow = (ImageView) rootView.findViewById(R.id.iv_arrow);
                this.listItemText = (TextView) rootView.findViewById(R.id.query_catalog_list_item_text);
            }

            public ImageView getIvArrow() {
                return ivArrow;
            }

            public TextView getListItemText() {
                return listItemText;
            }
        }
    }

    public class TableNodeBinder extends DirectoryNodeBinder {
        @Override
        public int getLayoutId() {
            return R.layout.catalog_item_table;
        }
    }

    public class TableEntryNodeBinder extends DirectoryNodeBinder {
        @Override
        public int getLayoutId() {
            return R.layout.catalog_item_entry;
        }
    }

    public class TableEntryItemNodeBinder extends CatalogItemBinder {
        @Override
        public int getLayoutId() {
            return R.layout.catalog_item_entry_leaf;
        }
    }
}
