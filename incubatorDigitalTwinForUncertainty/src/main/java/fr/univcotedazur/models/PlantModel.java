package fr.univcotedazur.models;

import uDataTypes.UReal;

public interface PlantModel {
    PlantSnapshot doStep(double timeStep, boolean on_heater, UReal in_room_temperature);

    UReal getT();
    void setT(UReal t);
    UReal getTHeater();
    void setTHeater(UReal t_heater);


}
