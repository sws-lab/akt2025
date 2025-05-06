package week8;

import java.util.*;

public class Environment<T> {
    private final Deque<Map<String, T>> envs = new ArrayDeque<>();

    /**
     * Esialgu peaks olemas olema globaalne skoop, kuhu saab muutujaid deklareerida enne ühtegi skoopi (plokki) sisenemist.
     */
    public Environment() {
        enterBlock();
    }

    /**
     * Deklareerib praeguses skoobis uue muutuja.
     */
    public void declare(String variable) {
        // 1. lahendus
        //envs.peekFirst().put(variable, null);
        // 2. lahendus
        declareAssign(variable, null);
    }

    /**
     * Omistab muutujale uue väärtuse kõige sisemises skoobis, kus see muutuja deklareeritud on.
     */
    public void assign(String variable, T value) {
        Map<String, T> env = findEnv(variable);
        if (env != null)
            env.put(variable, value);
    }

    /**
     * Deklareerib praeguses skoobis uue muutuja ja omistab sellele väärtuse.
     */
    public void declareAssign(String variable, T value) {
        // 1. lahendus
        //declare(variable);
        //assign(variable, value);
        // 2. lahendus
        envs.peekFirst().put(variable, value);
    }

    /**
     * Tagastab muutuja praeguse väärtuse kõige sisemises skoobis, kus see muutuja deklareeritud on.
     * Deklareerimata või väärtustamata muutujate korral peaks tagastama {@code null}.
     */
    public T get(String variable) {
        Map<String, T> env = findEnv(variable);
        return env != null ? env.get(variable) : null;
    }

    /**
     * Tähistab uude skoopi (plokki) sisenemist.
     * Uues skoobis võib üle deklareerida välimiste välimise skoobi muutujaid.
     */
    public void enterBlock() {
        envs.addFirst(new HashMap<>());
    }

    /**
     * Tähistab praegusest skoobist (plokist) väljumist.
     * Unustama peaks kõik sisemises skoobis deklareeritud muutujad.
     */
    public void exitBlock() {
        envs.removeFirst();
    }

    private Map<String, T> findEnv(String variable) {
        for (Map<String, T> env : envs) {
            if (env.containsKey(variable))
                return env;
        }
        return null;
    }
}
