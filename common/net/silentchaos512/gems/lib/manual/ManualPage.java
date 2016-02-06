//package net.silentchaos512.gems.lib.manual;
//
//import java.util.List;
//
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.SideOnly;
//import net.minecraft.client.gui.GuiButton;
//import net.minecraft.item.ItemStack;
//
//public abstract class ManualPage {
//
//  public String unlocalizedName;
//  public boolean skipRegistry;
//
//  public ManualPage(String unlocalizedName) {
//
//    this.unlocalizedName = unlocalizedName;
//  }
//
//  @SideOnly(Side.CLIENT)
//  public abstract void renderScreen(IGuiManualEntry gui, int mx, int my);
//
//  @SideOnly(Side.CLIENT)
//  public void updateScreen() {
//
//  }
//
//  @SideOnly(Side.CLIENT)
//  public void onOpened(IGuiManualEntry gui) {
//
//  }
//
//  @SideOnly(Side.CLIENT)
//  public void onClosed(IGuiManualEntry gui) {
//
//  }
//
//  @SideOnly(Side.CLIENT)
//  public void onActionPerformed(IGuiManualEntry gui, GuiButton button) {
//
//  }
//
//  @SideOnly(Side.CLIENT)
//  public void onKeyPressed(char c, int key) {
//
//  }
//
//  public void onPageAdded(ManualEntry entry, int index) {
//
//  }
//
//  public List<ItemStack> getDisplayedRecipes() {
//
//    return null;
//  }
//
//  public String getUnlocalizedName() {
//
//    return unlocalizedName;
//  }
//
//  public ManualPage setSkipRegistry() {
//
//    skipRegistry = true;
//    return this;
//  }
//}
