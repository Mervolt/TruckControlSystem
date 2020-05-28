package controlsystem;

import java.util.List;
public class CustomsControlStatusPrinter {
    private CustomsControlPrinterHelper helper;
    private int lineCounter;
    StringBuilder customsControlBuilder;
    ControlLane firstLane;
    ControlLane secondLane;
    WaitingLane waitingLane;

    public CustomsControlStatusPrinter(ControlLane firstLane, ControlLane secondLane, WaitingLane waitingLane) {
        this.firstLane = firstLane;
        this.secondLane = secondLane;
        this.waitingLane = waitingLane;
        this.helper = new CustomsControlPrinterHelper();
    }

    /**
     * prints to console status of simulation
     */
    public void print(){
        customsControlBuilder = new StringBuilder();
        appendWall();
        for(lineCounter = 0; lineCounter < 18; lineCounter ++){
            appendControlBoard();

        }
        appendWall();
        System.out.println(customsControlBuilder.toString());

    }

    private void appendWall(){
        customsControlBuilder.append(new String(new char[helper.fullBoardSize]).replace("\0", "-"));
        customsControlBuilder.append("\n");
        customsControlBuilder.append("\n");
    }

    private void appendControlBoard(){
        if(lineCounter == 0) {
            appendToWaitingGatePosition();
            customsControlBuilder.append(helper.documentBoxName);
            appendFromWaitingGateToControlGatePosition();
            customsControlBuilder.append(helper.firstControlBoxName);
        }

        else if(lineCounter == 1 || lineCounter == 17) {
            appendToWaitingGatePosition();
            appendSideOfBox();
            appendFromWaitingGateToControlGatePosition();
            appendSideOfBox();
        }

        else if(lineCounter == 2 || lineCounter == 3 || lineCounter == 5 || lineCounter == 6 ||
                lineCounter == 12 || lineCounter == 13 || lineCounter == 15 || lineCounter == 16){
            appendToWaitingGatePosition();
            appendEmptyBox();
            appendFromWaitingGateToControlGatePosition();
            appendEmptyBox();
        }

        else if(lineCounter == 4){
            appendToWaitingGatePosition();
            appendEmptyBox();
            appendTruckLine(helper.firstControlBoxName);
            appendBoxWithTruck(helper.firstControlBoxName);
        }

        else if(lineCounter == 7 || lineCounter == 11){
            appendToWaitingGatePosition();
            appendEmptyBox();
            appendFromWaitingGateToControlGatePosition();
            appendSideOfBox();
        }

        else if(lineCounter == 8){
            appendToWaitingGatePosition();
            appendEmptyBox();
        }

        else if(lineCounter == 9){
            appendTruckLine(helper.documentBoxName);
            /*Just to equate*/
            customsControlBuilder.append(new String(new char[8]));
            appendBoxWithTruck(helper.documentBoxName);
        }

        else if(lineCounter == 10){
            appendToWaitingGatePosition();
            appendEmptyBox();
            appendFromWaitingGateToControlGatePosition();
            customsControlBuilder.append(helper.secondControlBoxName);
        }

        else if(lineCounter == 14){
            appendToWaitingGatePosition();
            appendEmptyBox();
            appendTruckLine(helper.secondControlBoxName);
            appendBoxWithTruck(helper.secondControlBoxName);
        }
        customsControlBuilder.append("\n");
    }

    private void appendSideOfBox(){
        customsControlBuilder.append("+")
                .append(new String(new char[helper.gateWidth]).replace("\0", "-"))
                .append("+");
    }

    private void appendEmptyBox(){
        customsControlBuilder.append("+")
                .append(new String(new char[helper.gateWidth]))
                .append("+");
    }

    private void appendToWaitingGatePosition(){
        customsControlBuilder.append(new String(new char[helper.waitingGatePosition]));
    }

    private void appendFromWaitingGateToControlGatePosition(){
        customsControlBuilder.append(new String(new char[helper.controlGatePosition - helper.waitingGatePosition
                - helper.gateWidth - 2]));
    }

