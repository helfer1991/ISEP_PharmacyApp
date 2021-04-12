#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
#include <stdbool.h>
#include <unistd.h>
#include "estimate.h"



int readDataFile(char * dataFilePath, dockingSensor *sensor1){
	
	//array of pointers created to assign values to struct members through a loop
	int *struct_members_ptrs[4] = { &sensor1->chargerOutput,
								&sensor1->scooterId, 
								&sensor1->scooterBatteryCapacity,
								&sensor1->scooterBatteryPercentage};
	

		FILE *dataFile_ptr;
		dataFile_ptr = fopen(dataFilePath, "r");
		
		//checks if file is empty
		int c = fgetc(dataFile_ptr);
		if (c == EOF) {
			printf("Data file is empty!\n");
			fclose(dataFile_ptr);
			return 0;
		} else {
			ungetc(c, dataFile_ptr);
		}
		
		
		if(dataFile_ptr == NULL) //dataFile NOT found
		{
			printf("Flag file read but there is no matching data file.\n");
			return 0; //returns false
		}
		
		else //dataFile found
		{
			char buf[100];
			int temp;
			int idx=0;
			int size_of_array = sizeof(struct_members_ptrs)/sizeof(struct_members_ptrs[0]);

		//reads the values from the file
			for(idx = 0; idx < size_of_array; idx++)
			{
				fscanf(dataFile_ptr,"%*s %s ",buf);
				temp  = atoi(buf);
				if (temp < 0 )
				{
					//if any value is invalid, it is substituted by -1
					printf("Invalid value inside .data file. Only positive values are accepted.\n");
					*struct_members_ptrs[idx] = -1;
				}
				else
					*struct_members_ptrs[idx] = temp;
			}
			
			
			
		//validate if batterypercentage is over 100
			if(sensor1->scooterBatteryPercentage > 100)
			{
				printf("Invalid value inside .data file. Battery percentage can't be over 100.\n");
				sensor1->scooterBatteryPercentage = -1;
				fclose(dataFile_ptr);
				return 0;
			}

		//if any of the inputs where invalid the method returns false
			for(idx = 0; idx < size_of_array; idx++)
			{
				if(*struct_members_ptrs[idx] == -1)
				{
					fclose(dataFile_ptr);
					return 0;
				}
			}
		//validate if value read was a string or empty (atoi function outputs 0) 	
			if(sensor1->scooterId ==  0 || sensor1->scooterBatteryCapacity == 0 || sensor1->chargerOutput == 0)
			{
				printf("Invalid value inside .data file. VehicleId, batteryCapacity and chargerOutput are mandatory inputs.\n");
				sensor1->scooterId = -1;
				sensor1->scooterBatteryCapacity = -1; 
				sensor1->scooterBatteryPercentage = -1;
				sensor1->chargerOutput = -1;
				fclose(dataFile_ptr);
				return 0;
			}
			
		//if this line is reached, return true
			fclose(dataFile_ptr);
			return 1; 
		}
	
}
