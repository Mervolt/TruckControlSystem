package controlsystem;

/**
 * enum designed to describe current situation of truck placement
 * e.g. more trucks in first lane than in the second then firstLaneSided
 */
public enum TruckPlacement {
    firstLaneSided,
    secondLaneSided,
    balanced;
}
