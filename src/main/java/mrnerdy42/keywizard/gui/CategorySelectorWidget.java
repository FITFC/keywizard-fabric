package mrnerdy42.keywizard.gui;

import java.util.ArrayList;

import mrnerdy42.keywizard.util.KeyBindingUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TickableElement;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class CategorySelectorWidget extends PressableWidget implements TickableElement {
	
	public KeyWizardScreen keyWizardScreen;
	public boolean extended = false;
	
	private BindingCategoryListWidget list;
	
	public CategorySelectorWidget(KeyWizardScreen keyWizardScreen, int x, int y, int width, int height) {
		super(x, y, width, height, Text.of(""));
		this.keyWizardScreen = keyWizardScreen;
		MinecraftClient c = MinecraftClient.getInstance();
		int listItemHeight = c.textRenderer.fontHeight + 7;
		int listHeight = KeyBindingUtil.getCategoriesWithDynamics().size() * listItemHeight + 10;
		this.list = new BindingCategoryListWidget(c, this.y + this.height, this.x, this.width, listHeight, listItemHeight);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		boolean listClicked = this.list.mouseClicked(mouseX, mouseY, button);
		boolean thisClicked = super.mouseClicked(mouseX, mouseY, button);
		if (! (listClicked || thisClicked)) {
			this.extended = false;
		}
		return listClicked || thisClicked;
	}
	
	@Override
	public void onPress() {
		this.playDownSound(MinecraftClient.getInstance().getSoundManager());
		this.extended = !this.extended;
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		super.render(matrices, mouseX, mouseY, delta);
		this.list.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public void tick() {
		this.setMessage(new TranslatableText(this.getSelctedCategory()));
    	this.list.visible = this.extended;
		this.hovered = this.extended;
	}
	
	public String getSelctedCategory() {
		if (this.list.getSelected() == null) {
			return null;
		}
    	return ((BindingCategoryListWidget.CategoryEntry) this.list.getSelected()).category;
    }
    
	private class BindingCategoryListWidget extends FreeFormListWidget<BindingCategoryListWidget.CategoryEntry> {

		public BindingCategoryListWidget(MinecraftClient client, int top, int left, int width, int height, int itemHeight) {
			super(client, top, left, width, height, itemHeight);
			
			for (String c : KeyBindingUtil.getCategoriesWithDynamics()) {
				this.addEntry(new CategoryEntry(c));
			}
			this.setSelected(this.children().get(0));
		}
		
		public class CategoryEntry extends FreeFormListWidget<BindingCategoryListWidget.CategoryEntry>.Entry{

			private final String category;

			public CategoryEntry(String category) {
			    this.category = category;
			}

			@Override
			public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
				client.textRenderer.drawWithShadow(matrices, new TranslatableText(this.category), x + 3 , y + 2, 0xFFFFFFFF);
			}

		}
	}	
	
}
