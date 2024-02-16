package com.db.musicplayerdb.Model;

public class MusicModel
{
    String name;
    String path;
    String duration;
    String artist;
    int playCount;


    public MusicModel(String name, String path, String duration, String artist, int playCount) {
        this.name = name;
        this.path = path;
        this.duration = duration;
        this.artist = artist;
        this.playCount = playCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public void incrementPlayCount() {
        playCount++;
    }


}
