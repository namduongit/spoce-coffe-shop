package KhachHang;

import Utils.Date;
import Utils.Function;
import Utils.IXuat;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings("resource")
public class QLKhachHang implements IXuat {
    public ArrayList<KhachHang> customerList; // Array List để lưu các khách hàng

    // Phương thức khởi tạo phi tham số sẽ cấp phát bộ nhớ cho ArrayList
    public QLKhachHang() {
        this.customerList = new ArrayList<>();
    }

    // Phương thức khởi tạo với tham số là một Array List
    public QLKhachHang(ArrayList<KhachHang> customerList) {
        this.customerList = customerList;
    }

    // Phương thức để nhập dữ liệu từ file vào Array List
    public void Init() {
        File customerFile = new File("../File/customer.txt");
        try (Scanner sc = new Scanner(customerFile)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split("\\|");
                KhachHang khachHang = null;
                MemberCard card = null;
                boolean status = false;
                if (parts[3].equals("1")) {
                    Date ngayHetHanThe = new Date(parts[12], parts[13], parts[14]); // 12/13/2024
                    LocalDate localDate = LocalDate.now();
                    String currentDate = localDate.getDayOfMonth() + "/" + localDate.getMonthValue() + "/"
                            + localDate.getYear();
                    card = new MemberCard(parts[4], parts[5], new Date(parts[6], parts[7], parts[8]),
                            new Date(parts[9], parts[10], parts[11]),
                            new Date(parts[12], parts[13], parts[14]), Integer.parseInt(parts[15]));

                    if (ngayHetHanThe.toString().equals(currentDate)) {
                        card.setPoint(0);
                        card.setStartDate(card.getEndDate());
                        card.getEndDate().setYear(Integer.parseInt(card.getEndDate().getYear()) + 2 + "");
                    }
                    status = true;
                }
                if (parts[0].equals("0")) {
                    khachHang = new KHTaiCho(parts[1], parts[2], status, card);
                } else if (parts[0].equals("1")) {
                    khachHang = new KHMangDi(parts[1], parts[2], status, card);
                }
                this.customerList.add(khachHang);
            }
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }

