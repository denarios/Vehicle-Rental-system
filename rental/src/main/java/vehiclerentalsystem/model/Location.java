package vehiclerentalsystem.model;

public class Location {
   private String state;
    private String district;
    private String pincode;
    public Location(String state,String district,String pincode){
        this.district = district;
        this.state = state;
        this.pincode = pincode;
    }
    // also getter 
    public String getState(){
        return state;
    }
    public String getDistict(){
        return district;
    }
        public String getPinCode(){
        return pincode;
    }
}
