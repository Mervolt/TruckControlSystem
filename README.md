# Truck Customs Control System
System designed to simulate truck movement during customs control

# System introduction and schema
![Schema of control] (images/Customs_Control_Schema.png)

Control Gate - Control gates are responsible for verifying cargo transported by trucks.
There are two control gates in simulation. Time units needed to process truck through cargo control equals weight of a truck.

Control Lane - Control lane is a queue in which trucks wait for access to control gate.
By default there can be 5 trucks in each control lane. There are 2 control lanes in simulation

Waiting Gate - Waiting Gate is a place where documents are verified.
Truck is placed in waiting gate only if there are no free places in control gates and control lanes.
As soon as there appears free slot in control lane, truck is moved.
There is only 1 waiting gate in simulation.

Waiting Lane - Waiting lane is a queue in which trucks wait for access to waiting gate.
There is no limit to trucks amount in waiting lane. There is 1 waiting lane in simulation.

Customs control can be divided into 2 stages
First - when truck arrives to Documents gateway and hands over control of the truck
Second - when truck is placed in control lane or control gate, controlled by staff.

The main goal of the simulation is to provide the shortest possible average waiting time
given restrictions that truck can be switched places only with corresponding truck in second queue.
Trucks actually processed in control gates and first in control lane cannot be switched.

The starting turn of the simulation is turn 0.


# Truck placement and route optimization

# Truck placement
As written before the main task is to provide shortest possible average time.
But due to possible arrival of unexpected truck or change of truck generation parameters
the best possible option for truck placement is to place it in control gate or in quicker control lane (if it has free places).
If there are no free places in control gates and lanes truck is placed in waiting gate or waiting lane if gate is taken.

#Route optimization
To improve performance of simulation, truck can be switched places with a corresponding truck from other control lane.
Trucks currently processed in control gates and first trucks in control lanes cannot be moved.
Also truck cannot be switched with empty place.

The first thing to notice is that the time needed for truck in control lane to be processed is
a sum of her weight and all its predecessors weights

The second thing to notice is that no time can be saved by switching trucks between lanes if there is same amount of trucks in both lanes.
So the only possibility to save time is to place lighter trucks in longer lanes

#Example
Given example where number in square represents a truck and its time needed to be processed

![First example of optimization] (images/Customs_Control_Example1.PNG)
We can count:
Time for trucks in first lane to be processed as: 3 + 11 + 20 + 32 + 39 = 105
(since time needed for truck to be processed is time needed for her and all her predecessors)

Time for trucks in second lane to be processed as: 5 + 20 + 24 + 30 + 40 = 119 

So average time for truck equals 22.4 time units [105 + 119 / 10 trucks in simulation]

![Second example of optimization] (images/Customs_Control_Example2.PNG)
We can count:
Time for first lane trucks: 3 + 11 + 15 + 27 + 34 = 90
Time for second lane trucks: 5 + 20 + 29 + 35 + 45 = 134
With same average time 22.4 units [90 + 134 / 10 trucks in simulation]

This is caused of course by the fact that truck's time to be processed affects only itself
and trucks behind it so it does not matter which lane it affects.

To see possible optimization we should consider example where there is not equal amount of trucks in both lanes
![Third example of optimization] (images/Customs_Control_Example3.PNG)

Time for first lane trucks: 3 + 11 + 20 + 32 + 39 = 105
Time for second lane trucks: 5 + 20 + 24 + 30 = 79
With average time: 20,(4)

Since truck affects itself and trucks behind it, we should place lighter trucks in longer queue
to achieve lower waiting time.
![Fourth example of optimization] (images/Customs_Control_Example4.png)

Time for first lane trucks: 3 + 11 + 15 + 21 + 28 = 78
Time for second lane trucks: 5 + 20 + 29 + 41 = 95
With average time: 19,(2)

#User interface

Operations avalaible for user:

- arrive(TruckWeight) -> TruckId
- status() -> [TruckStatus]
- step()
- waitingTime(TruckId) -> Time
- generation(Frequency, TruckWeightCap)
- generationOff()
- generationOn()
- print()





