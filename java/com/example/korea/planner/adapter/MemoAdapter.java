package com.example.korea.planner.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.korea.planner.R;
import com.example.korea.planner.View.MemoAdd.MemoAddActivity;
import com.example.korea.planner.data.GoalData;
import com.example.korea.planner.data.MemoData;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by korea on 2017-04-26.
 */

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {
    private Realm realm;
    private RealmResults<MemoData> items;
    private Context context;

    public MemoAdapter(RealmResults<MemoData> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_recycler_item, parent, false));
        return vh;
    }

    @Override
    public void onBindViewHolder(MemoAdapter.ViewHolder holder, int position) {
        holder.memoTitle.setText(items.get(position).getTitle());
        holder.memoContent.setText(items.get(position).getContent());
        holder.memoCard.setCardBackgroundColor(items.get(position).getColor());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView memoTitle, memoContent;
        private CardView memoCard;

        public ViewHolder(View itemView) {
            super(itemView);
            memoTitle = (TextView) itemView.findViewById(R.id.memo_title);
            memoContent = (TextView) itemView.findViewById(R.id.memo_content);
            memoCard = (CardView) itemView.findViewById(R.id.memo_cardView);
            memoCard.setOnClickListener(this);
            memoCard.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.memo_cardView:
                    Intent intent = new Intent(context, MemoAddActivity.class);
                    intent.putExtra("id", items.get(getAdapterPosition()).getId());
                    context.startActivity(intent);

                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.memo_cardView:
                    MaterialDialog dialog = new MaterialDialog.Builder(context)
                            .title("메모 삭제")
                            .content("정말 삭제하시겠습니까?")
                            .positiveText("삭제")
                            .negativeText("취소")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    realm = Realm.getDefaultInstance();
                                    realm.beginTransaction();
                                    MemoData item = realm.where(MemoData.class).equalTo("id", items.get(getAdapterPosition()).getId()).findFirst();
                                    item.deleteFromRealm();
                                    realm.commitTransaction();
                                    notifyDataSetChanged();
                                    realm.close();
                                }
                            }).build();
                    dialog.show();
                    break;
            }
            return false;
        }
    }
}
