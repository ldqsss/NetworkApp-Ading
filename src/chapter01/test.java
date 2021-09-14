package chapter01;
import java.time.LocalDateTime;

public class test {
    public static void main(String[] args) {
        System.out.printf(String.valueOf(LocalDateTime.now().withNano(0)));
    }
}
