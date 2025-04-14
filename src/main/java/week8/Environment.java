package week8;

public class Environment<T> {

    /**
     * Esialgu peaks olemas olema globaalne skoop, kuhu saab muutujaid deklareerida enne ühtegi skoopi (plokki) sisenemist.
     */
    public Environment() {
    }

    /**
     * Deklareerib praeguses skoobis uue muutuja.
     */
    public void declare(String variable) {
        throw new UnsupportedOperationException();
    }

    /**
     * Omistab muutujale uue väärtuse kõige sisemises skoobis, kus see muutuja deklareeritud on.
     */
    public void assign(String variable, T value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Deklareerib praeguses skoobis uue muutuja ja omistab sellele väärtuse.
     */
    public void declareAssign(String variable, T value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Tagastab muutuja praeguse väärtuse kõige sisemises skoobis, kus see muutuja deklareeritud on.
     * Deklareerimata või väärtustamata muutujate korral peaks tagastama {@code null}.
     */
    public T get(String variable) {
        throw new UnsupportedOperationException();
    }

    /**
     * Tähistab uude skoopi (plokki) sisenemist.
     * Uues skoobis võib üle deklareerida välimiste välimise skoobi muutujaid.
     */
    public void enterBlock() {
        throw new UnsupportedOperationException();
    }

    /**
     * Tähistab praegusest skoobist (plokist) väljumist.
     * Unustama peaks kõik sisemises skoobis deklareeritud muutujad.
     */
    public void exitBlock() {
        throw new UnsupportedOperationException();
    }
}
