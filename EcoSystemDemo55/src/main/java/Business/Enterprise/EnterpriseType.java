/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.Enterprise;

/**
 *
 * @author prekshapraveen
 */
public enum EnterpriseType {
    BILLBOARD_OPERATOR("Billboard Operator"),
    AD_AGENCY("Advertising Agency"),
    CITY_SERVICES("City Services"),
    POWER_UTILITY("Power Utility");

    private String value;

    private EnterpriseType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
