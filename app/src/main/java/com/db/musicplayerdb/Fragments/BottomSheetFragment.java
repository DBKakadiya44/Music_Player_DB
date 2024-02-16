package com.db.musicplayerdb.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.db.musicplayerdb.Adapter.SelectAdapter;
import com.db.musicplayerdb.MainActivity;
import com.db.musicplayerdb.Model.MusicModel;
import com.db.musicplayerdb.Model.PlayListCode;
import com.db.musicplayerdb.Model.PlayListNamePref;
import com.db.musicplayerdb.R;
import com.db.musicplayerdb.SplashActivity;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetFragment extends DialogFragment {
    MainActivity mainActivity;
    ArrayList<MusicModel> audioList;
    List<String> namelist = new ArrayList<>();
    String edit;
    public BottomSheetFragment(MainActivity mainActivity, ArrayList<MusicModel> audioList, String edit) {
        this.mainActivity = mainActivity;
        this.audioList = audioList;
        this.edit=edit;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_dialog, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null) {
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.6);
            getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, height);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new SelectAdapter(mainActivity , audioList));

        ImageView chokdi = view.findViewById(R.id.chokdi);
        chokdi.setOnClickListener(v -> {
            mainActivity.onBackPressed();
            dismiss();
        });

        ImageView back = view.findViewById(R.id.back);
        back.setOnClickListener(v -> {
            mainActivity.onBackPressed();
            dismiss();
        });

        AppCompatButton button = view.findViewById(R.id.appCompatButton2);
        button.setOnClickListener(v -> {
            createplaylist(edit);
            dismiss();
        });
    }

    private void createplaylist(String edit) {

        SplashActivity.playlisttrue = SplashActivity.preferences.getInt("playlist",0);

        if (SplashActivity.playlisttrue == 1) {
            namelist = PlayListNamePref.getStringList();
        }

        namelist.add(edit);
        PlayListNamePref.saveStringList(namelist);

        ArrayList<MusicModel> arraydata = new ArrayList<>();

        for (int i = 0; i < SongsFragment.musicdata.size(); i++) {
            if (SelectAdapter.items.get(i).equals(true)) {
                MusicModel model = new MusicModel(SongsFragment.musicdata.get(i).getName(), SongsFragment.musicdata.get(i).getPath(), SongsFragment.musicdata.get(i).getDuration(), SongsFragment.musicdata.get(i).getArtist(), SongsFragment.musicdata.get(i).getPlayCount());
                arraydata.add(model);
            }
        }
        PlayListCode.playlistdata(mainActivity, edit, arraydata);
        SplashActivity.editor.putInt("playlist", 1);
        SplashActivity.editor.commit();
        PlaylistFragment.checkrecycler();
    }

}
