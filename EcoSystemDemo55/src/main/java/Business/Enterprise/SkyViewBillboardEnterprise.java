/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.Enterprise;

/**
 *
 * @author prekshapraveen
 */
public class SkyViewBillboardEnterprise extends Enterprise {
    private Business.Billboard.BillboardDirectory billboardDirectory;

    public SkyViewBillboardEnterprise(String name) {
        super(name, EnterpriseType.BILLBOARD_OPERATOR);
        billboardDirectory = new Business.Billboard.BillboardDirectory();
    }

    public Business.Billboard.BillboardDirectory getBillboardDirectory() {
        return billboardDirectory;
    }

    public void setBillboardDirectory(Business.Billboard.BillboardDirectory billboardDirectory) {
        this.billboardDirectory = billboardDirectory;
    }
}
