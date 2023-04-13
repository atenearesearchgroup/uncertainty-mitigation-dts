package fr.univcotedazur.utils;

public enum ConfidenceLevel {
    SixtyEightPercent(1),
    NinetyFivePercent(2),
    NinetyNineDotSevenPercent(3);

    public int value;
    private ConfidenceLevel(int value){
        this.value = value;
    }

}
