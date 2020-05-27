import controlsystem.CustomsControl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TruckControlSystemRunner {
    public static void main(String[] args) {
        boolean running = true;
        CustomsControl control = new CustomsControl();
        TruckControlSystemRunner runner = new TruckControlSystemRunner();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            while(running){
                String input = reader.readLine();
                if (input.contains("print"))
                    control.printStatus();
                else if (input.contains("arrive")) {
                    int weight = Integer.parseInt(runner.getOneArgumentFromParenthesis(input));
                    control.arrive(weight);
                }
                else if(input.contains("waitingTime")) {
                    String truckId = runner.getOneArgumentFromParenthesis(input);
                    System.out.println(control.getWaitingTime(truckId));
                }
                else if (input.contains("step"))
                    control.step();
                else if (input.contains("status"))
                    control.status();
                else if (input.contains("quit"))
                    running = false;
                else if (input.contains("generationOff"))
                    control.turnOffGeneration();
                else if (input.contains("generationOn"))
                    control.turnOnGeneration();
                else if (input.contains("generation")){
                    String[] params = runner.getTwoArgumentsFromParenthesis(input);
                    int firstParam = Integer.parseInt(params[0]);
                    int secondParam = Integer.parseInt(params[1]);
                    control.changeGenerationParams(firstParam, secondParam);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Please provide correct input");
        }
    }

    private String getOneArgumentFromParenthesis(String input){
        String[] firstSplit = input.trim().split("\\)");
        String[] secondSplit = firstSplit[0].trim().split("\\(");
        return secondSplit[1].trim();
    }

    private String[] getTwoArgumentsFromParenthesis(String input){
        String[] firstSplit = input.trim().split("\\)");
        String[] secondSplit = firstSplit[0].trim().split("\\(");
        String[] thirdSplit = secondSplit[1].trim().split(",");
        String[] result = new String[2];
        result[0] = thirdSplit[0].trim();
        result[1] = thirdSplit[1].trim();
        return result;
    }
}
