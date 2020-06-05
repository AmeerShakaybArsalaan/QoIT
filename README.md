/********The code involves two source selection schemes i.e. QoI and QoIT.********/
Each scenario assumes 6 static source nodes and 30 (static/mobile) user nodes. We have provided the seeds i.e. seedRandom in "classParameters.java", 
which are used for different purposes in the code. However one can choose his/her own seeds randomly. We assume the following 3 scenarios:
a. Same vs Different Network Metric Thresholds for User Nodes, where user-nodes are static (Scenario 1)
b. Varying Number of Querying Nodes, where user-nodes are static (Scenario 2)
c. Less vs More User Nodes as compared to Source Nodes, where user-nodes might be static/mobile (Scenario 3)

Different settings are required for the code w.r.t each scenario, which are mentioned below.
Details of the code setting w.r.t Scenario 1:
1. Go to "classParameters.java"; Keep the value of "nSteps = 1" (for static network) // code-line 7
2. Go to "sourceSelection.java"; Choose the appropriate function with respect to Same/Different network metric thresholds //code-line 260,261

Details of the code setting w.r.t Scenario 2:
1. Go to "classParameters.java"; Keep the value of "nSteps = 1" (for static network) // code-line 7
2. Go to "classParameters.java"; Update the value of "userNodes" to 3, 6, 12, 18, 24, 30 as required. //code-line 8
3. Go to "sourceSelection.java"; Uncomment code-line 160-171, 258, 270; Comment code-line 177-178, 244-254, 257, 271

Details of the code setting w.r.t Scenario 3:
Number of querying nodes assumed are 3 (for "less" case) and 30 (for "more" case). The topology is considered 
both static and mobile. 
1. Go to "classParameters.java"; Update the value of "userNodes" to 3 or 30 (for less/more user nodes respectively) //code-line 8
2. Go to "classParameters.java"; Update the value of "nSteps = 1" (for static network) and "nSteps = 30" (for mobility) //code-line 7
3. Go to "classParameters.java"; 
   Update the value of min_speed_kmh = 0.0,  max_speed_kmh = 5.0  (walking speed) //code-line 85
                       min_speed_kmh = 7.5,  max_speed_kmh = 13.5 (running speed) //code-line 86
                       min_speed_kmh = 16.0, max_speed_kmh = 24.0 (cycling speed) //code-line 87
                       min_speed_kmh = 50.0, max_speed_kmh = 90.0 (driving speed) //code-line 88
4. Go to "sourceSelection.java"; Uncomment code-line 160-171, 258, 270; Comment code-line 177-178, 244-254, 257, 271
 
