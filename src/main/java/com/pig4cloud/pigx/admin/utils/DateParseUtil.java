package com.pig4cloud.pigx.admin.utils;// 放到 common 工具包里
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateParseUtil {
    private DateParseUtil() {}

    private static final DateTimeFormatter[] DTS = new DateTimeFormatter[] {
        DateTimeFormatter.ISO_LOCAL_DATE_TIME,                 // 2025-10-18T11:23:45
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),    // 2025-10-18 11:23:45
        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),    // 2025/10/18 11:23:45
        DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"),    // 2025.10.18 11:23:45
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),             // 2025-10-18
        DateTimeFormatter.ofPattern("yyyy/MM/dd"),             // 2025/10/18
        DateTimeFormatter.ofPattern("yyyy.MM.dd")              // 2025.10.18
    };

    /** 安全解析：支持多种格式，失败返回 null；仅日期则按 00:00:00 */
    public static LocalDateTime parseLdtOrNull(String s) {
        if (s == null || s.isBlank()) return null;
        for (DateTimeFormatter f : DTS) {
            try {
                // 前三种是日期时间
                if (f == DTS[0] || f == DTS[1] || f == DTS[2] || f == DTS[3]) {
                    return LocalDateTime.parse(s.trim(), f);
                }
                // 后三种仅日期
                LocalDate d = LocalDate.parse(s.trim(), f);
                return d.atStartOfDay();
            } catch (Exception ignore) {}
        }
        return null;
    }
}
