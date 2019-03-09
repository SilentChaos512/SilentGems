package net.silentchaos512.gems.compat.rei;

import me.shedaniel.rei.gui.widget.HighlightableWidget;
import me.shedaniel.rei.gui.widget.LabelWidget;
import net.silentchaos512.gems.api.chaos.ChaosEmissionRate;

import java.awt.*;

public class ChaosEmissionLabelWidget extends LabelWidget implements HighlightableWidget {
    private final int amount;

    public ChaosEmissionLabelWidget(int x, int y, int amount) {
        super(x, y, textFromAmount(amount));
        this.amount = amount;
    }

    private static String textFromAmount(int amount) {
        ChaosEmissionRate emissionRate = ChaosEmissionRate.fromAmount(amount);
        return emissionRate.getEmissionText().getFormattedText();
    }

    @Override
    public Rectangle getBounds() {
        int width = fontRenderer.getStringWidth(text);
        return new Rectangle(x - width / 2 - 1, y - 5, width + 2, 14);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        int colour = -1;
        if (isHighlighted(mouseX, mouseY))
            colour = 16777120;
        String line = this.text + (isHighlighted(mouseX, mouseY) ? " (" + this.amount + ")" : "");
        drawCenteredString(fontRenderer, line, x, y, colour);
    }
}
