package com.gregbclement.spellingtime;

import java.util.List;

public class SpellingWordDefinition {
    private List<String> definitions;
    private String sentence;
    private String alteranteUrl;

    public List<String> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<String> definitions) {
        this.definitions = definitions;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getAlteranteUrl() {
        return alteranteUrl;
    }

    public void setAlteranteUrl(String alteranteUrl) {
        this.alteranteUrl = alteranteUrl;
    }
}
