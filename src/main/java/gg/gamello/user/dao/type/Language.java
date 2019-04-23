package gg.gamello.user.dao.type;

public enum Language {
    pl,
    en,
    es;

    private static Language getDefaultLanguage(){
        return Language.en;
    }

    public static Language mapLanguage(String languageString){
        try {
            return Language.valueOf(languageString.toLowerCase());
        }catch (IllegalArgumentException ex){
            return Language.getDefaultLanguage();
        }
    }
}
