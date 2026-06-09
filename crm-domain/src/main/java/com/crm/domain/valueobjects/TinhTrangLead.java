package com.crm.domain.valueobjects;

public enum TinhTrangLead {

    Moi,
    DangChamSoc,
    DaChuyenDoi,
    NgungChamSoc;


    public static TinhTrangLead from(String value) {
        if (value == null || value.isBlank()) {
            return Moi;
        }
        try {
            return TinhTrangLead.valueOf(value);
        } catch (IllegalArgumentException ex) {
            return Moi;
        }
    }


    public boolean canTransitionTo(TinhTrangLead next) {
        if (next == null) return false;
        return switch (this) {
            case Moi          -> next == DangChamSoc || next == NgungChamSoc;
            case DangChamSoc  -> next == DaChuyenDoi || next == NgungChamSoc;
            case DaChuyenDoi  -> false;
            case NgungChamSoc -> false;
        };
    }


    public boolean coTheConvert() {
        return this == DangChamSoc;
    }
}