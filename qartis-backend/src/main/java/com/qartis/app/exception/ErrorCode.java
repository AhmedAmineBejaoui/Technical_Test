package com.qartis.app.exception;

import lombok.Getter;

/**
 * Énumération des codes d'erreur avec traductions bilingues (EN/AR)
 * Tous les codes d'erreur de l'application doivent être définis ici
 */
@Getter
public enum ErrorCode {
    // Erreurs d'authentification
    BAD_CREDENTIALS(
            "INVALID_CREDENTIALS",
            "Invalid email or password",
            "بيانات اعتماد غير صحيحة"),
    UNAUTHORIZED(
            "UNAUTHORIZED",
            "Authentication required",
            "مطلوب المصادقة"),
    ACCESS_DENIED(
            "ACCESS_DENIED",
            "Insufficient permissions",
            "صلاحيات غير كافية"),
    TOKEN_EXPIRED(
            "TOKEN_EXPIRED",
            "Token has expired",
            "انتهت صلاحية الرمز"),
    INVALID_TOKEN(
            "INVALID_TOKEN",
            "Invalid or malformed token",
            "رمز غير صحيح أو مشوه"),

    // Erreurs de ressource
    RESOURCE_NOT_FOUND(
            "RESOURCE_NOT_FOUND",
            "Resource not found",
            "لم يتم العثور على المورد"),
    USER_NOT_FOUND(
            "USER_NOT_FOUND",
            "User not found",
            "لم يتم العثور على المستخدم"),
    COMPANY_NOT_FOUND(
            "COMPANY_NOT_FOUND",
            "Company not found",
            "لم يتم العثور على الشركة"),

    // Erreurs de conflit
    EMAIL_ALREADY_EXISTS(
            "EMAIL_ALREADY_EXISTS",
            "Email address is already registered",
            "عنوان البريد الإلكتروني مسجل بالفعل"),
    COMPANY_ALREADY_EXISTS(
            "COMPANY_ALREADY_EXISTS",
            "You already have an associated company",
            "لديك بالفعل شركة مرتبطة"),
    RESOURCE_ALREADY_EXISTS(
            "RESOURCE_ALREADY_EXISTS",
            "Resource already exists",
            "المورد موجود بالفعل"),

    // Erreurs de validation
    VALIDATION_FAILED(
            "VALIDATION_FAILED",
            "Request validation failed",
            "فشل التحقق من الطلب"),
    INVALID_REQUEST(
            "INVALID_REQUEST",
            "Invalid request",
            "طلب غير صحيح"),
    MISSING_FIELD(
            "MISSING_FIELD",
            "Required field is missing",
            "الحقل المطلوب مفقود"),
    INVALID_FORMAT(
            "INVALID_FORMAT",
            "Invalid format",
            "صيغة غير صحيحة"),

    // Erreurs métier
    BUSINESS_ERROR(
            "BUSINESS_ERROR",
            "Business rule violation",
            "انتهاك قاعدة العمل"),
    NO_COMPANY(
            "NO_COMPANY",
            "No company associated with your account",
            "لا توجد شركة مرتبطة بحسابك"),
    INACTIVE_ACCOUNT(
            "INACTIVE_ACCOUNT",
            "Your account is inactive",
            "حسابك غير نشط"),
    NOT_AUTHENTICATED(
            "NOT_AUTHENTICATED",
            "User is not authenticated",
            "المستخدم غير مصرح"),

    // Erreurs serveur
    INTERNAL_SERVER_ERROR(
            "INTERNAL_SERVER_ERROR",
            "An internal server error occurred",
            "حدث خطأ في الخادم"),
    SERVICE_UNAVAILABLE(
            "SERVICE_UNAVAILABLE",
            "Service is temporarily unavailable",
            "الخدمة غير متاحة مؤقتاً");

    private final String code;
    private final String messageEn;
    private final String messageAr;

    ErrorCode(String code, String messageEn, String messageAr) {
        this.code = code;
        this.messageEn = messageEn;
        this.messageAr = messageAr;
    }

    /**
     * Obtient le message dans la langue spécifiée
     * 
     * @param language "en" ou "ar"
     * @return Message traduit
     */
    public String getMessage(String language) {
        if ("ar".equalsIgnoreCase(language)) {
            return messageAr;
        }
        return messageEn; // Par défaut EN
    }

    /**
     * Obtient le message en anglais
     */
    public String getMessageEn() {
        return messageEn;
    }

    /**
     * Obtient le message en arabe
     */
    public String getMessageAr() {
        return messageAr;
    }
}
