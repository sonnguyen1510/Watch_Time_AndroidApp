package com.example.watchtime.source.Database.Timer;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AlertSong {
    @PrimaryKey
    @NonNull
    public String songname;
    public int location;

    public AlertSong(@NonNull String songname, int location) {
        this.songname = songname;
        this.location = location;
    }

    @NonNull
    public String getSongname() {
        return songname;
    }

    public void setSongname(@NonNull String songname) {
        this.songname = songname;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }


}
