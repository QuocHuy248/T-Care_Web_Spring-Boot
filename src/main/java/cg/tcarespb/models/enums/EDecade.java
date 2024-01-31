package cg.tcarespb.models.enums;

public enum EDecade {


    THIRTY("30s"),
    FORTY("40s"),
    FIFTY("50s"),
    SIXTY("60s"),
    SEVENTY("70s"),
    EIGHTY("80s"),
    NINETY("90s");
    private String name;

    EDecade(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}