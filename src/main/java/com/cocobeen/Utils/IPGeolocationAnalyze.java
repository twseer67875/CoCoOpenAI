package com.cocobeen.Utils;

import org.json.JSONObject;

import java.io.Serializable;

public class IPGeolocationAnalyze implements Serializable {
    private JSONObject object = null;
    private String ip = "";
    private String hostname = "";
    private String continent_code = "";
    private String continent_name = "";
    private String country_code2 = "";
    private String country_code3 = "";
    private String country_name = "";
    private String country_capital = "";
    private String state_prov = "";
    private String district = "";
    private String city = "";
    private String zipcode = "";
    private String latitude = "";
    private String longitude = "";
    private boolean is_eu = false;
    private String calling_code = "";
    private String country_tld = "";
    private String languages = "";
    private String country_flag = "";
    private String geoname_id = "";
    private String isp = "";
    private String connection_type = "";
    private String organization = "";
    private String currency_code = "";
    private String currency_name = "";
    private String currency_symbol = "";
    private String time_zone_name = "";
    private int time_zone_offset = 0;
    private String time_zone_current_time = "";
    private double time_zone_current_time_unix = 0;
    private boolean time_zone_is_dst = false;
    private int time_zone_dst_savings = 0;

    public IPGeolocationAnalyze(JSONObject jsonObject){
        if (jsonObject == null || jsonObject.has("message")) {
            return;
        }
        object = jsonObject;

        ip = object.getString("ip");
        continent_code = object.getString("continent_code");
        continent_name = object.getString("continent_name");

        country_code2 = object.getString("country_code2");
        country_code3 = object.getString("country_code3");
        country_name = object.getString("country_name");
        country_capital = object.getString("country_capital");

        state_prov = object.getString("state_prov");

        district = object.getString("district");

        city = object.getString("city");

        zipcode = object.getString("zipcode");

        latitude = object.getString("latitude");

        longitude = object.getString("longitude");

        is_eu = object.getBoolean("is_eu");

        calling_code = object.getString("calling_code");

        country_tld = object.getString("country_tld");

        languages = object.getString("languages");

        country_flag = object.getString("country_flag");

        geoname_id = object.getString("geoname_id");

        isp = object.getString("isp");

        connection_type = object.getString("connection_type");

        organization = object.getString("organization");

        JSONObject currency = object.getJSONObject("currency");
        currency_code = currency.getString("code");
        currency_name = currency.getString("name");
        currency_symbol = currency.getString("symbol");

        JSONObject time_zone = object.getJSONObject("time_zone");
        time_zone_name = time_zone.getString("name");
        time_zone_offset = time_zone.getInt("offset");
        time_zone_current_time = time_zone.getString("current_time");
        time_zone_current_time_unix = time_zone.getDouble("current_time_unix");
        time_zone_is_dst = time_zone.getBoolean("is_dst");
        time_zone_dst_savings = time_zone.getInt("dst_savings");
    }

    public JSONObject getJSON(){
        return object;
    }

    public String getIP(){
        return ip;
    }

    public String getHostname(){
        return hostname;
    }

    public String getContinentCode(){
        return continent_code;
    }

    public String getContinentName(){
        return continent_name;
    }

    public String getCountryCode2(){
        return country_code2;
    }

    public String getCountryCode3(){
        return country_code3;
    }

    public String getCountryName(){
        return country_name;
    }

    public String getCountryCapital(){
        return country_capital;
    }

    public String getStateProv(){
        return state_prov;
    }

    public String getDistrict(){
        return district;
    }

    public String getCity(){
        return city;
    }

    public String getZipcode(){
        return zipcode;
    }

    public String getLatitude(){
        return latitude;
    }

    public String getLongitude(){
        return longitude;
    }

    public boolean getIsEu(){
        return is_eu;
    }

    public String getCallingCode(){
        return calling_code;
    }

    public String getCountryTLD(){
        return country_tld;
    }

    public String getLanguages(){
        return languages;
    }

    public String getCountryFlagUrl(){
        return country_flag;
    }

    public String getGeonameID(){
        return geoname_id;
    }

    public String getISP(){
        return isp;
    }

    public String getConnectionType(){
        return connection_type;
    }

    public String getOrganization(){
        return organization;
    }

    public String getCurrencyCode(){
        return currency_code;
    }

    public String getCurrencyName(){
        return currency_name;
    }

    public String getCurrencySymbol(){
        return currency_symbol;
    }

    public String getTimeZoneName(){
        return time_zone_name;
    }

    public int getTimeZoneOffset(){
        return time_zone_offset;
    }

    public String getTimeZoneCurrentTime(){
        return time_zone_current_time;
    }

    public double getTimeZoneCurrentTimeUnix(){
        return time_zone_current_time_unix;
    }

    public boolean getTimeZoneIsDst(){
        return time_zone_is_dst;
    }

    public int getTimeZoneDstSavings(){
        return time_zone_dst_savings;
    }
}