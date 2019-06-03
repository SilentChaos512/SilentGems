package net.silentchaos512.gems.guide.page;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.lib.guidebook.GuideBook;
import net.silentchaos512.lib.guidebook.internal.GuiGuideBase;
import net.silentchaos512.lib.guidebook.page.GuidePage;

public class PageToolPart extends GuidePage {
    public final ToolPart part;

    public PageToolPart(GuideBook book, int key, ToolPart part) {
        this(book, key, 0, part);
    }

    public PageToolPart(GuideBook book, int key, int priority, ToolPart part) {
        super(book, key, priority);
        this.part = part;
    }

    @Override
    public String getInfoText() {
        ItemStack repStack = part.getCraftingStack();
        String title = part.getDisplayNamePrefix(repStack) + " " + part.getDisplayName(repStack);
        String str = book.i18n.translate("guide", "toolPartPage",
                title,
                part.getTier().toString(),
                part.getCraftingOreDictName());
        str = doTextReplacements(str);
        str += super.getInfoText();
        return str;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void initGui(GuiGuideBase gui, int startX, int startY) {
        super.initGui(gui, startX, startY);

        if (!part.getCraftingStack().isEmpty())
            gui.addOrModifyItemRenderer(part.getCraftingStack(), 5, 5, 1.0f, false);
    }
}
