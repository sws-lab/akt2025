package week6.lausearvutus;

import java.util.Map;

/**
 * Klassi Formula ei ole vaja muuta
 * (aga tõenäoliselt tahad luua sellele konkreetseid alamklasse).
 *
 * Soovi korral võid siia ka meetodeid juurde lisada.
 */
public abstract class Formula {
    /**
     * Konkreetsete valemi puhul peab toString andma valemi tavapärasel
     * infikssel kujul, aga nii, et kõigi binaarsete tehete ümber on sulud
     * ja eituste ning lausemuutujate ümber (vahetult) sulge ei ole.
     * Näiteks valemile A∨B&(¬(¬C)) vastava Formula korral peaks toString-i
     * tulemus olema "(A∨(B&¬¬C))"; valemi A&B&C&D puhul peab tulemus olema
     * "(((A&B)&C)&D)".
     * <p>
     * Tulemus ei tohi sisaldada tühikuid.
     */
    @Override
    abstract public String toString();


    /**
     * Konkreetsetes alamklassides peab equals tagastama true parajasti siis,
     * kui argumendiks on Formula, millel on täpselt sama struktuur
     * (sh. muutujanimed), nagu sellel valemil.
     */
    @Override
    abstract public boolean equals(Object obj);

    /**
     * Konkreetsetes alamkassides peab evaluate tagastama valemi väärtuse
     * näidatud väärtustusel.
     * <p>
     * Kui mõne muutuja väärtus puudub, siis tuleb visata erind
     * (seda ka siis, kui valemi väärtus selle muutuja väärtusest ei sõltu).
     */
    abstract public boolean evaluate(Map<Character, Boolean> variableValues);

}
