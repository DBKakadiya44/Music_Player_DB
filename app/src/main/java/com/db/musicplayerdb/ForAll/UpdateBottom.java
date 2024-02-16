package com.db.musicplayerdb.ForAll;

import com.db.musicplayerdb.AlbumActivity;
import com.db.musicplayerdb.Fragments.Activity.FavoriteActivity;
import com.db.musicplayerdb.Fragments.Activity.LastAddActivity;
import com.db.musicplayerdb.Fragments.Activity.MostPlayActivity;
import com.db.musicplayerdb.Fragments.Activity.RecentActivity;
import com.db.musicplayerdb.Fragments.AlbumsFragment;
import com.db.musicplayerdb.Fragments.ArtistFragment;
import com.db.musicplayerdb.Fragments.FoldersFragment;
import com.db.musicplayerdb.Fragments.PlaylistFragment;
import com.db.musicplayerdb.Fragments.SongsFragment;
import com.db.musicplayerdb.QueueActivity;

public class UpdateBottom {
    public static void updateAllBottom() {
        if (SongsFragment.isCreated) {
            SongsFragment.checkcurrentmusic();
        }
        if (QueueActivity.isCreated) {
            QueueActivity.checkcurrentmusic();
        }
        if (AlbumActivity.isCreated) {
            AlbumActivity.checkcurrentmusic();
        }
        if (FoldersFragment.isCreated) {
            FoldersFragment.checkcurrentmusic();
        }
        if (LastAddActivity.isCreated) {
            LastAddActivity.checkcurrentmusic();
        }
        if (RecentActivity.isCreated) {
            RecentActivity.checkcurrentmusic();
        }
        if (MostPlayActivity.isCreated) {
            MostPlayActivity.checkcurrentmusic();
        }
        if(PlaylistFragment.isCreated){
            PlaylistFragment.checkcurrentmusic();
        }
        if (ArtistFragment.isCreated) {
            ArtistFragment.checkcurrentmusic();
        }
        if (AlbumsFragment.isCreated) {
            AlbumsFragment.checkcurrentmusic();
        }
        if(FavoriteActivity.isCreated){
            FavoriteActivity.checkcurrentmusic();
        }

    }
}