        this.writeAll();
    }

    // Phương thức để ghi dữ liệu từ mảng vào file
    public void writeAll() {
        try (FileWriter writer = new FileWriter("../File/customer.txt", false)) {
            for (KhachHang kh : this.customerList) {
                writer.write(kh.makeString() + "\n");
            }
            writer.flush();
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    // Phương thức để thêm một khách hàng vào Array List
    public void addCustomer() {
        Scanner sc = new Scanner(System.in);
        String str;
        KhachHang kh = null;

        while (true) {
            System.out.println(
                    "\n\t==========================================================================================");
            System.out.printf("\t| %-87s |%n", "Chọn loại khách hàng:");
            System.out.printf("\t| %-5s %-81s |%n", "1.", "Khách hàng mang đi");
            System.out.printf("\t| %-5s %-81s |%n", "2.", "Khách hàng dùng tại chỗ");
            System.out.println(
                    "\t============================================================================================");
            System.out.print("\t=> Nhập lựa chọn: ");
            str = sc.nextLine();

            if (Function.isEmpty(str)) {
                System.out.println("\tLựa chọn không được rỗng!");
                continue;
            }

            if (!Function.isTrueNumber(str)) {
                System.out.println("\tLựa chọn phải là số!");
                continue;
            }

            if (Integer.parseInt(str) < 0) {
                System.out.println("\tLựa chọn không được có giá trị âm!");
                continue;
            }

            switch (Integer.parseInt(str)) {
                case 1:
                    kh = new KHMangDi();
                    break;

                case 2:
                    kh = new KHTaiCho();
                    break;

                default:
                    System.out.println("\tLựa chọn không hợp lệ!");
                    continue;
            }

            kh.nhapThongTin();
            this.customerList.add(kh);
            this.writeAll();
            System.out.println("\tThêm khách hàng thành công!");
            break;
        }
    }

    // Phương thức để xóa một khách hàng khỏi Array List
    public void removeCustomer() {
        this.xuatThongTin();
        Scanner sc = new Scanner(System.in);
        String str;

        while (true) {
            System.out.println(
                    "\n\t==========================================================================================");
            System.out.printf("\t| %-87s |%n", "Bạn muốn xóa khách hàng theo ID hay theo tên?");
            System.out.printf("\t| %-5s %-81s |%n", "1.", "ID");
            System.out.printf("\t| %-5s %-81s |%n", "2.", "Tên khách hàng");
            System.out.println(
                    "\t============================================================================================");
            System.out.print("\t=> Nhập lựa chọn: ");
            str = sc.nextLine();

            if (Function.isEmpty(str)) {
                System.out.println("\tLựa chọn không được rỗng!");
                continue;
            }

            if (!Function.isTrueNumber(str)) {
                System.out.println("\tLựa chọn phải là số!");
                continue;
            }

            switch (str) {
                case "1":
                    while (true) {
                        boolean isDone = false;
                        System.out.print("\n\t=> Mời bạn nhập ID khách hàng: ");
                        str = sc.nextLine();

                        if (Function.isEmpty(str)) {
                            System.out.println("\tID không được rỗng!");
                            continue;
                        }

                        for (KhachHang kh : this.customerList) {
                            if (kh.getCustomerID().equals(str)) {
                                this.customerList.remove(kh);
                                isDone = true;
                                break;
                            }
                        }

                        if (!isDone) {
                            System.out.println("\tKhông tìm thấy khách hàng!\n");
                            continue;
                        } else {
                            this.writeAll();
                            System.out.println("\tXóa khách hàng thành công!");
                        }
                        break;
                    }
                    break;

                case "2":
                    while (true) {
                        boolean isDone = false;
                        System.out.print("\n\t=> Mời bạn nhập tên khách hàng: ");
                        str = sc.nextLine();

                        if (Function.isEmpty(str)) {
                            System.out.println("\tTên khách hàng không được rỗng!");
                            continue;
                        }

                        if (Function.isTrueNumber(str)) {
                            System.out.println("\tTên khách hàng không được là số!");
                            continue;
                        }

                        str = Function.normalizeName(str);

                        for (KhachHang kh : this.customerList) {
                            if (kh.getCustomerName().equals(str)) {
                                this.customerList.remove(kh);
                                isDone = true;
                                break;
                            }
                        }

                        if (!isDone) {
                            System.out.println("\tKhông tìm thấy khách hàng!");
                            continue;
                        } else {
                            this.writeAll();
                            System.out.println("\tXóa khách hàng thành công!");
                        }

                        break;
                    }
                    break;

                default:
                    System.out.println("\tLựa chọn không hợp lệ!");
                    continue;
            }

            break;
        }
    }

    // Phương thức để in ra các khách hàng trong mảng
    @Override
    public void xuatThongTin() {
        for (KhachHang kh : this.customerList) {
            kh.xuatThongTin();
        }
    }

    // Hàm để sửa thông tin khách hàng
    public void modifyCustomer() {
        Scanner sc = new Scanner(System.in);
        String str;

        this.xuatThongTin();

        while (true) {
            System.out.println("\n\tBạn muốn sửa thông tin khách hàng theo ID hay theo tên?");
            System.out.println("\t1. ID");
            System.out.println("\t2. Tên khách hàng");
            System.out.print("\t=> Nhập lựa chọn: ");
            str = sc.nextLine();

            if (Function.isEmpty(str)) {
                System.out.println("\tLựa chọn không được rỗng!");
                continue;
            }

            if (!Function.isTrueNumber(str)) {
                System.out.println("\tLựa chọn phải là số!");
                continue;
            }

            switch (str) {
                case "1":
                    while (true) {
                        boolean isDone = false;
                        System.out.print("\n\t=> Mời bạn nhập ID khách hàng: ");
                        str = sc.nextLine();

                        if (Function.isEmpty(str)) {
                            System.out.println("\tID không được rỗng!");
                            continue;
                        }

                        for (KhachHang kh : this.customerList) {
                            if (kh.getCustomerID().equals(str)) {
                                kh.suaThongTin();
                                isDone = true;
                                break;
                            }
                        }

                        if (!isDone) {
                            System.out.println("\tKhông tìm thấy khách hàng!\n");
                            continue;
                        } else {
                            this.writeAll();
                        }

                        break;
                    }
                    break;

                case "2":
                    while (true) {
                        boolean isDone = false;
                        System.out.print("\n\t=> Mời bạn nhập tên khách hàng: ");
                        str = sc.nextLine();

                        if (Function.isEmpty(str)) {
                            System.out.println("\tTên khách hàng không được rỗng!");
                            continue;
                        }

                        if (Function.isTrueNumber(str)) {
                            System.out.println("\tTên khách hàng không được là số!");
                            continue;
                        }

                        str = Function.normalizeName(str);

                        for (KhachHang kh : this.customerList) {
                            if (kh.getCustomerName().equals(str)) {
                                kh.suaThongTin();
                                isDone = true;
                                break;
                            }
                        }

                        if (!isDone) {
                            System.out.println("\tKhông tìm thấy khách hàng!");
                            continue;
                        } else {
                            this.writeAll();
                        }

                        break;
                    }
                    break;

                default:
                    System.out.println("\tLựa chọn không hợp lệ!");
                    continue;
            }

            break;
        }
    }

    public void resetList() {
        Scanner sc = new Scanner(System.in);
        String str;

        while (true) {
            System.out.println("\n\tBạn có chắc chắn muốn xoá toàn bộ danh sách không?");
            System.out.println("\t1. Có");
            System.out.println("\t2. Không");
            System.out.print("\t=> Nhập lựa chọn: ");
            str = sc.nextLine();

            if (Function.isEmpty(str)) {
                System.out.println("\tLựa chọn không được rỗng!");
                continue;
            }

            if (!Function.isTrueNumber(str)) {
                System.out.println("\tLựa chọn phải là số!");
                continue;
            }

            switch (str) {
                case "1":
                    this.customerList.clear();
                    System.out.println("\tLàm mới danh sách thành công!");
                    break;

                case "2":
                    System.out.println("\tHủy bỏ làm mới danh sách!");
                    break;

                default:
                    System.out.println("\tLựa chọn không hợp lệ!");
                    continue;
            }

            break;
        }
    }

    public void listItem() {
        int dineInCustomer = 0;
        int takeAwayCustomer = 0;
        for (KhachHang kh : this.customerList) {
            if (kh instanceof KHMangDi) {
                takeAwayCustomer++;
            } else if (kh instanceof KHTaiCho) {
                dineInCustomer++;
            }
        }
        System.out.println("\tSố loại khách hàng: ");
        System.out.println("\t1. Khách hàng mang đi: " + takeAwayCustomer);
        System.out.println("\t2. Khách hàng uống tại chỗ: " + dineInCustomer);
    }

    public void findCustomer() {
        Scanner sc = new Scanner(System.in);
        String str;

        while (true) {
            Function.clearScreen();
            System.out.println("\t============================[ Tìm kiếm khách hàng ]=========================");
            System.out.printf("\t| %-4s | %-65s |%n", "STT", "Chức năng");
            System.out.println(
                    "\t|------|-------------------------------------------------------------------|");
            System.out.printf("\t| %-4s | %-65s |%n", "1", "Tìm kiếm theo ID");
            System.out.printf("\t| %-4s | %-65s |%n", "2", "Tìm kiếm theo tên");
            System.out.printf("\t| %-4s | %-65s |%n", "3", "Quay về trang trước");
            System.out.println("\t============================================================================");
            System.out.print("\t=> Nhập lựa chọn: ");
            str = sc.nextLine();

            if (Function.isEmpty(str)) {
                System.out.println("\tLựa chọn không được rỗng!");
                continue;
            }

            if (!Function.isTrueNumber(str)) {
                System.out.println("\tLựa chọn phải là số!");
                continue;
            }

            switch (str) {
                case "1":
                    while (true) {
                        System.out.print("\t=> Nhập ID muốn tìm (bao gồm mã): ");
                        str = sc.nextLine();

                        if (Function.isEmpty(str)) {
                            System.out.println("\tID nhập vào không được rỗng!");
                            continue;
                        }

                        KhachHang khachhang = null;
                        for (KhachHang kh : this.customerList) {
                            if (kh.getCustomerID().equals(str)) {
                                khachhang = kh;
                                break;
                            }
                        }

                        if (khachhang == null) {
                            System.out.println("\tKhông tìm thấy khách hàng nào có ID: " + str);
                        } else {
                            System.out.println("\tKết quả tìm kiếm: ");
                            khachhang.xuatThongTin();
                        }

                        break;
                    }
                    break;

                case "2":
                    while (true) {
                        System.out.print("\t=> Nhập tên muốn tìm: ");
                        str = sc.nextLine();

                        if (Function.isEmpty(str)) {
                            System.out.println("\tTên không được rỗng!");
                            continue;
                        }

                        if (Function.isTrueNumber(str)) {
                            System.out.println("\tTên không được là số!");
                            continue;
                        }

                        KhachHang khachhang = null;
                        for (KhachHang kh : this.customerList) {
                            if (kh.getCustomerName().equalsIgnoreCase(str)) {
                                khachhang = kh;
                                break;
                            }
                        }

                        if (khachhang == null) {
                            System.out.println("\tKhông tìm thấy khách hàng nào có tên: " + str);
                        } else {
                            System.out.println("\tKết quả tìm kiếm: ");
                            khachhang.xuatThongTin();
                        }
                        break;
                    }
                    break;

                case "3":
                    System.out.println("\tThoát tìm kiếm!");
                    Function.clearScreen();
                    break;

                default:
                    System.out.println("\tVui lòng chọn trong khoảng 1 đến 3 !");
                    continue;
            }

            break;
        }
    }

    public void menuQLKhachHang() {
        Function.clearScreen();
        this.Init(); // Khởi tạo dữ liệu
        Scanner sc = new Scanner(System.in);
        String str;

        while (true) {
            // In tiêu đề
            Function.clearScreen();
            System.out.println(
                    "\t==============================[ Menu Quản Lý Khách Hàng ]===============================");

            // In tiêu đề các cột
            System.out.printf("\t| %-4s | %-77s |%n", "STT", "Chức năng");
            System.out.println(
                    "\t|------|-------------------------------------------------------------------------------|");

            // In danh sách các lựa chọn
            System.out.printf("\t| %-4s | %-77s |%n", "1", "In danh sách khách hàng");
            System.out.printf("\t| %-4s | %-77s |%n", "2", "Thêm/sửa/xóa thông tin khách hàng");
            System.out.printf("\t| %-4s | %-77s |%n", "3", "Cập nhật và làm mới danh sách");
            System.out.printf("\t| %-4s | %-77s |%n", "4", "Tìm kiếm");
            System.out.printf("\t| %-4s | %-77s |%n", "5", "Làm mới màn hình");

            System.out.printf("\t| %-4s | %-77s |%n", "6", "Thoát chương trình quản lý");

            // In dòng kẻ dưới cùng
            System.out.println(
                    "\t========================================================================================");
            System.out.print("\t[Manage] Nhập lựa chọn: ");
            str = sc.nextLine();
            if (Function.isEmpty(str)) {
                System.out.println("\tVui lòng không để trống !");
            } else {
                if (Function.isTrueNumber(str)) {
                    int number = Integer.parseInt(str);
                    if (number >= 1 && number <= 6) {
                        if (number == 1) {
                            this.xuatThongTin();
                            System.out.print("\tEnter để tiếp tục!");
                            str = sc.nextLine();
                        }
                        if (number == 2) {
                            this.MenuChinhSua();
                            System.out.print("\tEnter để tiếp tục!");
                            str = sc.nextLine();
                        }
                        if (number == 3) {
                            this.capnhatChinhSua();
                            System.out.print("\tEnter để tiếp tục!");
                            str = sc.nextLine();
                        }
                        if (number == 4) {
                            this.findCustomer();
                            System.out.print("\tEnter để tiếp tục!");
                            str = sc.nextLine();
                        }
                        if (number == 5) {
                            Function.clearScreen();
                        }
                        if (number == 6) {
                            break;
                        }
                    } else {
                        System.out.println("\tVui lòng chọn trong khoảng 1 đến 11 !");
                    }
                } else {
                    System.out.println("\tVui lòng nhập số !");
                }
            }
        }

    }

    public void MenuChinhSua() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            Function.clearScreen();
            System.out.println("\t============================[ Menu thêm/sửa/xóa ]===========================");
            System.out.printf("\t| %-4s | %-65s |%n", "STT", "Chức năng");
            System.out.println(
                    "\t|------|-------------------------------------------------------------------|");
            System.out.printf("\t| %-4s | %-65s |%n", "1", "Thêm một khách hàng (Tự động lưu vào File)");
            System.out.printf("\t| %-4s | %-65s |%n", "2", "Xoá một khách hàng (Tự động load vào File)");
            System.out.printf("\t| %-4s | %-65s |%n", "3", "Sửa thông tin khách hàng");
            System.out.printf("\t| %-4s | %-65s |%n", "4", "Quay lại menu chính");
            System.out.println("\t============================================================================");
            System.out.print("\t[Làm mới] Nhập lựa chọn: ");
            String str = sc.nextLine();

            switch (str) {
                case "1":
                    this.addCustomer();
                    pause(sc);
                    break;

                case "2":
                    this.removeCustomer();
                    pause(sc);
                    break;

                case "3":
                    this.modifyCustomer();
                    pause(sc);
                    break;

                case "4":
                    return;

                default:
                    System.out.println("\tLựa chọn không hợp lệ! Hãy thử lại.");
                    pause(sc);
                    break;
            }
        }
    }

    public void capnhatChinhSua() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            Function.clearScreen();
            System.out.println("\t==============================[ Menu cập nhật và làm mới ]==============================");
            System.out.printf("\t| %-4s | %-77s |%n", "STT", "Chức năng");
            System.out.println(
                    "\t|------|-------------------------------------------------------------------------------|");
            System.out.printf("\t| %-4s | %-77s |%n", "1", "Cập nhật lại khách hàng vào File từ danh sách");
            System.out.printf("\t| %-4s | %-77s |%n", "2", "Cập nhật lại khách hàng vào danh sách từ File");
            System.out.printf("\t| %-4s | %-77s |%n", "3",
                    "Làm mới danh sách khách hàng (Reset dữ liệu nhưng không load vào File)");
            System.out.printf("\t| %-4s | %-77s |%n", "4", "Quay lại menu chính");
            System.out.println("\t========================================================================================");
            System.out.print("\t[Làm mới] Nhập lựa chọn: ");
            String str = sc.nextLine();

            switch (str) {
                case "1":
                    this.customerList.clear();
                    this.Init();
                    pause(sc);
                    break;

                case "2":
                    this.writeAll();
                    pause(sc);
                    break;

                case "3":
                    this.resetList();
                    pause(sc);

                case "4":
                    return;

                default:
                    System.out.println("\tLựa chọn không hợp lệ! Hãy thử lại.");
                    pause(sc);
                    break;
            }
        }

    }

    public void in() {
        while (true) {
            Scanner sc = new Scanner(System.in);
            Function.clearScreen();
            System.out.println("\t============================[ Menu in danh sách ]===========================");
            System.out.printf("\t| %-4s | %-60s |%n", "STT", "Chức năng");
            System.out.println(
                    "\t|------|--------------------------------------------------------------|");
            System.out.printf("\t| %-4s | %-77s |%n", "1", "In danh sách khách hàng");
            System.out.printf("\t| %-4s | %-77s |%n", "2", "In danh sách khách hàng từng loại");
            System.out.printf("\t| %-4s | %-60s |%n", "3", "Quay lại menu chính");
            System.out.println("\t=======================================================================");
            System.out.print("\t[Làm mới] Nhập lựa chọn: ");
            String str = sc.nextLine();

            switch (str) {
                case "1":
                    this.xuatThongTin();
                    pause(sc);
                    break;

                case "2":
                    this.listItem();
                    pause(sc);
                    break;

                case "3":
                    return;

                default:
                    System.out.println("\tLựa chọn không hợp lệ! Hãy thử lại.");
                    pause(sc);
                    break;
            }
        }

    }

    private void pause(Scanner sc) {
        System.out.print("\tNhấn Enter để tiếp tục...");
        sc.nextLine();
    }

    public static void main(String[] args) {
        QLKhachHang ql = new QLKhachHang();
        ql.menuQLKhachHang();
    }

}
