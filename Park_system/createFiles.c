#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
#include <stdbool.h>
#include <unistd.h>
#include "estimate.h"


int createFiles(dockingSensor *sensor1, float estimatedTime, char* file_path, int isReestimate)
{


//create the estimate.data file
		
		FILE *fptr;
		
		fptr = fopen(file_path, "w");
		
		if(fptr == NULL)
		{
			printf("Error writing estimate data file at: %s\n", file_path);
			return 0; // return false
		}

	
		fprintf(fptr, "%s %d\n", "VehicleId:", sensor1->scooterId);
		fprintf(fptr, "%s %d\n", "BatteryCapacity:", sensor1->scooterBatteryCapacity);
		fprintf(fptr, "%s %d\n", "BatteryPercentage:", sensor1->scooterBatteryPercentage);
		fprintf(fptr, "%s %.2f\n","EstimatedTimeToFullCharge(min):", estimatedTime);
		fprintf(fptr, "%s %d\n","Reestimate:", isReestimate);
		
		fclose(fptr);
		printf("Created estimate .data file\n");
		
		
		char * file_path_flag = (char*)malloc(100 * sizeof(char));
		
//create the estimate.data.flag file
		strcpy(file_path_flag, file_path);
		strcat(file_path_flag, ".flag");
		
		fptr = NULL;
		
		fptr = fopen(file_path_flag, "w");
		
		if(fptr == NULL)
		{
			printf("Error writing estimate flag file!\n");
			return 0;
		}
		
		fclose(fptr);
		printf("Created estimate .flag file\n");
		
		

		free(file_path_flag);
		return 1; //return true
		
	}
