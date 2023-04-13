package fr.univcotedazur.models;

import uDataTypes.UReal;

public class PlantSnapshot {
    public UReal boxTemperature;
    public UReal heaterTemperature;
    public UReal power_in;
    public UReal power_transfer_heat;
    public UReal total_power_heater;
    public UReal power_out_box;
    public UReal total_power_box;

    public UReal der_T;

    public UReal der_T_heater;

    public Boolean in_heather;

    public PlantSnapshot(UReal boxTemp, UReal heaterTemp) {
        this.boxTemperature = boxTemp;
        this.heaterTemperature = heaterTemp;
    }

    public PlantSnapshot(UReal boxTemp, UReal heaterTemp, UReal power_in, UReal power_transfer_heat, UReal total_power_heater, UReal power_out_box, UReal total_power_box, UReal der_T, UReal der_T_heater, Boolean in_heather) {
        this.boxTemperature = boxTemp;
        this.heaterTemperature = heaterTemp;
        this.power_in = power_in;
        this.power_transfer_heat = power_transfer_heat;
        this.total_power_heater = total_power_heater;
        this.power_out_box = power_out_box;
        this.total_power_box = total_power_box;
        this.der_T = der_T;
        this.der_T_heater = der_T_heater;
        this.in_heather = in_heather;
    }
}
