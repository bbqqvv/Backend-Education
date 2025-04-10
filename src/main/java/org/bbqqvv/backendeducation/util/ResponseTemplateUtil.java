package org.bbqqvv.backendeducation.util;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class ResponseTemplateUtil {

    public static String getCountStudentsByClass(String className, long count) {
        List<String> templates = List.of(
                "Lớp %s hiện có %d học sinh nhé!",
                "Có tất cả %d bạn đang học ở lớp %s.",
                "Sĩ số lớp %s là %d học sinh.",
                "Bạn đang hỏi lớp %s hả? Có %d học sinh đó."
        );
        return formatRandom(templates, className, count);
    }

    public static String getListStudentsByClass(String className, String names) {
        List<String> templates = List.of(
                "Dưới đây là danh sách học sinh lớp %s: %s.",
                "Học sinh lớp %s gồm có: %s.",
                "Lớp %s có các bạn: %s.",
                "Danh sách lớp %s nè: %s."
        );
        return formatRandom(templates, className, names);
    }

    public static String getCountTeachersByClass(String className, long count) {
        List<String> templates = List.of(
                "Có tổng cộng %d giáo viên đang dạy lớp %s.",
                "Lớp %s hiện có %d giáo viên phụ trách.",
                "Số giáo viên dạy lớp %s là %d người.",
                "Lớp %s được %d giáo viên giảng dạy."
        );
        return formatRandom(templates, count, className);
    }

    public static String getListClassesOfTeacher(String teacherName, Set<String> classes) {
        String joined = String.join(", ", classes);
        List<String> templates = List.of(
                "Thầy/cô %s đang giảng dạy các lớp: %s.",
                "Các lớp mà giáo viên %s phụ trách là: %s.",
                "Giáo viên %s hiện đang dạy các lớp sau: %s.",
                "Lớp học của %s: %s."
        );
        return formatRandom(templates, teacherName, joined);
    }

    public static String getRandomSchoolIntroduction() {
        List<String> introductions = List.of(
                "Trường THPT TCV là một trong những ngôi trường hàng đầu khu vực với bề dày truyền thống hơn 20 năm. Nơi đây không chỉ đào tạo kiến thức mà còn chú trọng phát triển toàn diện cho học sinh.",
                "Tự hào là ngôi trường giàu thành tích, THPT TCV mang đến môi trường học tập hiện đại, năng động và thân thiện với học sinh.",
                "THPT TCV nổi bật với đội ngũ giáo viên giỏi chuyên môn, cơ sở vật chất hiện đại và phong trào học tập, hoạt động ngoại khoá vô cùng sôi nổi.",
                "Với khẩu hiệu 'Học để làm người', THPT TCV luôn đặt chất lượng giáo dục và đạo đức học sinh lên hàng đầu, tạo nên một cộng đồng học tập đáng tự hào.",
                "Không chỉ chú trọng kiến thức, THPT TCV còn là nơi nuôi dưỡng đam mê, sáng tạo và tinh thần trách nhiệm cho thế hệ trẻ.",
                "Trường THPT TCV tự hào là môi trường giáo dục toàn diện với các chương trình đào tạo tiên tiến, giúp học sinh phát triển cả về học lực lẫn kỹ năng sống.",
                "THPT TCV là mái trường thân yêu nơi thầy cô luôn đồng hành cùng học sinh trong từng bước trưởng thành, khơi nguồn cảm hứng học tập mỗi ngày."
        );
        return introductions.get(ThreadLocalRandom.current().nextInt(introductions.size()));
    }

    private static String formatRandom(List<String> templates, Object... args) {
        String selected = templates.get(ThreadLocalRandom.current().nextInt(templates.size()));
        return String.format(selected, args);
    }
}