    private void appendTruckLine(String boxName){
        int laneCapacity = 0;
        ILane lane = null;
        if(boxName.equals(helper.firstControlBoxName)) {
            lane = firstLane;
            laneCapacity = firstLane.getLaneCapacity();
        }
        else if(boxName.equals(helper.secondControlBoxName)) {
            lane = secondLane;
            laneCapacity = secondLane.getLaneCapacity();
        }
        else if(boxName.equals(helper.documentBoxName)) {
            lane = waitingLane;
            laneCapacity = helper.boardWaitingLaneCapacity;
        }
        else {
            appendFromWaitingGateToControlGatePosition();
            return;
        }

        List<Truck> trucks = lane.getTrucks();
        customsControlBuilder.append("  ");
        int truckAmount = trucks.size();

        for(int lackingTrucks = 0; lackingTrucks < laneCapacity - truckAmount; lackingTrucks++)
            customsControlBuilder.append("            ");

        for(int truckIterator = trucks.size() - 1; truckIterator >= 0; truckIterator--){
            appendTruckToLine(trucks.get(truckIterator));
        }

        customsControlBuilder.append("  ");
    }

    private void appendTruckToLine(Truck truck){
        customsControlBuilder.append(" | ");
        int truckNumber = getTruckNumber(truck.getTruckId());
        appendTruckNumber(truckNumber);
        appendTruckWeight(truck.getWeightAmount());
        customsControlBuilder.append("|");
    }



    private int getTruckNumber(String truckId){
        String[] truckIdSplit = truckId.split("-");
        String truckNumberString = truckIdSplit[1].trim();
        return Integer.parseInt(truckNumberString);
    }

    private void appendTruckNumber(int truckNumber){
        customsControlBuilder.append("T-");
        if(truckNumber < 10)
            customsControlBuilder.append(truckNumber).append("  ");
        else if(truckNumber < 100)
            customsControlBuilder.append(truckNumber).append(" ");
        else
            customsControlBuilder.append(truckNumber);
    }

    private void appendTruckWeight(int truckWeight){
        if(truckWeight < 10)
            customsControlBuilder.append(truckWeight).append("  ");
        else if(truckWeight < 100)
            customsControlBuilder.append(truckWeight).append(" ");
        else
            customsControlBuilder.append(truckWeight);
    }

    private void appendBoxWithTruck(String boxName){
        Truck truck = null;
        ILane lane = null;
        if(boxName.equals(helper.firstControlBoxName) && !firstLane.getGate().isEmpty()) {
            truck = firstLane.getGate().getProcessedTruck();
            lane = firstLane;
        }
        else if(boxName.equals(helper.secondControlBoxName) && !secondLane.getGate().isEmpty()) {
            truck = secondLane.getGate().getProcessedTruck();
            lane = secondLane;
        }
        else if(boxName.equals(helper.documentBoxName) && !waitingLane.getGate().isEmpty()) {
            truck = waitingLane.getGate().getProcessedTruck();
            lane = waitingLane;
        }
        if(truck != null) {
            customsControlBuilder.append("+ ");
            if(lane != waitingLane)
                appendTruckToBox(truck, lane);
            else
                appendTruckToWaitingGate(truck, lane);
            customsControlBuilder.append(" +");
        }
        else
            appendEmptyBox();
    }

    private void appendTruckToWaitingGate(Truck truck, ILane lane) {
        int truckNumber = getTruckNumber(truck.getTruckId());
        appendTruckNumber(truckNumber);
        appendTruckProcessedTime(lane.getGate().getTimeInProcess());
        customsControlBuilder.append("/   ");
    }

    private void appendTruckToBox(Truck truck, ILane lane) {
        int truckNumber = getTruckNumber(truck.getTruckId());
        appendTruckNumber(truckNumber);
        appendTruckProcessedTime(lane.getGate().getTimeInProcess());
        customsControlBuilder.append("/");
        appendTruckWeight(truck.getWeightAmount());
    }

    private void appendTruckProcessedTime(int processedTime) {
        if(processedTime < 10)
            customsControlBuilder.append(processedTime).append("  ");
        else if(processedTime < 100)
            customsControlBuilder.append(processedTime).append(" ");
        else
            customsControlBuilder.append(processedTime);
    }


}
