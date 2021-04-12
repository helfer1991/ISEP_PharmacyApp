#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
#include <unistd.h>
#include <math.h>
#include "estimate.h"

//check if dir exists
#include <sys/types.h>
#include <sys/stat.h>


int main (int argc, char *argv[]){
	
	
	char* output_directory = (char*) malloc(100 * sizeof(char));
	char* input_directory  = (char*) malloc(100 * sizeof(char));

//utilização de argumentos para identificar/criar directorios de input e output
	if(argc > 1)
	{
		
		if( strcmp(argv[1], "-d") == 0  &&  argc == 4)
		{
			strcpy(input_directory, argv[2]);
			strcpy(output_directory, argv[3]);
			
			struct stat st = {0};
			
			if (stat(input_directory, &st) == -1) 
			mkdir(input_directory, 0700); //700 so only the user can access
			
			
			if (stat(output_directory, &st) == -1) 
			mkdir(output_directory, 0700); // 700 so only the user can access
			printf("Using custom directories.\n");
			
		}
		else
		{
			 printf("To specify input ant output folder use '-d <input_directory> <output_directory>' . \n");
			 exit(1);
		}
	}
	else
	{
		strcpy(output_directory, "./output_files/");
		strcpy(input_directory, "./input_files/");
	}
		
	//struct to store sensor/vehicle information
	dockingSensor sensor1; 
	
	
	int charging_spaces_used = 1;
	dockingSensor* activeChargingSpaces = (dockingSensor*) calloc(charging_spaces_used, sizeof(dockingSensor));
	
	
	while(1)
	{
		
		//default values
		sensor1.chargerOutput = -1;
		sensor1.scooterId = -1;
		sensor1.scooterBatteryCapacity = -1;
		sensor1.scooterBatteryPercentage = -1;
		sensor1.correctlyDocked = 0;
		
		
		char* lockFlagFileName = (char*) malloc (100* sizeof(char));
		printf("\nWaiting for next input...\n");
		// watch the folder for files moved or created. If a file is detected then the name is stored in 'lockFlagFileName'
		check_dir_changes(input_directory, lockFlagFileName);
		
		
		//validates the file name. if it doesn't match lock_[datetime].data.flag the while loop restarts and waits for another file
		char bufTemp[10];
		strncpy( bufTemp, lockFlagFileName, 5 * sizeof(char) );
		bufTemp[5] = '\0';
		
		if ( strcmp( &lockFlagFileName[strlen(lockFlagFileName)-10], ".data.flag") != 0
				|| strcmp( bufTemp, "lock_" ) !=0 )
		{
			continue;
		}
		printf("Detected a .data.flag file, searching for a corresponding .data file...\n");


		//if its found a file with a correct name, then tries to read the corresponding data file
		char* lockDataFilePath = (char*) malloc (100* sizeof(char));
		strcpy(lockDataFilePath, input_directory);
		strcat(lockDataFilePath, lockFlagFileName);
		lockDataFilePath[strlen(lockDataFilePath)-5] = '\0';
		
		int read_result = readDataFile(lockDataFilePath, &sensor1);
		
		
		//if the data file was read correctly, deletes both .data and .flag files
		sensor1.correctlyDocked =  read_result; //returns 1 if read correctly | 0 if file not read
		
		if(read_result)
		{
			printf("Data file read successfully.\n");
			
			sleep(1); //to avoid errors while moving files into the directory
			char* lockFlagFilePath = (char*) malloc (100* sizeof(char));
			strcpy(lockFlagFilePath, input_directory);
			strcat(lockFlagFilePath, lockFlagFileName);
			
			if (remove(lockFlagFilePath) == 0) 
				printf("Deleted lock .flag file\n"); 
			else
				printf("Error deleting lock .flag file.\n"); 
				
						
			if (remove(lockDataFilePath) == 0) 
				printf("Deleted lock .data file\n"); 
			else
				printf("Error deleting lock .data file.\n"); 
				
			free(lockFlagFileName); 	
			free(lockFlagFilePath);
			free(lockDataFilePath);
		}
		else
		{
			printf("Data file not read correclty. Input files saved for analysis.\n");
			continue;
		}
		//------------------ends reading stage-----------------//
		
		
		
		
		activeChargingSpaces[charging_spaces_used-1] = sensor1;
		
		int charger_output_per_spot = roundf(sensor1.chargerOutput/charging_spaces_used);
		
		//allocate more memory for the next struct (that will store the information of the next vehicle)
		charging_spaces_used++;
		dockingSensor* tempArray = (dockingSensor*) realloc(activeChargingSpaces, charging_spaces_used * sizeof(dockingSensor) );
		if(tempArray != NULL){
			activeChargingSpaces = tempArray;
		}else{
			printf("error reallocating memory for new charging spots!\n");
		}
		
		
		char *timeStamp = (char*)malloc(50 * sizeof(char));
		char* estimateDataFileName = (char*) malloc (50* sizeof(char));
		char* estimateDataFilePath = (char*) malloc (50* sizeof(char));
		
		
		

		//iterates over each parked vehicle, re-estimates the charging time, and creates output files
		int i; 
		for (i = 0; i< (charging_spaces_used-1 ); i++){
			
				
			activeChargingSpaces[i].chargerOutput = charger_output_per_spot;
		
			//if (i > 0) é a segunda ou milesima vez que a estimativa está a ser calculada
			//posso enviar uma flag a indicar que é recalculo
			 
			
			//etimates charging time	

			float estimatedTime = -1;	//if vehicle is incorrectly docked estimate stays at -1
			
			if (activeChargingSpaces[i].correctlyDocked)
			{
				estimatedTime = (float) estimate(&activeChargingSpaces[i]); //função assembly que calcula a estimativa de tempo de carga
				if(estimatedTime != 0)
					//revert multiplication done inside assembly function
					//used in order to achieve a smaller "rounding" error  while using idiv instruction (remainder was discarded), since we don't use floats.
					estimatedTime = estimatedTime/100;  										
			}
			
			
			sleep(1);
			
			
			//creates the timeStamp
			time_t t = time(NULL);
			struct tm tm  = *localtime(&t);
			snprintf(timeStamp, 50, "%d_%02d_%02d_%02d_%02d_%02d",
			tm.tm_year + 1900, tm.tm_mon + 1, tm.tm_mday, tm.tm_hour, tm.tm_min, tm.tm_sec);
			
			//creates name for the file estimate_[datetime].data
			strcpy(estimateDataFileName, "estimate_");
			strcat(estimateDataFileName, timeStamp);
			strcat(estimateDataFileName, ".data");
			
			//prefixes the filename with the output_directory
			strcpy(estimateDataFilePath, output_directory);
			strcat(estimateDataFilePath, estimateDataFileName);
			
			//creates files estimate.data and  estimate.data.flag
			int isReestimate = 0;
			if(i>0 && activeChargingSpaces[i].correctlyDocked == 1){
				isReestimate = 1;
			}
			int write_result = createFiles(&activeChargingSpaces[i], estimatedTime, estimateDataFilePath, isReestimate);
			if(write_result)
			{
				printf("Done.\n\n");
			}
			else
			{
				printf("Error creating estimate files.\n");
			}
			
			
		}
			free(timeStamp);
			free(estimateDataFileName);
			free(estimateDataFilePath);
			
		
		printf("\n");
	
	}


	free(output_directory);
	free(input_directory);
	free(activeChargingSpaces);
	
	return 0;
}

	
	
