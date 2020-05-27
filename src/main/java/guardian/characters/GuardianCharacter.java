package guardian.characters;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import guardian.GuardianMod;
import guardian.cards.*;
import guardian.patches.AbstractCardEnum;
import guardian.patches.GuardianEnum;
import guardian.relics.ModeShifter;

import java.util.ArrayList;
import java.util.List;


public class GuardianCharacter extends CustomPlayer {
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String[] orbTextures = {"guardianResources/GuardianImages/char/orb/layer1.png", "guardianResources/GuardianImages/char/orb/layer2.png", "guardianResources/GuardianImages/char/orb/layer3.png", "guardianResources/GuardianImages/char/orb/layer4.png", "guardianResources/GuardianImages/char/orb/layer5.png", "guardianResources/GuardianImages/char/orb/layer6.png", "guardianResources/GuardianImages/char/orb/layer1d.png", "guardianResources/GuardianImages/char/orb/layer2d.png", "guardianResources/GuardianImages/char/orb/layer3d.png", "guardianResources/GuardianImages/char/orb/layer4d.png", "guardianResources/GuardianImages/char/orb/layer5d.png"};
    public static final CharacterStrings charStrings;
    public static Color cardRenderColor = GuardianMod.mainGuardianColor;

    static {
        charStrings = CardCrawlGame.languagePack.getCharacterString("Guardian");
        NAME = charStrings.NAMES[0];
        DESCRIPTION = charStrings.TEXT[0];

    }

    public float renderscale = 2.5F;
    private String atlasURL = "guardianResources/GuardianImages/char/skeleton.atlas";
    private String jsonURL = "guardianResources/GuardianImages/char/skeleton.json";
    private String jsonURLPuddle = "guardianResources/GuardianImages/char/skeletonPuddle.json";
    private String currentJson = jsonURL;
    private boolean inDefensive;
    private boolean inShattered;

    public GuardianCharacter(String name, PlayerClass setClass) {
        super(name, setClass, orbTextures, "guardianResources/GuardianImages/char/orb/vfx.png", (String)null, (String)null);


        this.initializeClass(null, "guardianResources/GuardianImages/char/shoulder2.png", "guardianResources/GuardianImages/char/shoulder.png", "guardianResources/GuardianImages/char/corpse.png", this.getLoadout(), 0.0F, -10F, 310.0F, 260.0F, new EnergyManager(3));
        this.reloadAnimation();


    }

    //TODO - Victory screens

    public CharSelectInfo getInfo() {
        return getLoadout();
    }

    @Override
    public Texture getCutsceneBg() {
        return ImageMaster.loadImage("images/scenes/greenBg.jpg");

    }

    @Override
    public List<CutscenePanel> getCutscenePanels() {
        List<CutscenePanel> panels = new ArrayList();
        panels.add(new CutscenePanel("guardianResources/GuardianImages/scenes/guardian1.png", "MONSTER_GUARDIAN_DESTROY"));
        panels.add(new CutscenePanel("guardianResources/GuardianImages/scenes/guardian2.png", "GUARDIAN_ROLL_UP"));
        panels.add(new CutscenePanel("guardianResources/GuardianImages/scenes/guardian3.png"));
        return panels;
    }

    public void reloadAnimation() {
        this.loadAnimation(atlasURL, this.currentJson, renderscale);
        this.state.setAnimation(0, "idle", true);


    }

    @Override
    public void applyStartOfCombatLogic() {
        super.applyStartOfCombatLogic();
        this.state.setTimeScale(.75F);
        this.state.setAnimation(0, "idle", true);
    }

    public void switchToDefensiveMode() {
        if (!inShattered) {
            if (!inDefensive) {
                CardCrawlGame.sound.play("GUARDIAN_ROLL_UP");
                this.stateData.setMix("idle", "defensive", 0.2F);
                this.state.setTimeScale(.75F);
                this.state.setAnimation(0, "defensive", true);

                inDefensive = true;
            }
        }
    }

    public void switchToOffensiveMode() {
        if (!inShattered) {
            if (inDefensive) {
                CardCrawlGame.sound.playA("GUARDIAN_ROLL_UP", .25F);
                this.stateData.setMix("defensive", "idle", 0.2F);
                this.state.setTimeScale(.75F);
                this.state.setAnimation(0, "idle", true);

                inDefensive = false;
            }
        } else {
            this.stateData.setMix("transition", "idle", 0.2F);
            this.state.setTimeScale(.75F);
            this.state.setAnimation(0, "idle", true);
            inShattered = false;
        }
    }

    public void switchToShatteredMode() {
        if (!inShattered) {
            if (inDefensive) {
                this.stateData.setMix("defensive", "transition", 0.2F);
                this.state.setTimeScale(.75F);
                this.state.setAnimation(0, "transition", true);
                inShattered = true;
            } else {
                this.stateData.setMix("idle", "transition", 0.2F);
                this.state.setTimeScale(.75F);
                this.state.setAnimation(0, "transition", true);
                inShattered = true;
            }
        }
    }

    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList();
        //TODO - Starting deck goes here

        retVal.add(Strike_Guardian.ID);
        retVal.add(Strike_Guardian.ID);
        retVal.add(Strike_Guardian.ID);
        retVal.add(Strike_Guardian.ID);
        retVal.add(Defend_Guardian.ID);
        retVal.add(Defend_Guardian.ID);
        retVal.add(Defend_Guardian.ID);
        retVal.add(Defend_Guardian.ID);

        retVal.add(CurlUp.ID);
        retVal.add(TwinSlam.ID);

        return retVal;
    }

    public ArrayList<String> getStartingRelics() {

        ArrayList<String> retVal = new ArrayList();
        retVal.add(ModeShifter.ID);
        UnlockTracker.markRelicAsSeen(ModeShifter.ID);
        return retVal;
    }

    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAME, DESCRIPTION, 80, 80, 3, 99, 5, this,

                getStartingRelics(), getStartingDeck(), false);

    }

    public String getTitle(PlayerClass playerClass) {
        return NAME;
    }

    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.GUARDIAN;
    }

    public Color getCardRenderColor() {

        return cardRenderColor;
    }

    public AbstractCard getStartCardForEvent() {
        //TODO - Note card goes here
        return new Gem_Red();
    }

    public Color getCardTrailColor() {
        return cardRenderColor.cpy();
    }

    public int getAscensionMaxHPLoss() {
        return 8;
    }

    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontGreen;
    }

    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("MONSTER_GUARDIAN_DESTROY", MathUtils.random(-0.1F, 0.1F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, true);
    }

    public String getCustomModeCharacterButtonSoundKey() {
        return "MONSTER_GUARDIAN_DESTROY";
    }

    public String getLocalizedCharacterName() {
        return NAME;
    }

    public AbstractPlayer newInstance() {
        return new GuardianCharacter(NAME, GuardianEnum.GUARDIAN);
    }

    public String getSpireHeartText() {
        return charStrings.TEXT[1];
    }

    public Color getSlashAttackColor() {
        return Color.FIREBRICK;
    }

    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, AbstractGameAction.AttackEffect.SLASH_VERTICAL, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, AbstractGameAction.AttackEffect.SLASH_VERTICAL, AbstractGameAction.AttackEffect.SLASH_HEAVY};
    }

    public String getVampireText() {
        return charStrings.TEXT[3];
    }

}


