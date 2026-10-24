package net.pand0ra.o3hux.atmosphere.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.pand0ra.o3hux.atmosphere.AtmosphereConfig;

public class AtmosphereMenuScreen extends Screen {
    private static final int PANEL_WIDTH = 260;
    private static final int ROW_HEIGHT = 24;
    private static final int ROW_GAP = 4;

    private final AtmosphereConfig cfg = AtmosphereConfig.get();

    public AtmosphereMenuScreen() {
        super(Text.literal("Atmosphere"));
    }

    @Override
    protected void init() {
        int left = (this.width - PANEL_WIDTH) / 2;
        int top = 50;

        this.addDrawableChild(ButtonWidget.builder(
                toggleLabel("Снег на земле", cfg.snowEnabled),
                b -> {
                    cfg.snowEnabled = !cfg.snowEnabled;
                    b.setMessage(toggleLabel("Снег на земле", cfg.snowEnabled));
                    AtmosphereConfig.save();
                }
        ).dimensions(left, top, PANEL_WIDTH, 20).build());
        top += ROW_HEIGHT + ROW_GAP;

        this.addDrawableChild(ButtonWidget.builder(
                toggleLabel("Снежинки", cfg.particlesEnabled),
                b -> {
                    cfg.particlesEnabled = !cfg.particlesEnabled;
                    b.setMessage(toggleLabel("Снежинки", cfg.particlesEnabled));
                    AtmosphereConfig.save();
                }
        ).dimensions(left, top, PANEL_WIDTH, 20).build());
        top += ROW_HEIGHT + ROW_GAP;

        this.addDrawableChild(new SliderWidget(
                left, top, PANEL_WIDTH, 20,
                Text.literal("Плотность снега: " + pct(cfg.particleDensity) + "%"),
                cfg.particleDensity / 2.0f
        ) {
            @Override
            protected void updateMessage() {
                this.setMessage(Text.literal("Плотность снега: " + pct(cfg.particleDensity) + "%"));
            }

            @Override
            protected void applyValue() {
                cfg.particleDensity = (float) this.value * 2.0f;
                AtmosphereConfig.save();
            }
        });
        top += ROW_HEIGHT + ROW_GAP;

        this.addDrawableChild(new SliderWidget(
                left, top, PANEL_WIDTH, 20,
                Text.literal("Видимость тумана: " + pct(cfg.fogVisibility) + "%"),
                (cfg.fogVisibility - 0.1f) / 1.9f
        ) {
            @Override
            protected void updateMessage() {
                this.setMessage(Text.literal("Видимость тумана: " + pct(cfg.fogVisibility) + "%"));
            }

            @Override
            protected void applyValue() {
                cfg.fogVisibility = 0.1f + (float) this.value * 1.9f;
                AtmosphereConfig.save();
            }
        });
        top += ROW_HEIGHT + ROW_GAP;

        this.addDrawableChild(ButtonWidget.builder(
                toggleLabel("Тёплый свет", cfg.warmLight),
                b -> {
                    cfg.warmLight = !cfg.warmLight;
                    b.setMessage(toggleLabel("Тёплый свет", cfg.warmLight));
                    AtmosphereConfig.save();
                }
        ).dimensions(left, top, PANEL_WIDTH, 20).build());
        top += ROW_HEIGHT + ROW_GAP;

        this.addDrawableChild(new SliderWidget(
                left, top, PANEL_WIDTH, 20,
                Text.literal("Сила тёплого света: " + pct(cfg.warmLightStrength) + "%"),
                cfg.warmLightStrength
        ) {
            @Override
            protected void updateMessage() {
                this.setMessage(Text.literal("Сила тёплого света: " + pct(cfg.warmLightStrength) + "%"));
            }

            @Override
            protected void applyValue() {
                cfg.warmLightStrength = (float) this.value;
                AtmosphereConfig.save();
            }
        });
        top += ROW_HEIGHT + ROW_GAP;

        int bottom = this.height - 40;
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Сбросить настройки"),
                b -> {
                    cfg.snowEnabled = true;
                    cfg.particlesEnabled = true;
                    cfg.particleDensity = 1.0f;
                    cfg.fogVisibility = 1.0f;
                    cfg.warmLight = true;
                    cfg.warmLightStrength = 1.0f;
                    AtmosphereConfig.save();
                    this.clearAndInit();
                }
        ).dimensions(left, bottom, PANEL_WIDTH, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Готово"),
                b -> this.close()
        ).dimensions(left, bottom + ROW_HEIGHT, PANEL_WIDTH, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        int centerX = this.width / 2;

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("Atmosphere"),
                centerX,
                20,
                0xFFD77A
        );

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("Нажми G в игре, чтобы открыть это меню"),
                centerX,
                34,
                0xAAAAAA
        );

        int panelLeft = (this.width - PANEL_WIDTH) / 2 - 10;
        int panelTop = 44;
        int panelBottom = this.height - 50;
        context.fill(panelLeft, panelTop, panelLeft + PANEL_WIDTH + 20, panelBottom, 0x80000000);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private static Text toggleLabel(String name, boolean value) {
        return Text.literal(name + ": " + (value ? "§aВКЛ" : "§cВЫКЛ"));
    }

    private static int pct(float value) {
        return Math.round(value * 100.0f);
    }
}
