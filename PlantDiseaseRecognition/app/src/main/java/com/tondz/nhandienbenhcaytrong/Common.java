package com.tondz.nhandienbenhcaytrong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileNotFoundException;

public class Common {
    public static Bitmap bitmap;
    public static String result = "";
    public static String[] caPhes = new String[]{
            "đốm rồng", "nấm rỉ sắt", "phấn trắng", "sâu vẽ bùa"
    };
    public static String[] sauRiengs = new String[]{
            "Đốm lá tảo", "Bốc lá", "Đốm lá", "Không bệnh"
    };
    public static String[] bos = new String[]{
            "Bị Bệnh", "Khoẻ mạnh"
    };
    public static String[] chuaBo = new String[]{
            "1. Nhận biết bệnh:\n" +
                    "Quan sát kỹ lá: Các đốm, vết loét, màu sắc bất thường, lá bị xoăn, rụng...\n" +
                    "Kiểm tra gốc: Có dấu hiệu thối rễ, vàng lá, chảy nhựa không?\n" +
                    "Kiểm tra môi trường: Đất quá ẩm ướt, quá khô, thiếu ánh sáng, hoặc có nhiều sâu bệnh khác.\n" +
                    "2. Các biện pháp phòng trị:\n" +
                    "Vệ sinh vườn:\n" +
                    "Tỉa bỏ những cành lá bị bệnh, lá vàng úa.\n" +
                    "Thu gom và tiêu hủy lá bệnh để tránh lây lan.\n" +
                    "Làm sạch cỏ dại xung quanh gốc cây.\n" +
                    "Điều chỉnh môi trường:\n" +
                    "Tưới nước vừa đủ, tránh ngập úng hoặc quá khô hạn.\n" +
                    "Tạo độ thông thoáng cho cây bằng cách tỉa cành lá.\n" +
                    "Bón phân hợp lý:\n" +
                    "Bổ sung các loại phân bón cần thiết cho cây, đặc biệt là các loại phân vi sinh để tăng cường sức đề kháng.\n" +
                    "Phun thuốc:\n" +
                    "Khi đã xác định được loại bệnh: Sử dụng các loại thuốc đặc trị theo khuyến cáo của nhà sản xuất.\n" +
                    "Một số loại thuốc phổ biến: Đồng oxyclorua, Mancozeb, Carbendazim...\n" +
                    "Lưu ý: Luôn đọc kỹ hướng dẫn sử dụng và tuân thủ đúng liều lượng, thời gian phun.\n" +
                    "Các bệnh thường gặp trên cây bơ và cách phòng trị:\n" +
                    "Bệnh đốm lá: Do nấm gây ra, thường xuất hiện các đốm tròn hoặc bầu dục trên lá.\n" +
                    "Cách chữa: Phun thuốc gốc đồng, tỉa bỏ lá bệnh.\n" +
                    "Bệnh cháy lá: Lá bị khô, chuyển màu nâu và rụng.\n" +
                    "Cách chữa: Kiểm tra xem có bị sâu bệnh tấn công không, bổ sung phân bón, tưới nước đầy đủ.\n" +
                    "Bệnh thối rễ: Gây vàng lá, rụng lá, cây còi cọc.\n" +
                    "Cách chữa: Cắt bỏ phần rễ bị thối, xử lý vết cắt bằng thuốc sát trùng, trồng lại cây vào đất tơi xốp.", ""
    };

