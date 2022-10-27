package com.example.watchtime.source.TimeAPI;

public class Timezone {
        private String timezone;
        private float timezone_offset;
        private float timezone_offset_with_dst;
        private String date;
        private String date_time;
        private String date_time_txt;
        private String date_time_wti;
        private String date_time_ymd;
        private float date_time_unix;
        private String time_24;
        private String time_12;
        private float week;
        private float month;
        private float year;
        private String year_abbr;
        private boolean is_dst;
        private float dst_savings;


        // Getter Methods

        public String getTimezone() {
            return timezone;
        }

        public float getTimezone_offset() {
            return timezone_offset;
        }

        public float getTimezone_offset_with_dst() {
            return timezone_offset_with_dst;
        }

        public String getDate() {
            return date;
        }

        public String getDate_time() {
            return date_time;
        }

        public String getDate_time_txt() {
            return date_time_txt;
        }

        public String getDate_time_wti() {
            return date_time_wti;
        }

        public String getDate_time_ymd() {
            return date_time_ymd;
        }

        public float getDate_time_unix() {
            return date_time_unix;
        }

        public String getTime_24() {
            return time_24;
        }

        public String getTime_12() {
            return time_12;
        }

        public float getWeek() {
            return week;
        }

        public float getMonth() {
            return month;
        }

        public float getYear() {
            return year;
        }

        public String getYear_abbr() {
            return year_abbr;
        }

        public boolean getIs_dst() {
            return is_dst;
        }

        public float getDst_savings() {
            return dst_savings;
        }

        // Setter Methods

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        public void setTimezone_offset(float timezone_offset) {
            this.timezone_offset = timezone_offset;
        }

        public void setTimezone_offset_with_dst(float timezone_offset_with_dst) {
            this.timezone_offset_with_dst = timezone_offset_with_dst;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setDate_time(String date_time) {
            this.date_time = date_time;
        }

        public void setDate_time_txt(String date_time_txt) {
            this.date_time_txt = date_time_txt;
        }

        public void setDate_time_wti(String date_time_wti) {
            this.date_time_wti = date_time_wti;
        }

        public void setDate_time_ymd(String date_time_ymd) {
            this.date_time_ymd = date_time_ymd;
        }

        public void setDate_time_unix(float date_time_unix) {
            this.date_time_unix = date_time_unix;
        }

        public void setTime_24(String time_24) {
            this.time_24 = time_24;
        }

        public void setTime_12(String time_12) {
            this.time_12 = time_12;
        }

        public void setWeek(float week) {
            this.week = week;
        }

        public void setMonth(float month) {
            this.month = month;
        }

        public void setYear(float year) {
            this.year = year;
        }

        public void setYear_abbr(String year_abbr) {
            this.year_abbr = year_abbr;
        }

        public void setIs_dst(boolean is_dst) {
            this.is_dst = is_dst;
        }

        public void setDst_savings(float dst_savings) {
            this.dst_savings = dst_savings;
        }

}
