package io.jenkins.util;

public class IP2LocationRecord {

    private String ip;
    private String country_short;
    private String country_long;
    private String region;
    private String city;
    private String isp;
    private String latitude;
    private String longitude;
    private String domain;
    private String zipcode;
    private String timezone;
    private String netspeed;
    private String idd_code;
    private String area_code;
    private String weather_code;
    private String weather_name;
    private String mcc;
    private String mnc;
    private String mobile_brand;
    private String elevation;
    private String usage_type;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCountry_short() {
        return country_short;
    }

    public void setCountry_short(String country_short) {
        this.country_short = country_short;
    }

    public String getCountry_long() {
        return country_long;
    }

    public void setCountry_long(String country_long) {
        this.country_long = country_long;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getNetspeed() {
        return netspeed;
    }

    public void setNetspeed(String netspeed) {
        this.netspeed = netspeed;
    }

    public String getIdd_code() {
        return idd_code;
    }

    public void setIdd_code(String idd_code) {
        this.idd_code = idd_code;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getWeather_code() {
        return weather_code;
    }

    public void setWeather_code(String weather_code) {
        this.weather_code = weather_code;
    }

    public String getWeather_name() {
        return weather_name;
    }

    public void setWeather_name(String weather_name) {
        this.weather_name = weather_name;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getMobile_brand() {
        return mobile_brand;
    }

    public void setMobile_brand(String mobile_brand) {
        this.mobile_brand = mobile_brand;
    }

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public String getUsage_type() {
        return usage_type;
    }

    public void setUsage_type(String usage_type) {
        this.usage_type = usage_type;
    }

    @Override
    public String toString() {
        return "IP2LocationRecord [ip=" + ip + ", country_short=" + country_short + ", country_long=" + country_long
                + ", region=" + region + ", city=" + city + ", isp=" + isp + ", latitude=" + latitude + ", longitude="
                + longitude + ", domain=" + domain + ", zipcode=" + zipcode + ", timezone=" + timezone + ", netspeed="
                + netspeed + ", idd_code=" + idd_code + ", area_code=" + area_code + ", weather_code=" + weather_code
                + ", weather_name=" + weather_name + ", mcc=" + mcc + ", mnc=" + mnc + ", mobile_brand=" + mobile_brand
                + ", elevation=" + elevation + ", usage_type=" + usage_type + "]";
    }

}
