package de.uni_stuttgart.informatik.sopra.sopraapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.activity.TagAlertHelper;
import de.uni_stuttgart.informatik.sopra.sopraapp.persistence.CockpitDbHelper;
import de.uni_stuttgart.informatik.sopra.sopraapp.persistence.model.Tag;

/**
 * CustomQueryAdapter to get the text and create it.
 */
public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.QueryAdapterViewHolder> {
    // get table information
    private CockpitDbHelper cockpitDbHelper;
    private TagAlertHelper tagAlertHelper;
    private Context context;

    /**
     * constructor
     *
     * @param cockpitDbHelper
     * @param context
     */
    public TagListAdapter(CockpitDbHelper cockpitDbHelper,
                          TagAlertHelper tagAlertHelper,
                          Context context) {
        //Initialising the sql table
        this.cockpitDbHelper = cockpitDbHelper;
        this.tagAlertHelper = tagAlertHelper;
        this.context = context;
    }

    @NonNull
    @Override
    public QueryAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tag_card_item, parent, false);
        return new QueryAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QueryAdapterViewHolder holder, int position) {
        // tags are already in record
        Tag tagRecord = cockpitDbHelper.getTagByListOffset(position);
        holder.tagRecord = tagRecord;
        holder.tagNameTextView.setText(tagRecord.getName());

        holder.mView.setOnClickListener(v -> {
            tagAlertHelper.showTagEditDialog(tagRecord, true);
        });
    }

    @Override
    public int getItemCount() {
        return cockpitDbHelper.getAllTags().size();
    }

    /**
     * view holder of an item
     */
    public class QueryAdapterViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private Tag tagRecord;
        private TextView tagNameTextView;
        /**
         * constructor
         *
         * @param itemView
         */
        private QueryAdapterViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            tagNameTextView = itemView.findViewById(R.id.tag_name_text_view);
        }
    }
}
