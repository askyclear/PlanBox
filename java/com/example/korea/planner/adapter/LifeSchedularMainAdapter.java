package com.example.korea.planner.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.korea.planner.R;
import com.example.korea.planner.View.LifeSchedular.LifeSchedularActivity;
import com.example.korea.planner.View.LifeSchedular.LifeSchedularMainActivity;
import com.example.korea.planner.data.LifeSchedularData;
import com.example.korea.planner.data.LifeSchedularDataList;
import com.example.korea.planner.util.CustomLifeSchedularView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by korea on 2017-03-24.
 *
 */

public class LifeSchedularMainAdapter extends RecyclerView.Adapter<LifeSchedularMainAdapter.ViewHolder> {
    private RealmResults<LifeSchedularData> items;
    private ArrayList<LifeSchedularDataList> dataLists;
    private Realm realm;
    private Context context;

    public LifeSchedularMainAdapter(RealmResults<LifeSchedularData> itmes, Context context) {
        this.context = context;
        this.items = itmes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.life_schedular_main_recycler_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LifeSchedularData item = items.get(position);
        dataLists = new ArrayList<>();
        //item에 등록된 List 가져오기.
        dataLists.addAll(item.getList());
        holder.lifemainimage.setBackgroundResource(dataLists.get(0).getColor());
        holder.lifemaincategoryname.setText(item.getCategory_name());
        holder.lifemaincategorycontent.setText(item.getCategory_content());
        holder.lifemainimage.setTing(dataLists, 0);
        holder.lifemainimage.invalidate();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private CustomLifeSchedularView lifemainimage;
        private TextView lifemaincategoryname;
        private TextView lifemaincategorycontent;

        ViewHolder(View itemView) {
            super(itemView);
            lifemainimage = (CustomLifeSchedularView) itemView.findViewById(R.id.life_main_image);
            lifemaincategoryname = (TextView) itemView.findViewById(R.id.life_main_category_title);
            lifemaincategorycontent = (TextView) itemView.findViewById(R.id.life_main_category_content);
            lifemainimage.setOnClickListener(this);
            lifemainimage.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                default:
                    int position = getAdapterPosition();
                    Intent intent = new Intent(context, LifeSchedularActivity.class);
                    intent.putExtra("ItemPosition", items.get(position).getId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                default:
                    new MaterialDialog.Builder(context)
                            .title("제거")
                            .content("정말 제거하시겠습니까?")
                            .positiveText("제거")
                            .negativeText("취소")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    realm = Realm.getDefaultInstance();
                                    realm.beginTransaction();
                                    items.deleteFromRealm(getAdapterPosition());
                                    realm.commitTransaction();
                                    notifyDataSetChanged();
                                    realm.close();
                                }
                            }).build().show();
                    break;

            }
            return false;
        }
    }
}
