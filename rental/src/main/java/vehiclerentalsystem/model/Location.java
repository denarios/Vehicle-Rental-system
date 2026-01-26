package vehiclerentalsystem.model;

/**
 * Location model for store addresses.
 * 
 * Enhanced with city and state for location-based search.
 */
public class Location {

    private String state;
    private String city;
    private String district;
    private String pincode;

    public Location(String state, String city, String district, String pincode) {
        this.state = state;
        this.city = city;
        this.district = district;
        this.pincode = pincode;
    }

    // Legacy constructor for backward compatibility
    public Location(String state, String district, String pincode) {
        this.state = state;
        this.city = district; // Use district as city for old data
        this.district = district;
        this.pincode = pincode;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getPincode() {
        return pincode;
    }

    @Override
    public String toString() {
        return city + ", " + state + " - " + pincode;
    }
}
