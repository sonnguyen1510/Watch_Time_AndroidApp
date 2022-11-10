package com.example.watchtime.source.Object;

import java.io.Serializable;
import java.util.ArrayList;

public class Timezone implements Serializable {
    public String group;
    public ArrayList<zones> zones;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public ArrayList<Timezone.zones> getZones() {
        return zones;
    }

    public void setZones(ArrayList<Timezone.zones> zones) {
        this.zones = zones;
    }

    public Timezone(String group, ArrayList<zones> zones) {
        this.group = group;
        this.zones = zones;
    }

    public static class zones implements Serializable {
        public String name;
        public String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public zones(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}
