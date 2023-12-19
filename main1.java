import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Scenario {
    private String text;
    private Map<String, String> options;

    public Scenario(String text) {
        this.text = text;
        this.options = new HashMap<>();
    }

    public void addOption(String optionText, String nextScenario) {
        options.put(optionText, nextScenario);
    }

    public String getText() {
        return text;
    }

    public Map<String, String> getOptions() {
        return options;
    }
}

class ScenarioManager {
    private Map<String, Scenario> scenarios;

    public ScenarioManager() {
        this.scenarios = new HashMap<>();
    }

    public void loadScenarios(String folderPath) {
        try {
            File folder = new File(folderPath);
            File[] files = folder.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".txt")) {
                        String scenarioName = file.getName().replace(".txt", "");
                        String scenarioContent = new String(Files.readAllBytes(file.toPath()));
                        Scenario scenario = parseScenario(scenarioContent);
                        scenarios.put(scenarioName, scenario);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Scenario parseScenario(String content) {
        String[] lines = content.split("\n");
        Scenario scenario = new Scenario(lines[0].trim());

        for (int i = 1; i < lines.length; i++) {
            String[] parts = lines[i].trim().split(":");
            if (parts.length == 2) {
                scenario.addOption(parts[0].trim(), parts[1].trim());
            }
        }

        return scenario;
    }

    public Scenario getScenario(String scenarioName) {
        return scenarios.get(scenarioName);
    }
}

public class main1 {
    public static void main(String[] args) {
        ScenarioManager scenarioManager = new ScenarioManager();
        scenarioManager.loadScenarios("scenarios");

        Scanner scanner = new Scanner(System.in);
        String currentScenario = "start";

        while (true) {
            Scenario scenario = scenarioManager.getScenario(currentScenario);

            System.out.println(scenario.getText());
            Map<String, String> options = scenario.getOptions();

            for (String optionText : options.keySet()) {
                System.out.println(optionText);
            }

            System.out.print("Выберете вариант: ");
            String userChoice = scanner.nextLine();

            if (options.containsKey(userChoice)) {
                currentScenario = options.get(userChoice);
            } else {
                System.out.println("Неправильно введен вариант выбора, попробуйте снова");
            }


            if ("end".equals(currentScenario)) {
                System.out.println("Поздравляем, вы прошли игру");
                break;
            }
        }

        scanner.close();
    }
}
