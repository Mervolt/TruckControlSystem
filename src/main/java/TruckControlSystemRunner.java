import controlsystem.CustomsControl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TruckControlSystemRunner {
    public static void main(String[] args) {
        boolean running = true;
        CustomsControl control = new CustomsControl();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            while(running){
                String input = reader.readLine();
                if (input.contains("print"))
                    control.printStatus();
                else if (input.contains("arrive")) {
                    String[] firstSplit = input.split("\\)");
                    String[] secondSplit = firstSplit[0].split("\\(");
                    int weight = Integer.parseInt(secondSplit[1]);
                    control.arrive(weight);
                }
                else if (input.contains("step"))
                    control.step();
                else if (input.contains("status"))
                    System.out.println("TODO");
                else if(input.contains("waitingTime"))
                    System.out.println("TODO");
                else if (input.contains("quit"))
                    running = false;
            }
            /*TODO REFACTOR INPUT*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
