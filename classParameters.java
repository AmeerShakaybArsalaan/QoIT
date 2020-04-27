import java.util.*;

public class classParameters {
	static sourceSelection ss = new sourceSelection();
	static double  gridSize = 20.0, key = 4.0, deltaT_hr = 1.0/60.0; // in bush-fire situation, we have new available data after 1.0 minute
	static int     totalUserNodes = 36; // 16, 26, 36, 46, 56, 66
	static int     nUAVs = 0, totalNetworkNodes = totalUserNodes + nUAVs,  nSteps = 1; //nSteps = 1;
	static int 	   userNodes = 30;
	static int[]   sourceNodeList = {0, 1, 2, 3, 4, 5};
	static boolean print_condition = false;
	static String  root_path = "C:/Users/a1703149/eclipse-workspace/Paper 2 Code for Users with same Quality Metric Weights/";	

	//9535,436789,41792,1734,23764263,30000,15000,14081947,124000,2827417,63134,1744967,8368,864,99263,834653,
	
	// Parameters to change for mobility scenario
	
	/**********Seed************/
	/************* for less User-Nodes than Source-Nodes **************/
	//static int     seedRandom = 747;  //good
	//static int     seedRandom = 478;    //good
	//static int     seedRandom = 1361;
	//static int     seedRandom = 20159;
	//static int     seedRandom = 5368976;
	//static int     seedRandom = 655000;
	//static int     seedRandom = 105;
	//static int     seedRandom = 13579;
	//static int     seedRandom = 259;
	//static int     seedRandom = 2134;
	//static int     seedRandom = 7623;
	//static int     seedRandom = 78253;
	//static int     seedRandom = 82754;
	//static int     seedRandom = 9237;
	//static int     seedRandom = 676;
	//static int     seedRandom = 3433;
	//static int     seedRandom = 8888;
	//static int     seedRandom = 23423; //good
	//static int     seedRandom = 76432; //good
	//static int     seedRandom = 95348;   //good
	
	// more User-Nodes than Source-Nodes
	//static int     seedRandom = 777; 
	//static int     seedRandom = 23215;
	//static int     seedRandom = 3355;
	//static int     seedRandom = 350000;	//4
	//static int     seedRandom = 5432;
	//static int     seedRandom = 275317;
	//static int     seedRandom = 1361;		//7
	//static int     seedRandom = 20159;
	//static int     seedRandom = 5368976;
	//static int     seedRandom = 10011987;   //10
	//static int     seedRandom = 400000;
	//static int     seedRandom = 137030;
	//static int     seedRandom = 655000;	//13
	//static int     seedRandom = 2222;		
	//static int     seedRandom = 44375;
	//static int     seedRandom = 35000;    //16
	//static int     seedRandom = 678263;
	//static int     seedRandom = 105;		//18
	//static int     seedRandom = 823;
	//static int     seedRandom = 13579;
	
	/******** for changing number of user nodes in network ********/
	static int     seedRandom = 23215;
	//static int     seedRandom = 350000;	//2
	//static int     seedRandom = 275317;
	//static int     seedRandom = 1361;		//4
	//static int     seedRandom = 20159;
	//static int     seedRandom = 5368976;  //6
	//static int     seedRandom = 400000;
	//static int     seedRandom = 137030;   //8
	//static int     seedRandom = 655000;			
	//static int     seedRandom = 44375;    //10
	//static int     seedRandom = 105;		
	//static int     seedRandom = 823;		//12
	//static int     seedRandom = 13579;
	//static int     seedRandom = 2423;   //14
	//static int     seedRandom = 786;
	//static int     seedRandom = 87654;  //16
	//static int     seedRandom = 95348;
	//static int     seedRandom = 23423;  //18
	//static int     seedRandom = 478;
	//static int     seedRandom = 19344;  //20
	
	/**********Mobility Speed************/
	static double  min_speed_kmh = 0.0, max_speed_kmh = 5.0;    // walking speed
	//static double  min_speed_kmh = 7.5, max_speed_kmh = 13.5;   // running speed
	//static double  min_speed_kmh = 16.0, max_speed_kmh = 24.0;  // cycling speed
	//static double  min_speed_kmh = 50.0, max_speed_kmh = 90.0;  // car speed

	static Integer destNodes_Seed[] = new Integer[nSteps];
	// Seeds generation for random selection of destination-nodes w.r.t each iteration. These seeds will remain the same for all mobility speeds.
	public static void destNodes_Seed() {
		Random rand = new Random(seedRandom);
		for(int i = 0; i < nSteps; i++) {          // nStep random seeds
			int temp_seed = (int) (rand.nextDouble()*seedRandom);
			if (i == 0) {				
				if(temp_seed >= totalNetworkNodes)  destNodes_Seed[i] = temp_seed;     // if seed-value >= totalNetworkNodes					
				else							    i--;                               // seed will be unable to generate proper destination-nodes					
			}
			else {
				if(temp_seed < totalNetworkNodes || ss.contains(destNodes_Seed, temp_seed) == true)  i--;               // if seed < no.of n/w nodes or seed already exists					  
				else                      							destNodes_Seed[i] = temp_seed;					
			}
		}
	}
}
