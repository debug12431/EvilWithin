package automaton.cards;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Separator extends AbstractBronzeCard {

    public final static String ID = makeID("Separator");

    //stupid intellij stuff skill, self, common


    public Separator() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        thisEncodes();
        baseMagicNumber = magicNumber = 2;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void onCompile(AbstractCard function, boolean forGameplay) {
        if (forGameplay) {
            atb(new GainEnergyAction(1));
            if (!firstCard() && !lastCard()) {
                atb(new GainEnergyAction(magicNumber));
            }
        }
    }

    public void upp() {
        upgradeBaseCost(0);
    }
}