package com.example.korea.planner.View.LifeSchedular.setting.presenter;

import android.net.Uri;

import com.example.korea.planner.data.LifeSchedularData;

import java.util.List;

/**
 * Created by korea on 2017-03-30.
 * Contarct
 */

public class LifeSettingContract {
    public interface View {
        void init();
    }

    public interface Presenter {
        void setView(View view);

        List<String> getNames();

        List<LifeSchedularData> getItems();

        void changeDays(int position, int select_item_position);

        void setClicked(boolean b);

        boolean getClicked();

        boolean getVibed();

        boolean getSounded();

        void setVibed(boolean b);

        void setSounded(boolean b);

        void setTimeed(int selectedIndex);

        int getTimeIndex();

        void setRingtonUri(Uri uri);

        Uri getRingtonUri();

        void initialize();
    }
}
