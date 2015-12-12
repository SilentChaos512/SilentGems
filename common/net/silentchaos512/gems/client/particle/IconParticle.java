/**
 * ****************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 * *****************************************************************************************************************
 */

package net.silentchaos512.gems.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

@SideOnly(Side.CLIENT)
public class IconParticle implements IIcon {

  private final String iconName;
  private final int iconWidth;
  private final int iconHeight;
  private final float minU;
  private final float minV;
  private final float maxU;
  private final float maxV;
  
  public IconParticle(String iconName, int width, int height) {
    
    this(iconName, width, height, 0, 0, width, height);
  }

  /**
   * Constructs a new {@link IconParticle} to use for the {@link net.minecraft.client.particle.EntityFX EntityFX} class.
   *
   * @param iconName
   *          The name of the icon. Can be anything.
   * @param sheetWidth
   *          The width (in pixels) of the texture sheet used. The Minecraft Particle Sheet is {@code 128px} wide.
   * @param sheetHeight
   *          The height (in pixels) of the texture sheet used. The Minecraft Particle Sheet is {@code 128px} high.
   * @param iconCoordU
   *          The horizontal location (in pixels) of the particle icon.
   * @param iconCoordV
   *          The vertical location (in pixels) of the particle icon.
   * @param iconWidth
   *          The width (in pixels) of the particle icon.
   * @param iconHeight
   *          The height (in pixels) of the particle icon.
   */
  public IconParticle(String iconName, int sheetWidth, int sheetHeight, int iconCoordU,
      int iconCoordV, int iconWidth, int iconHeight) {
    this.iconName = iconName;

    this.iconWidth = iconWidth;
    this.iconHeight = iconHeight;

    float ratioU = 1.0F / sheetWidth;
    float ratioV = 1.0F / sheetHeight;

    this.minU = ratioU * iconCoordU;
    this.minV = ratioV * iconCoordV;
    this.maxU = ratioU * (iconCoordU + iconWidth);
    this.maxV = ratioV * (iconCoordV + iconHeight);
  }

  @Override
  public int getIconWidth() {

    return this.iconWidth;
  }

  @Override
  public int getIconHeight() {

    return this.iconHeight;
  }

  @Override
  public float getMinU() {

    return this.minU;
  }

  @Override
  public float getMaxU() {

    return this.maxU;
  }

  /**
   * Gets an interpolated U coordinate on the icon.
   *
   * @param amount
   *          the amount the U value is shifted on the icon. Regular values are between 0.0D and {@code iconWidth}.
   * @return {@code minU}, if {@code amount} is {@code 0.0D} and {@code maxU}, if {@code amount} is {@code iconWidth}.
   *         Other arguments return in-between values.
   */
  @Override
  public float getInterpolatedU(double amount) {

    float deltaU = this.maxU - this.minU;
    return this.minU + deltaU * (float) amount / this.iconWidth;
  }

  @Override
  public float getMinV() {

    return this.minV;
  }

  @Override
  public float getMaxV() {

    return this.maxV;
  }

  /**
   * Gets an interpolated V coordinate on the icon.
   *
   * @param amount
   *          the amount the V value is shifted on the icon. Regular values are between 0.0D and {@code iconHeight}.
   * @return {@code minV}, if {@code amount} is {@code 0.0D} and {@code maxV}, if {@code amount} is {@code iconHeight}.
   *         Other arguments return in-between values.
   */
  @Override
  public float getInterpolatedV(double amount) {

    float deltaV = this.maxV - this.minV;
    return this.minV + deltaV * (float) amount / this.iconHeight;
  }

  @Override
  public String getIconName() {

    return this.iconName;
  }
}
