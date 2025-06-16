public enum Livraison{DOMICILE("C"), MAGASIN("M");
private final String code;

    Livraison(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Livraison fromCode(String code) {
        for (Livraison mode : Livraison.values()) {
            if (mode.code.equalsIgnoreCase(code)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Code inconnu : " + code);
    }
}

