package rip.autumn.module.impl.visuals.hud.impl;

import java.awt.Color;
import java.util.Collection;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import rip.autumn.module.impl.visuals.hud.Component;
import rip.autumn.module.impl.visuals.hud.HUDMod;
import rip.autumn.utils.render.Palette;

public final class InfoComponent extends Component {
   public InfoComponent(HUDMod parent) {
      super(parent);
   }

   public void draw(ScaledResolution sr) {
      HUDMod hud = this.getParent();
      int height = sr.getScaledHeight();
      int width = sr.getScaledWidth();
      FontRenderer fr = hud.defaultFont.getValue() ? mc.fontRendererObj : mc.fontRenderer;
      int color = Palette.fade((Color)hud.color.getValue()).getRGB();

      // Display FPS on the left
      String fps = String.format("FPS§7: %d", Minecraft.getDebugFPS());
      int fontHeight = 9;

      if (mc.currentScreen instanceof GuiChat) {
         fontHeight += 15;
      }

      fr.drawStringWithShadow(fps, 2.0F, (float)(height - fontHeight), color);

      // Display Build ID on the bottom right
      String buildId = "Away (0001) - 1.01 [Developer]";
      fr.drawStringWithShadow(buildId, (float)(width - fr.getStringWidth(buildId)) - 2.0F, (float)(height - fontHeight), color);

      // Display Active Potion Effects on the bottom right above Build ID
      Collection<PotionEffect> activePotions = Minecraft.getMinecraft().thePlayer.getActivePotionEffects();
      int potionY = height - fontHeight - 12; // Start above the build ID

      for (PotionEffect effect : activePotions) {
         Potion potion = Potion.potionTypes[effect.getPotionID()];
         String potionName = potion.getName().replace("potion.", "");
         potionName = formatPotionName(potionName);
         String potionText = String.format("%s (%d) %s", potionName, effect.getAmplifier() + 1, Potion.getDurationString(effect));
         int potionColor = potion.getLiquidColor();

         fr.drawStringWithShadow(potionText, (float)(width - fr.getStringWidth(potionText)) - 2.0F, (float)potionY, potionColor);
         potionY -= 12; // Move up for the next potion effect
      }
   }

   private String formatPotionName(String rawName) {
      switch (rawName) {
         case "moveSpeed":
            return "Speed";
         case "moveSlowdown":
            return "Slowness";
         case "digSpeed":
            return "Haste";
         case "digSlowdown":
            return "Mining Fatigue";
         case "harm":
            return "Instant Damage";
         case "heal":
            return "Instant Health";
         case "jump":
            return "Jump Boost";
         case "confusion":
            return "Nausea";
         case "regeneration":
            return "Regeneration";
         case "resistance":
            return "Resistance";
         case "fireResistance":
            return "Fire Resistance";
         case "waterBreathing":
            return "Water Breathing";
         case "invisibility":
            return "Invisibility";
         case "blindness":
            return "Blindness";
         case "nightVision":
            return "Night Vision";
         case "hunger":
            return "Hunger";
         case "weakness":
            return "Weakness";
         case "poison":
            return "Poison";
         case "wither":
            return "Wither";
         case "healthBoost":
            return "Health Boost";
         case "absorption":
            return "Absorption";
         case "saturation":
            return "Saturation";
         default:
            return rawName.substring(0, 1).toUpperCase() + rawName.substring(1);
      }
   }
}