    public static String[] chuaCaPhe = new String[]{
            "Bệnh Đốm Rồng (Cercospora coffeicola)\n" +
                    "Đặc điểm:\n" +
                    "Vết bệnh ban đầu là những chấm nhỏ màu nâu nhạt, sau đó lớn dần và có hình dạng giống như chiếc lá của cây trúc đào (đốm rồng).\n" +
                    "Vết bệnh thường xuất hiện ở mặt trên lá, sau đó lan xuống mặt dưới.\n" +
                    "Khi bệnh nặng, lá bị rụng nhiều, cây còi cọc, giảm năng suất.\n" +
                    "Phương pháp phòng trị:\n" +
                    "Vệ sinh vườn: Thu gom và tiêu hủy lá bệnh, tỉa cành tạo tán thoáng.\n" +
                    "Luân canh cây trồng: Trồng xen canh các loại cây khác nhau để giảm áp lực bệnh hại.\n" +
                    "Sử dụng giống kháng bệnh: Chọn giống cà phê có khả năng kháng bệnh đốm rồng.\n" +
                    "Phun thuốc: Sử dụng các loại thuốc đặc trị như: Mancozeb, Carbendazim, Đồng oxyclorua... phun định kỳ theo hướng dẫn của nhà sản xuất.",
            "Bệnh Nấm Rỉ Sắt (Hemileia vastatrix)\n" +
                    "Đặc điểm:\n" +
                    "Vết bệnh ban đầu là những chấm nhỏ màu vàng nhạt ở mặt dưới lá.\n" +
                    "Sau đó, vết bệnh phát triển thành những mụn nhỏ màu vàng cam, vỡ ra tạo thành lớp bột màu vàng cam.\n" +
                    "Lá bị nhiễm bệnh nặng sẽ rụng sớm, cây suy yếu, giảm năng suất.\n" +
                    "Phương pháp phòng trị:\n" +
                    "Sử dụng giống kháng bệnh: Đây là biện pháp hiệu quả nhất để phòng trừ bệnh nấm rỉ sắt.\n" +
                    "Vệ sinh vườn: Thu gom và tiêu hủy lá bệnh, tỉa cành tạo tán thoáng.\n" +
                    "Phun thuốc: Sử dụng các loại thuốc đặc trị như: Propiconazole, Triazole, Strobilurin... phun định kỳ theo hướng dẫn của nhà sản xuất.",
            "Bệnh Phấn Trắng (Oidium)\n" +
                    "Đặc điểm:\n" +
                    "Trên lá, quả và cành non xuất hiện lớp bột màu trắng.\n" +
                    "Lá bị nhiễm bệnh thường biến dạng, cuộn lại, rụng sớm.\n" +
                    "Quả bị nhiễm bệnh thường bị thối, rụng.\n" +
                    "Phương pháp phòng trị:\n" +
                    "Vệ sinh vườn: Thu gom và tiêu hủy các bộ phận bị bệnh.\n" +
                    "Tỉa cành tạo tán: Tạo tán thoáng để ánh sáng chiếu vào, giảm độ ẩm.\n" +
                    "Phun thuốc: Sử dụng các loại thuốc đặc trị như: Sulfur, Triazole... phun định kỳ theo hướng dẫn của nhà sản xuất.",
            " Sâu Vẽ Bùa\n" +
                    "Đặc điểm:\n" +
                    "Sâu non gặm nhu mô lá, chỉ để lại lớp biểu bì, tạo thành những đường ngoằn ngoèo trên lá.\n" +
                    "Lá bị hại nặng sẽ khô héo và rụng.\n" +
                    "Phương pháp phòng trị:\n" +
                    "Săn bắt thủ công: Vào sáng sớm hoặc chiều mát, tiến hành bắt sâu bằng tay.\n" +
                    "Sử dụng bẫy đèn: Thu hút và tiêu diệt sâu trưởng thành.\n" +
                    "Phun thuốc: Sử dụng các loại thuốc trừ sâu sinh học hoặc hóa học có nguồn gốc thực vật như: Neem, Bt... phun theo hướng dẫn của nhà sản xuất."
    };
    public static String[] chuaSauRieng = new String[]{
            "Bệnh Đốm Lá Tảo\n" +
                    "Đặc điểm:\n" +
                    "Vết bệnh thường xuất hiện ở mặt trên lá, có hình tròn hoặc bầu dục, hơi nhô lên bề mặt lá.\n" +
                    "Ban đầu, vết bệnh có màu xanh xám, sau đó chuyển sang màu nâu đỏ.\n" +
                    "Khi bệnh nặng, lá bị nhiều đốm, giảm khả năng quang hợp, cây sinh trưởng kém.\n" +
                    "Nguyên nhân: Do tảo gây ra.\n" +
                    "Phòng trị:\n" +
                    "Vệ sinh vườn: Tỉa bỏ cành lá bị bệnh, thu gom và tiêu hủy.\n" +
                    "Phun thuốc: Sử dụng các loại thuốc gốc đồng như: Coc 85, Champion, Norshield... phun định kỳ.\n" +
                    "Tăng cường dinh dưỡng: Bổ sung các loại phân bón giúp cây tăng sức đề kháng.",
            " Bệnh Bốc Lá\n" +
                    "Đặc điểm:\n" +
                    "Lá bị bệnh thường xuất hiện các đốm tròn hoặc bầu dục, màu nâu đen.\n" +
                    "Vết bệnh lan rộng nhanh, làm lá bị cháy, khô và rụng.\n" +
                    "Cây bị bệnh nặng sẽ còi cọc, giảm năng suất.\n" +
                    "Nguyên nhân: Có thể do nhiều nguyên nhân như: nấm bệnh, vi khuẩn, hoặc do điều kiện môi trường bất lợi.\n" +
                    "Phòng trị:\n" +
                    "Vệ sinh vườn: Tỉa bỏ cành lá bị bệnh, thu gom và tiêu hủy.\n" +
                    "Phun thuốc: Sử dụng các loại thuốc đặc trị như: Mancozeb, Carbendazim, Đồng oxyclorua... phun định kỳ.\n" +
                    "Điều chỉnh môi trường: Tạo điều kiện thông thoáng cho vườn cây, tránh úng nước.",
            "Bệnh Đốm Lá (chung)\n" +
                    "Đặc điểm:\n" +
                    "Lá xuất hiện các đốm màu nâu, vàng hoặc đen, hình dạng và kích thước đốm bệnh khác nhau tùy theo loại nấm gây bệnh.\n" +
                    "Lá bị bệnh thường rụng sớm, cây sinh trưởng kém.\n" +
                    "Nguyên nhân: Có nhiều loại nấm khác nhau gây ra bệnh đốm lá trên sầu riêng.\n" +
                    "Phòng trị:\n" +
                    "Vệ sinh vườn: Tỉa bỏ cành lá bị bệnh, thu gom và tiêu hủy.\n" +
                    "Phun thuốc: Sử dụng các loại thuốc đặc trị như: Mancozeb, Carbendazim, Đồng oxyclorua... phun định kỳ.\n" +
                    "Luân canh cây trồng: Tránh trồng sầu riêng liên tục trên một diện tích.",
            ""
    };


    public static Bitmap decodeUri(Uri selectedImage, Context context) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 400;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);
    }

}
