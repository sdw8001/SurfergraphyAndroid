package com.surfergraphy.surf.base.data;

import com.surfergraphy.surf.base.BaseType;

import java.util.ArrayList;

public class Nation {
    private BaseType.LocationType nationLocationType;
    private ArrayList<BaseType.LocationType> surfingSpots;

    public Nation(BaseType.LocationType nationLocationType, ArrayList<BaseType.LocationType> surfingSpots) {
        this.nationLocationType = nationLocationType;
        this.surfingSpots = surfingSpots;
    }

    public BaseType.LocationType getNationLocationType() {
        return nationLocationType;
    }

    public void setNationLocationType(BaseType.LocationType nationLocationType) {
        this.nationLocationType = nationLocationType;
    }

    public ArrayList<BaseType.LocationType> getSurfingSpots() {
        return surfingSpots;
    }

    public void setSurfingSpots(ArrayList<BaseType.LocationType> surfingSpots) {
        this.surfingSpots = surfingSpots;
    }
}
