import ui.UserInterfaceRunner;
import ui.impl.ConsoleUserInterfaceRunner;

public class Main {
    private static UserInterfaceRunner userInterfaceRunner = new ConsoleUserInterfaceRunner();

    public static void main(String[] args) {
        userInterfaceRunner.runApplication();
    }
}
