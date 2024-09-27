package rip.autumn.module.impl.visuals;

import java.awt.Color;
import rip.autumn.events.api.basicbus.api.annotations.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import rip.autumn.annotations.Label;
import rip.autumn.core.Autumn;
import rip.autumn.events.render.RenderGuiEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.impl.combat.AuraMod;
import rip.autumn.utils.ColorUtils;
import rip.autumn.utils.Stopwatch;
import rip.autumn.utils.render.AnimationUtils;
import rip.autumn.utils.render.RenderUtils;

@Label("TargetHUD")
@Category(ModuleCategory.VISUALS)
public final class TargetHUDMod extends Module {
   private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 180);
   private static final Color HEALTH_COLOR = new Color(192, 192, 192); // Silver-themed grayscale
   private final Stopwatch animationStopwatch = new Stopwatch();
   private EntityOtherPlayerMP target;
   private double healthBarWidth;
   private double hudHeight;

   @Listener(RenderGuiEvent.class)
   public final void onRenderGui(RenderGuiEvent event) {
      AuraMod aura = (AuraMod)Autumn.MANAGER_REGISTRY.moduleManager.getModuleOrNull(AuraMod.class);
      float scaledWidth = (float)event.getScaledResolution().getScaledWidth();
      float scaledHeight = (float)event.getScaledResolution().getScaledHeight();
      if (aura.getTarget() != null && aura.isEnabled()) {
         if (aura.getTarget() instanceof EntityOtherPlayerMP) {
            this.target = (EntityOtherPlayerMP)aura.getTarget();
            float width = 150.0F; // Increased width to fit armor and distance
            float height = 60.0F; // Increased height to fit armor and distance
            float x = scaledWidth / 2.0F - width / 2.0F;
            float y = scaledHeight / 2.0F + 80.0F;
            float health = this.target.getHealth();
            double hpPercentage = (double)(health / this.target.getMaxHealth());
            hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0D, 1.0D);
            double hpWidth = 92.0D * hpPercentage;
            int healthColor = HEALTH_COLOR.getRGB();
            String healthStr = String.valueOf((float)((int)this.target.getHealth()) / 2.0F);
            double distance = this.target.getDistanceToEntity(mc.thePlayer);
            String distanceStr = String.format("Distance: %.1f", distance);

            if (this.animationStopwatch.elapsed(15L)) {
               this.healthBarWidth = AnimationUtils.animate(hpWidth, this.healthBarWidth, 0.3529999852180481D);
               this.hudHeight = AnimationUtils.animate(height, this.hudHeight, 0.10000000149011612D);
               this.animationStopwatch.reset();
            }

            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            RenderUtils.prepareScissorBox(x, y, x + width, (float)((double)y + this.hudHeight));
            Gui.drawRect((double)x, (double)y, (double)(x + width), (double)(y + height), BACKGROUND_COLOR.getRGB());

            // Health bar
            Gui.drawRect((double)(x + 40.0F), (double)(y + 15.0F), (double)(x + 40.0F) + this.healthBarWidth, (double)(y + 25.0F), healthColor);
            mc.fontRendererObj.drawStringWithShadow(healthStr, x + 40.0F + 46.0F - (float)mc.fontRendererObj.getStringWidth(healthStr) / 2.0F, y + 16.0F, -1);

            // Player name
            mc.fontRendererObj.drawStringWithShadow(this.target.getName(), x + 40.0F, y + 2.0F, -1);

            // Distance
            mc.fontRendererObj.drawStringWithShadow(distanceStr, x + 40.0F, y + 30.0F, -1);

            // Armor display
            for (int i = 0; i < 4; i++) {
               if (this.target.getCurrentArmor(3 - i) != null) {
                  // Drawing armor background with the same color as target HUD background
                  Gui.drawRect((int)(x + 40 + (i * 20)), (int)(y + 45.0F), (int)(x + 40 + (i * 20) + 18), (int)(y + 63.0F), BACKGROUND_COLOR.getRGB());
                  mc.getRenderItem().renderItemIntoGUI(this.target.getCurrentArmor(3 - i), (int)(x + 40 + (i * 20)), (int)(y + 45.0F));
               }
            }

            // Player model
            GuiInventory.drawEntityOnScreen((int)(x + 20.0F), (int)(y + 55.0F), 25, this.target.rotationYaw, this.target.rotationPitch, this.target);

            GL11.glDisable(GL11.GL_SCISSOR_TEST);
         }
      } else {
         this.healthBarWidth = 92.0D;
         this.hudHeight = 0.0D;
         this.target = null;
      }

   }
}
